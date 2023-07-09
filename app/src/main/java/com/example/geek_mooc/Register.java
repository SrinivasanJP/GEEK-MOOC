package com.example.geek_mooc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Register extends AppCompatActivity {
    private EditText vUserName, vPassword;
    private ProgressBar vProgressBar;
    private RelativeLayout vSingUpBtn;
    private TextView vMvSignin;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        // set progressbar invisible
        vProgressBar = findViewById(R.id.ProgressbarSignin);
        vProgressBar.setVisibility(ProgressBar.INVISIBLE);
        // axilary jobs
        vMvSignin = findViewById(R.id.backtologin);
        vMvSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Login.class));
                finish();
            }
        });

        //main register
        vSingUpBtn = findViewById(R.id.btnSignIN);
        vSingUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vUserName = findViewById(R.id.signUPEmailID);
                vPassword = findViewById(R.id.SignUPPassword);
                String username = vUserName.getText().toString().trim();
                String password = vPassword.getText().toString().trim();
                if(username.isEmpty()){
                    Toast.makeText(Register.this, "Enter MailID", Toast.LENGTH_SHORT).show();
                    vUserName.setError("Enter Username");
                }else if(password.isEmpty()) {
                    Toast.makeText(Register.this, "Enter Password", Toast.LENGTH_SHORT).show();
                    vPassword.setError("Password Required");
                }else if(password.length()<7){
                    Toast.makeText(Register.this, "Password length must be more then 7 character", Toast.LENGTH_SHORT).show();
                    vPassword.setError("Week Password");
                }else{
                    vProgressBar.setVisibility(View.VISIBLE);
                    firebaseAuth = FirebaseAuth.getInstance();
                    firebaseAuth.createUserWithEmailAndPassword(username,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {
                                vProgressBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(Register.this, "Check your mail to verify your account", Toast.LENGTH_SHORT).show();
                                firebaseAuth.getCurrentUser().sendEmailVerification();
                                startActivity(new Intent(getApplicationContext(), Login.class));
                                finish();

                            }else if(task.isCanceled()){
                                task.addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(Register.this, e.toString(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }else{
                                vProgressBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(Register.this, "Failed to register", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

    }
}