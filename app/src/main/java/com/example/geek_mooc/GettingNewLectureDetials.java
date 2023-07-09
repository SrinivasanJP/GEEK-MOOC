package com.example.geek_mooc;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import Backend.LectureHelper;

public class GettingNewLectureDetials extends AppCompatActivity {
    private EditText vTitle;
    private RelativeLayout vVideo,vNotes, btnAddLecture;
    private CheckBox vFinal;
    private TextView btnText;
    private ProgressBar btnProgress;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private Uri videoUri,pdfUri;
    private UploadTask uploadTask;
    private String vLink,title,nLink;
    private Intent i;
    private Boolean isFinal;
    private LectureHelper lectureHelper;
    private long lectureCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getting_new_lecture_detials);
        vTitle = findViewById(R.id.titleInput);
        vVideo = findViewById(R.id.addVideo);
        vNotes = findViewById(R.id.addNotes);
        vFinal = findViewById(R.id.finalCheck);
        btnAddLecture = findViewById(R.id.btnSubmit);
        btnText = findViewById(R.id.BtnText);
        btnProgress = findViewById(R.id.ProgressbarBtn);
        i = getIntent();
        databaseReference = FirebaseDatabase.getInstance().getReference("Courses").child(FirebaseAuth.getInstance().getUid()).child(i.getStringExtra("ccKey")).child("lecture");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                lectureCount = snapshot.getChildrenCount();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        vVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!vTitle.getText().toString().equals(null)) {
                    btnProgress.setVisibility(View.VISIBLE);
                    btnText.setVisibility(View.INVISIBLE);
                    storageReference = FirebaseStorage.getInstance().getReference("Courses").child(FirebaseAuth.getInstance().getUid()).child(i.getStringExtra("ccKey")).child("lecture")
                            .child(lectureCount+"_"+vTitle.getText().toString().replace(" ","_"));
                    chooseVideo();
                    Log.d("log", "choose video is run");

                }else{
                    vTitle.setError("Please fill Title first");
                }
            }
        });
        vNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!vTitle.getText().toString().equals(null)) {
                    btnProgress.setVisibility(View.VISIBLE);
                    btnText.setVisibility(View.INVISIBLE);
                    storageReference = FirebaseStorage.getInstance().getReference("Courses").child(FirebaseAuth.getInstance().getUid()).child(i.getStringExtra("ccKey")).child("notes")
                            .child(lectureCount+"_"+vTitle.getText().toString().replace(" ","_"));
                    chooseNotes();

                }else{
                    vTitle.setError("Please fill Title first");
                }
            }
        });
        btnAddLecture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnText.setVisibility(View.INVISIBLE);
                btnProgress.setVisibility(View.VISIBLE);
                lectureHelper = new LectureHelper();
                lectureHelper.setTitle(vTitle.getText().toString());
                lectureHelper.setNotesLink(nLink);
                lectureHelper.setVideoLink(vLink);
                lectureHelper.setFinal(vFinal.isChecked());
                databaseReference = FirebaseDatabase.getInstance().getReference("Courses").child(FirebaseAuth.getInstance().getUid()).child(i.getStringExtra("ccKey")).child("lectures").child(lectureCount+"_"+lectureHelper.getTitle().replace(" ","_"));
                databaseReference.setValue(lectureHelper).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getApplicationContext(), "Lecture Created", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), Home.class));
                            finish();
                        }else{
                            btnProgress.setVisibility(View.INVISIBLE);
                            btnText.setVisibility(View.VISIBLE);
                            Toast.makeText(getApplicationContext(), "Lecture Creation failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 || resultCode == RESULT_OK || data!=null || data.getData().equals(null)){
            videoUri = data.getData();
            uploadVideo();
        }

        if(requestCode == 2 || resultCode == RESULT_OK || data!=null || data.getData() != null){
            pdfUri = data.getData();
            uploadNotes();
        }
    }
    private void chooseVideo(){
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }
    private void chooseNotes(){
        Intent intent = new Intent();
        intent.setType("*/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 2);
    }
    private Void uploadVideo(){
        if(videoUri != null){
            btnProgress.setVisibility(View.VISIBLE);
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
                        vLink = task.getResult().toString();
                        btnProgress.setVisibility(View.INVISIBLE);

                        Toast.makeText(getApplicationContext(), "Video Uploaded", Toast.LENGTH_SHORT).show();

                    }else{
                        Toast.makeText(getApplicationContext(), "Video Upload failed", Toast.LENGTH_SHORT).show();
                    }
                    btnProgress.setVisibility(View.INVISIBLE);
                    btnText.setVisibility(View.VISIBLE);
                }
            });
        }else{
            Toast.makeText(getApplicationContext(), "Choose video first", Toast.LENGTH_SHORT).show();
        }
        return null;
    }
    private Void uploadNotes(){
        if(videoUri != null){
            btnProgress.setVisibility(View.VISIBLE);
            storageReference.child(System.currentTimeMillis()+"."+getExt(pdfUri));
            uploadTask = storageReference.putFile(pdfUri);
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
                        nLink = task.getResult().toString();
                        btnProgress.setVisibility(View.INVISIBLE);

                        Toast.makeText(getApplicationContext(), "Notes Uploaded", Toast.LENGTH_SHORT).show();

                    }else{
                        Toast.makeText(getApplicationContext(), "Notes Upload failed", Toast.LENGTH_SHORT).show();
                    }
                    btnProgress.setVisibility(View.INVISIBLE);
                    btnText.setVisibility(View.VISIBLE);
                }
            });
        }else{
            Toast.makeText(getApplicationContext(), "Choose video first", Toast.LENGTH_SHORT).show();
        }
        return null;
    }
    private String getExt(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
}