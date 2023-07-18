package com.example.geek_mooc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import Backend.SharedPreferenceStore;

public class Login extends AppCompatActivity {
    private EditText vUserName, vPassword;
    private TextView vregisterBtn, vForgetPasswordBtn, vLoginText;
    private ProgressBar vProgressbar;
    private RelativeLayout vLoginBtn;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private SharedPreferences sharedPreferences;
    private FirebaseDatabase rootNode;
    private DatabaseReference referenceNode;
    private String name, mobile;
    //Login not for logged in user
    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser!= null && firebaseUser.isEmailVerified()){
            startActivity(new Intent(getApplicationContext(), Home.class));
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //preliminary activities
        vProgressbar = findViewById(R.id.ProgressbarLogin);
        vProgressbar.setVisibility(ProgressBar.INVISIBLE);

        vregisterBtn = findViewById(R.id.mvRegister);
        vregisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Register.class));
            }
        });

        vForgetPasswordBtn = findViewById(R.id.mvForgetPassword);
        vForgetPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),ForgetPassword.class));
            }
        });

        //Login
        vLoginBtn = findViewById(R.id.loginContainer);
        vLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vLoginText = findViewById(R.id.BtnLoginText);
                //hooks
                vUserName = findViewById(R.id.username);
                vPassword = findViewById(R.id.password);

                String username = vUserName.getText().toString().trim();
                String password = vPassword.getText().toString().trim();

                if(username.isEmpty()){
                    vUserName.setError("Mail id is required");
                }else if(password.isEmpty()){
                    vPassword.setError("Password is required");
                }else if(password.length()<7){
                    vPassword.setError("Password should be more then 7 character long");
                }else {
                    vProgressbar.setVisibility(View.VISIBLE);
                    vLoginText.setVisibility(View.INVISIBLE);

                    firebaseAuth.signInWithEmailAndPassword(username,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                firebaseUser = firebaseAuth.getCurrentUser();
                                if(!firebaseUser.isEmailVerified()){
                                    vProgressbar.setVisibility(View.INVISIBLE);
                                    vLoginText.setVisibility(View.VISIBLE);
                                    Toast.makeText(Login.this, "Please verify your email", Toast.LENGTH_SHORT).show();
                                }else {
                                    sharedPreferences = getSharedPreferences(SharedPreferenceStore.SHARED_PREFERENCE_NAME, MODE_PRIVATE);
                                    rootNode = FirebaseDatabase.getInstance();
                                    referenceNode = rootNode.getReference("users");
                                    referenceNode.child(firebaseUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                                            if (task.isSuccessful()) {
                                                DataSnapshot dataSnapshot = task.getResult();
                                                name = String.valueOf(dataSnapshot.child("name").getValue());
                                                mobile = String.valueOf(dataSnapshot.child("mobile").getValue());
                                                sharedPreferences = getSharedPreferences(SharedPreferenceStore.SHARED_PREFERENCE_NAME, MODE_PRIVATE);
                                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                                editor.putString(SharedPreferenceStore.KEY_NAME, name).apply();
                                                editor.putString(SharedPreferenceStore.KEY_MOBILE, mobile).apply();
                                                if (!task.getResult().exists()) {

                                                    startActivity(new Intent(getApplicationContext(), Basic_Info.class));
                                                } else {

                                                    startActivity(new Intent(getApplicationContext(), Home.class));
                                                    finish();
                                                }
                                            } else {
                                                Toast.makeText(getApplicationContext(), "Failed to read", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                                }
                            }else{
                                vProgressbar.setVisibility(View.INVISIBLE);
                                vLoginText.setVisibility(View.VISIBLE);
                                Toast.makeText(Login.this, "Login failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }


            }
        });




    }
}