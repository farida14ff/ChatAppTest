package com.example.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class PhoneActivity extends AppCompatActivity {

    EditText editPhone;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);
        editPhone = findViewById(R.id.edit_text);
        callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                Log.e("Phone", "onVerificationCompleted: ");
                signIn(phoneAuthCredential);
            }



            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Log.e("Phone", "onVerificationFailed: ");

            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                Log.e("Phone", "onCodeSent: ");

            }

            @Override
            public void onCodeAutoRetrievalTimeOut(@NonNull String s) {
                super.onCodeAutoRetrievalTimeOut(s);
                Log.e("Phone", "onCodeAutoRetrievalTimeOut: ");

            }
        };

    }


    public void onClickStart(View view){
        String phone = editPhone.getText().toString().trim();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(phone,
                60,
                TimeUnit.SECONDS,
                this,
                callbacks);
    }

    private void signIn(PhoneAuthCredential phoneAuthCredential) {
        FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential).
                addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @SuppressLint("ShowToast")
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            startActivity(new Intent(PhoneActivity.this, ProfileActivity.class));
                            finish();
                        }else {
                            Toast.makeText(PhoneActivity.this, "Ошибка при ввод", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
