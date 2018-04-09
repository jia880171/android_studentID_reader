package com.unick.studentid_checker;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.unick.studentid_checker.models.User;

import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {

    private String userUID;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private String name;
    private String birthday;
    private String email;
    private String password;
    private String personalID;
    private String carID;
    private String carID_2;
    private String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onResume() {
        Log.d("life cycle","onResume");
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.d("life cycle","onPause");
        super.onPause();
    }

    public void createUser(View v){
        name = ((EditText)findViewById(R.id.textView_name)).getText().toString();
        email = ((EditText)findViewById(R.id.textView_email)).getText().toString();
        password = ((EditText)findViewById(R.id.textView_password)).getText().toString();

        if(name.matches("")){
            new AlertDialog.Builder(SignUpActivity.this)
                    .setTitle("姓名不可為空！")
                    .setPositiveButton("確認", null)
                    .show();
            return;
        }else if(isValidEmail(email)==false){
            new AlertDialog.Builder(SignUpActivity.this)
                    .setTitle("email格式錯誤！！！")
                    .setPositiveButton("確認", null)
                    .show();
            return;
        } else if (email.matches("") || password.matches("")) {
            Log.d("in SignUpActivity","email: "+email+" password: "+password);
            new AlertDialog.Builder(SignUpActivity.this)
                    .setTitle("帳號密碼不可為空！")
                    .setPositiveButton("確認", null)
                    .show();
            return;
        }else if(password.length()<6){
            new AlertDialog.Builder(SignUpActivity.this)
                    .setTitle("密碼長度不可小於6")
                    .setPositiveButton("確認", null)
                    .show();
            return;
        }
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(
                        new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                String message = task.isSuccessful() ? "註冊成功" : "註冊失敗";
                                if(task.isSuccessful()){
                                    Log.d("in SignUpActivity","sign up success");

                                    userUID = task.getResult().getUser().getUid();
                                    Log.d("in SignUpActivity","UserId: "+userUID);
                                    //String name = usernameFromEmail(email);
                                    writeNewUser(userUID);
                                    Intent intent = new Intent();
                                    intent.setClass(SignUpActivity.this, MainActivity.class);
                                    startActivity(intent);
                                }else{
                                    String userCreateFailedMessage=" ";
                                    if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                        userCreateFailedMessage="該信箱已被使用";
                                    }
                                    Log.d("failed to create:", "reason: "+task.getException() );
                                    new AlertDialog.Builder(SignUpActivity.this)
                                            .setTitle(message)
                                            .setMessage(userCreateFailedMessage)
                                            .setPositiveButton("確認", null)
                                            .show();
                                    return;//執行權還給呼叫方不繼續往下執行
                                }
                            }
                        });
    }

    public static boolean isValidEmail(String email) {
        boolean result = false;
        Pattern EMAIL_PATTERN = Pattern
                .compile("^\\w+\\.*\\w+@(\\w+\\.){1,5}[a-zA-Z]{2,3}$");
        if (EMAIL_PATTERN.matcher(email).matches()) {
            result = true;
        }
        return result;
    }

    private void writeNewUser(String userId) {
        User user = new User(name, email);
        mDatabase.child("users").child(userId).setValue(user);
    }
}
