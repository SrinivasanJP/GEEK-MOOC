package jp.srini.geek_mooc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.Srini.geek_mooc.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;

import Backend.BasicInfo_helper;
import Backend.SharedPreferenceStore;

public class Basic_Info extends AppCompatActivity {
    private EditText datas[];
    private int dataids[];
    private CheckBox vBaskets[];
    private RelativeLayout vSubmitBtn, dobBtn;
    private Spinner gender;
    private int ids[];
    private String name, mobile;
    private String basket[];
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseDatabase rootNode;
    private DatabaseReference referenceNode;
    private BasicInfo_helper basicInfo_helper;
    private SharedPreferences sharedPreferences;
    private String genderSelected;
    private TextView vDob,vBtnText;
    private ProgressBar vProgressbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_info);
        vBaskets = new CheckBox[9];
        ids = new int[]{R.id.programming, R.id.media, R.id.iot, R.id.science, R.id.datascience, R.id.ai,R.id.business,R.id.android, R.id.ml};
        dataids = new int[]{R.id.name,R.id.mobile,R.id.country,R.id.language,R.id.edu,R.id.website,R.id.git,R.id.other,R.id.about,R.id.upi};
        vBtnText = findViewById(R.id.BtnText);
        vProgressbar = findViewById(R.id.Progressbarbasic);

        vDob = findViewById(R.id.dob);
        gender = findViewById(R.id.gender);
        dobBtn = findViewById(R.id.dobBtn);
        String genderItem[] = new String[]{"Select Gender","Male","Female"};
        ArrayAdapter genderAdapter = new ArrayAdapter(this,R.layout.spinner_list,genderItem);
        gender.setAdapter(genderAdapter);
        gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i>0) {
                    genderSelected = genderItem[i];
                }
                else {
                    genderSelected = "others";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        Calendar calendar = Calendar.getInstance();
        dobBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(Basic_Info.this, AlertDialog.THEME_DEVICE_DEFAULT_DARK, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        vDob.setText(i2+"-"+(i1+1)+"-"+i);
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });
        datas = new EditText[10];
        vSubmitBtn = findViewById(R.id.submitBasicBtn);
        vSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vBtnText.setVisibility(View.INVISIBLE);
                vProgressbar.setVisibility(View.VISIBLE);
                firebaseAuth = FirebaseAuth.getInstance();
                firebaseUser = firebaseAuth.getCurrentUser();

                ArrayList<String> seletedBasket = new ArrayList<>();
                for (int i=0; i<9; i++){
                    vBaskets[i] = findViewById(ids[i]);
                }
                for (int i = 0; i < 9; i++) {
                    if (vBaskets[i].isChecked()) {
                        seletedBasket.add(vBaskets[i].getText().toString());
                    }
                }

                for(int i=0;i<10;i++){
                    datas[i] = findViewById(dataids[i]);
                }
                basicInfo_helper = new BasicInfo_helper();
                basicInfo_helper.setName(datas[0].getText().toString());
                basicInfo_helper.setMobile(datas[1].getText().toString());
                basicInfo_helper.setCountry(datas[2].getText().toString());
                basicInfo_helper.setLanguage(datas[3].getText().toString());
                basicInfo_helper.setEducationalBackground(datas[4].getText().toString());
                basicInfo_helper.setWebsite(datas[5].getText().toString());
                basicInfo_helper.setGit(datas[6].getText().toString());
                basicInfo_helper.setOthers(datas[7].getText().toString());
                basicInfo_helper.setAbout(datas[8].getText().toString());
                basicInfo_helper.setUpi(datas[9].getText().toString());
                basicInfo_helper.setGender(genderSelected);
                basicInfo_helper.setEmail(FirebaseAuth.getInstance().getCurrentUser().getEmail());

                rootNode = FirebaseDatabase.getInstance();
                referenceNode = rootNode.getReference("users");
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