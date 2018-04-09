package com.unick.studentid_checker;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EventListByDateActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private ListView listView;
    private String schoolname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list_by_date);
        Intent intent =getIntent();
        final String date =intent.getStringExtra("date");


        //firebase auth check
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                } else {
                    // User is signed out
                    Intent intent = new Intent();
                    intent.setClass(EventListByDateActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        };
        listView = findViewById(R.id.user_list);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_expandable_list_item_1,android.R.id.text1);
        listView.setAdapter(adapter);
        String path = "Events/"+date;
        Log.d("UserListActivity","path: "+path);
        final DatabaseReference myUserRef = FirebaseDatabase.getInstance().getReference(path);
        myUserRef.limitToLast(10).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("UserListActivity","onDataChange");
                //Log.d("UserListActivity","users: "+dataSnapshot.getKey());
                for(DataSnapshot ds : dataSnapshot.getChildren() ){
                    adapter.add(ds.getKey());
                    Log.d("UserListActivity","users:"+ds.getKey());
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String UID = adapterView.getItemAtPosition(i).toString();
                String path = "Events/" + date + "/" + UID;
                DatabaseReference nameRef =FirebaseDatabase.getInstance().getReference(path);
                nameRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        schoolname = dataSnapshot.child("schoolname").getValue().toString();
                        Intent intent = new Intent();
                        intent.setClass(EventListByDateActivity.this, StudentIDReaderActivity.class);
                        intent.putExtra("date",date);
                        intent.putExtra("schoolname",schoolname);
                        startActivity(intent);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
}
