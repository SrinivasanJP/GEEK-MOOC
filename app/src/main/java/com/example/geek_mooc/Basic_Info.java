package com.example.geek_mooc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.sql.Array;
import java.util.ArrayList;

import Backend.BasicInfo_helper;
import Backend.SharedPreferenceStore;

public class Basic_Info extends AppCompatActivity {
    private EditText vName, vMobile;
    private CheckBox vBaskets[];
    private RelativeLayout vSubmitBtn;
    private int ids[];
    private String name, mobile;
    private String basket[];
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseDatabase rootNode;
    private DatabaseReference referenceNode;
    private BasicInfo_helper basicInfo_helper;
    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_info);
        vBaskets = new CheckBox[9];
        ids = new int[]{R.id.programming, R.id.media, R.id.iot, R.id.science, R.id.datascience, R.id.ai,R.id.business,R.id.android, R.id.ml};

        vSubmitBtn = findViewById(R.id.submitBasicBtn);
        vSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth = FirebaseAuth.getInstance();
                firebaseUser = firebaseAuth.getCurrentUser();
                rootNode = FirebaseDatabase.getInstance();
                referenceNode = rootNode.getReference("users");
                ArrayList<String> seletedBasket = new ArrayList<>();
                for (int i=0; i<9; i++){
                    vBaskets[i] = findViewById(ids[i]);
                }
                for (int i = 0; i < 9; i++) {
                    if (vBaskets[i].isChecked()) {
                        seletedBasket.add(vBaskets[i].getText().toString());
                    }
                }
                vName = findViewById(R.id.name);
                vMobile = findViewById(R.id.mobile);
                name = vName.getText().toString().trim();
                mobile = vMobile.getText().toString().trim();
                basicInfo_helper = new BasicInfo_helper(name, mobile, seletedBasket);

                referenceNode.child(firebaseUser.getUid()).setValue(basicInfo_helper).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(Basic_Info.this, "We wiil remember you", Toast.LENGTH_SHORT).show();
                            sharedPreferences = getSharedPreferences(SharedPreferenceStore.SHARED_PREFERENCE_NAME, MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(SharedPreferenceStore.KEY_NAME, name).apply();
                            editor.putString(SharedPreferenceStore.KEY_MOBILE, mobile).apply();
                            startActivity(new Intent(getApplicationContext(), Home.class));
                            finish();
                        }else{
                            Toast.makeText(Basic_Info.this, "Data entry failed checkout later", Toast.LENGTH_SHORT).show();
                            FirebaseAuth.getInstance().signOut();
                            startActivity(new Intent(getApplicationContext(), Login.class));
                            finish();
                        }
                    }
                });

            }
        });









    }
}