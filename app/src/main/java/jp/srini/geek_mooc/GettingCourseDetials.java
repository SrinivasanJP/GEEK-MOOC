package jp.srini.geek_mooc;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.Srini.geek_mooc.R;;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Calendar;

import Backend.CreateCourseHelper;
import Backend.SharedPreferenceStore;

public class GettingCourseDetials extends AppCompatActivity {

    private EditText vTitle, vDescription, vLanguage;
    private TextView vBtnText;
    private ProgressBar vBtnProgress, vUploadProgress;
    private Uri videoUri;
    private RelativeLayout vCreateBtn, vAddVideoBtn;
    private MediaController mediaController;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private CreateCourseHelper createCourseHelper;
    private SharedPreferences sharedPreference;
    private Spinner basket;
    private String basketitems[];

    UploadTask uploadTask;
    String link, basketSelected;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getting_course_detials);
        createCourseHelper = new CreateCourseHelper();




        vTitle = findViewById(R.id.titleInput);
        vDescription = findViewById(R.id.descriptionInput);
        vLanguage = findViewById(R.id.languageInput);
        vCreateBtn = findViewById(R.id.btnSubmit);
        vBtnText = findViewById(R.id.BtnText);
        vBtnProgress = findViewById(R.id.ProgressbarBtn);
        vUploadProgress = findViewById(R.id.progressUpload);
        vAddVideoBtn = findViewById(R.id.addVideo);
        basket = findViewById(R.id.basket);



        basketitems = new String[]{"programming", "computers", "trading", "iot", "science", "astronomy"};

        ArrayAdapter<String> baskerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, basketitems);
        basket.setAdapter(baskerAdapter);
        basket.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                basketSelected = basketitems[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                basketSelected = "general";
            }
        });


        vUploadProgress.setVisibility(View.INVISIBLE);
        vAddVideoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!vTitle.getText().toString().isEmpty()) {
                    vBtnProgress.setVisibility(View.VISIBLE);
                    vBtnText.setVisibility(View.INVISIBLE);
                    storageReference = FirebaseStorage.getInstance().getReference("Courses").child(FirebaseAuth.getInstance().getUid()).child(vTitle.getText().toString().replace(" ", "_"));
                    chooseVideo();
                    Log.d("log", "choose video is run");

                }else{
                    vTitle.setError("Please fill Title first");
                }
            }
        });
        vCreateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vBtnText.setVisibility(View.INVISIBLE);
                vBtnProgress.setVisibility(View.VISIBLE);
                sharedPreference =  getSharedPreferences(SharedPreferenceStore.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
                createCourseHelper.setTitle(vTitle.getText().toString().trim());
                createCourseHelper.setDescription(vDescription.getText().toString().trim());
                createCourseHelper.setLanguage(vLanguage.getText().toString().trim());
                createCourseHelper.setIntrolink(link);
                createCourseHelper.setBasket(basketSelected);
                createCourseHelper.setKey(vTitle.getText().toString().replace(" ","_")+FirebaseAuth.getInstance().getUid().substring(0,6));
                createCourseHelper.setAuthor(sharedPreference.getString(SharedPreferenceStore.KEY_NAME,null));
                Log.d("UT_authorname", "onClick: "+sharedPreference.getString(SharedPreferenceStore.KEY_NAME,SharedPreferenceStore.KEY_NAME));
                Calendar calendar;
                calendar = Calendar.getInstance();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                String date = simpleDateFormat.format(calendar.getTime()).toString();
                createCourseHelper.setLastUpdate(date);
                databaseReference = FirebaseDatabase.getInstance().getReference("Courses")
                        .child(FirebaseAuth.getInstance().getUid())
                        .child(vTitle.getText().toString().replace(" ","_")+FirebaseAuth.getInstance().getUid().substring(0,6));
                databaseReference.setValue(createCourseHelper).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(GettingCourseDetials.this, "Course Created", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), Home.class));
                            finish();
                        }else{
                            vBtnProgress.setVisibility(View.INVISIBLE);
                            vBtnText.setVisibility(View.VISIBLE);
                            Toast.makeText(GettingCourseDetials.this, "Course Creation failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 || resultCode == RESULT_OK || data!=null || data.getData() != null){
            videoUri = data.getData();
            uploadVideo();

        }
    }

    private void chooseVideo(){
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }
    private String getExt(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
    private Void uploadVideo(){
        final Uri[] downloadUrl = new Uri[1];
        if(videoUri != null){
            vUploadProgress.setVisibility(View.VISIBLE);
            storageReference.child(System.currentTimeMillis()+"."+getExt(videoUri));
            uploadTask = storageReference.putFile(videoUri);
            Task<Uri> urltask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if(!task.isSuccessful()){
                        throw task.getException();
                    }
                    return storageReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()) {
                        downloadUrl[0] = task.getResult();
                        link = task.getResult().toString();
                        vUploadProgress.setVisibility(View.INVISIBLE);

                        Toast.makeText(GettingCourseDetials.this, "Video Uploaded", Toast.LENGTH_SHORT).show();

                    }else{
                        Toast.makeText(GettingCourseDetials.this, "Video Upload failed", Toast.LENGTH_SHORT).show();
                    }
                    vBtnProgress.setVisibility(View.INVISIBLE);
                    vBtnText.setVisibility(View.VISIBLE);
                }
            });
        }else{
            Toast.makeText(GettingCourseDetials.this, "Choose video first", Toast.LENGTH_SHORT).show();
        }
        return null;
    }

}