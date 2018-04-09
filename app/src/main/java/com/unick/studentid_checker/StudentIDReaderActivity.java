package com.unick.studentid_checker;

import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.unick.studentid_checker.fragments.CreateFragment;
import com.unick.studentid_checker.fragments.ListFragment;
import com.unick.studentid_checker.fragments.MainFragment;
import com.unick.studentid_checker.models.Order;
import com.unick.studentid_checker.models.User;

public class StudentIDReaderActivity extends AppCompatActivity {

    private NfcAdapter mNfcAdapter;
    private PendingIntent pi;
    private String color1;
    private String color2;
    private String color3;
    private String color;

    private String date;
    private String schoolname;
    private String CardId;
    private String StudentID;

    private int stock1;
    private int stock2;
    private int stock3;

    EditText editText_nfc_ID;
    EditText editText_cup_color;
    Button button_enter;
    private DatabaseReference mDatabase;
    private DatabaseReference queryIDRef;


    TextView stock_name_1;
    TextView stock_amount_1;
    TextView stock_name_2;
    TextView stock_amount_2;
    TextView stock_name_3;
    TextView stock_amount_3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_idreader);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        editText_nfc_ID = (EditText) findViewById(R.id.nfc_ID);
        editText_cup_color = (EditText) findViewById(R.id.cup_color);
        button_enter = (Button) findViewById(R.id.button_order);

        stock_name_1 = (TextView) findViewById(R.id.stock_name_color1);
        stock_name_2 = (TextView) findViewById(R.id.stock_name_color2);
        stock_name_3 = (TextView) findViewById(R.id.stock_name_color3);
        stock_amount_1 = (TextView) findViewById(R.id.stock_amount_color1);
        stock_amount_2 = (TextView) findViewById(R.id.stock_amount_color2);
        stock_amount_3 = (TextView) findViewById(R.id.stock_amount_color3);

        Intent intent =getIntent();
        date =intent.getStringExtra("date");
        schoolname =intent.getStringExtra("schoolname");
        Log.d("nfc","date : " + date);
        Log.d("nfc","schoolname: " + schoolname);

        DatabaseReference myDataRef = FirebaseDatabase.getInstance().getReference("Events");
        myDataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                color1 = dataSnapshot.child(date).child(schoolname).child("color1").getValue().toString();
                color2 = dataSnapshot.child(date).child(schoolname).child("color2").getValue().toString();
                color3 = dataSnapshot.child(date).child(schoolname).child("color3").getValue().toString();

                stock_name_1.setText(dataSnapshot.child(date).child(schoolname).child("color1").getValue().toString());
                stock_name_2.setText(dataSnapshot.child(date).child(schoolname).child("color2").getValue().toString());
                stock_name_3.setText(dataSnapshot.child(date).child(schoolname).child("color3").getValue().toString());

                stock1 = dataSnapshot.child(date).child(schoolname).child("stock1").getValue(int.class);
                stock2 = dataSnapshot.child(date).child(schoolname).child("stock2").getValue(int.class);
                stock3 = dataSnapshot.child(date).child(schoolname).child("stock3").getValue(int.class);

                stock_amount_1.setText(String.valueOf(stock1));
                stock_amount_2.setText(String.valueOf(stock2));
                stock_amount_3.setText(String.valueOf(stock3));

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        button_enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if((StudentID == null)||( color == null)){
                    new AlertDialog.Builder(StudentIDReaderActivity.this)
                            .setTitle("姓名與顏色！")
                            .setPositiveButton("確認", null)
                            .show();
                    return;
                }
                writeOrderToFirebase(StudentID,color);
            }
        });

        //初始化NfcAdapter
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        //初始化PendingIntent
        // 初始化PendingIntent，当有NFC设备连接上的时候，就交给当前Activity处理
        pi = PendingIntent.getActivity(this, 0, new Intent(this, getClass())
                .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        // 当前app正在前端界面运行，这个时候有intent发送过来，那么系统就会调用onNewIntent回调方法，将intent传送过来
        // 我们只需要在这里检验这个intent是否是NFC相关的intent，如果是，就调用处理方法
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())) {
            processIntent(intent);
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        mNfcAdapter.enableForegroundDispatch(this, pi, null, null);
    }

    /**
     * Parses the NDEF Message from the intent and prints to the TextView
     */
    private void processIntent(Intent intent) {
        //取出封装在intent中的TAG
        Tag tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        CardId =ByteArrayToHexString(tagFromIntent.getId());

        switch (CardId) {
            case "FFFFFFFF":
                Log.d("nfc case2, id: ",CardId);
                color = color2;
                editText_cup_color.setText(color2);
                if(stock2 == 0){
                    new AlertDialog.Builder(StudentIDReaderActivity.this)
                            .setTitle("庫存不足")
                            .setPositiveButton("確認", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    return;
                                }
                            })
                            .show();
                    editText_cup_color.setText("");
                }
                break;
            case "FFFFFFFE":
                Log.d("nfc case1, id: ",CardId);
                color = color1;
                editText_cup_color.setText(color1);
                if(stock1 == 0){
                    new AlertDialog.Builder(StudentIDReaderActivity.this)
                            .setTitle("庫存不足")
                            .setPositiveButton("確認", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    return;
                                }
                            })
                            .show();
                    editText_cup_color.setText("");
                }
                break;
            case "FFFFFFFD":
                Log.d("nfc case2, id: ",CardId);
                color = color3;
                editText_cup_color.setText(color2);
                if(stock2 == 0){
                    new AlertDialog.Builder(StudentIDReaderActivity.this)
                            .setTitle("庫存不足")
                            .setPositiveButton("確認", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    return;
                                }
                            })
                            .show();
                    editText_cup_color.setText("");
                }
                break;
            default:
                Log.d("nfc default, ","id : "+ CardId);
                StudentID = CardId;
                editText_nfc_ID.setText(StudentID);

                queryIDRef = FirebaseDatabase.getInstance().getReference();
                Query queryUIDbyEmail = queryIDRef.child("Data").child(schoolname).orderByKey().equalTo(StudentID);
                queryUIDbyEmail.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            for (DataSnapshot issue : dataSnapshot.getChildren()) {
                                new AlertDialog.Builder(StudentIDReaderActivity.this)
                                        .setTitle("已購買過")
                                        .setPositiveButton("確認", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                return;
                                            }
                                        })
                                        .show();
                                StudentID = " ";
                                editText_nfc_ID.setText("");
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                break;
        }

    }

    private void writeOrderToFirebase(String CardID, String color){
        Order order = new Order(CardID, color);
        mDatabase.child("Data").child(schoolname).child(CardID).setValue(order);

        if(color == color1){
            mDatabase.child("Events").child(date).child(schoolname).child("stock1").setValue(stock1-1);
        }else if (color == color2){
            mDatabase.child("Events").child(date).child(schoolname).child("stock2").setValue(stock2-1);
        }else if (color == color3){
            mDatabase.child("Events").child(date).child(schoolname).child("stock3").setValue(stock3-1);
        }

        editText_nfc_ID.setText("");
        editText_cup_color.setText("");
    }

    private String ByteArrayToHexString(byte[] inarray) {
        int i, j, in;
        String[] hex = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A",
                "B", "C", "D", "E", "F" };
        String out = "";


        for (j = 0; j < inarray.length; ++j) {
            in = (int) inarray[j] & 0xff;
            i = (in >> 4) & 0x0f;
            out += hex[i];
            i = in & 0x0f;
            out += hex[i];
        }
        return out;
    }


}
