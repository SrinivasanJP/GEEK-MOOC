package com.example.geek_mooc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.media3.common.MediaItem;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.PlayerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegisterCourse extends AppCompatActivity {
    private TextView vTitle, vDes, vRatings, vNoOfRatings, vStudents, vAuthor, vLastUpdated, vLanguage, vRegisterBtnText;
    private ProgressBar vRegisterProgressbar;
    private RelativeLayout vWatchListBtn, vRegisterBtn;
    private PlayerView vPreviewPlayer;
    private ExoPlayer player;
    private MediaItem mediaItem;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_course);
        Intent intent = getIntent();

        //hooks
        vTitle = findViewById(R.id.title);
        vDes = findViewById(R.id.description);
        vRatings = findViewById(R.id.rating);
        vNoOfRatings = findViewById(R.id.noOfRatings);
        vAuthor = findViewById(R.id.createdBy);
        vStudents = findViewById(R.id.noOfStudents);
        vLastUpdated = findViewById(R.id.updated);
        vLanguage = findViewById(R.id.lang);
        vWatchListBtn = findViewById(R.id.watchListContainer);
        vRegisterBtn = findViewById(R.id.registerContainer);
        vPreviewPlayer = findViewById(R.id.previewPlayer);
        vRegisterProgressbar = findViewById(R.id.ProgressbarRegister);
        vRegisterBtnText = findViewById(R.id.BtnRegister);
        vWatchListBtn = findViewById(R.id.watchListContainer);

        //filling placeholders
        vTitle.setText(intent.getStringExtra("ccName"));
        vDes.setText(intent.getStringExtra("ccDes"));
        vLanguage.setText(intent.getStringExtra("ccLang"));
        vRatings.setText("Ratings: "+intent.getStringExtra("ccRating"));
        vStudents.setText("No of Students: "+intent.getStringExtra("ccRegistrations"));
        vAuthor.setText("Created by: "+intent.getStringExtra("ccAuthor"));
        vNoOfRatings.setText("("+intent.getStringExtra("ccNoofRatings")+")");
        vLastUpdated.setText(intent.getStringExtra("ccUpdate"));


        //setting up preview player
        player = new ExoPlayer.Builder(this).build();
        mediaItem = MediaItem.fromUri(Uri.parse(intent.getStringExtra("ccIntro")));
        vPreviewPlayer.setPlayer(player);
        player.setMediaItem(mediaItem);
        player.prepare();
        reference = FirebaseDatabase.getInstance().getReference("users");


        vWatchListBtn.setOnClickListener(new View.OnClickListener() {
            Boolean current ;
            @Override
            public void onClick(View view) {

                reference.child(FirebaseAuth.getInstance().getUid()).child("watchListCourses").child(intent.getStringExtra("ccKey")).setValue(true);
                vWatchListBtn.setBackground(getDrawable(R.drawable.curve_box));
            }
        });
        vRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vRegisterProgressbar.setVisibility(View.VISIBLE);
                vRegisterBtnText.setVisibility(View.INVISIBLE);

                reference.child(FirebaseAuth.getInstance().getUid()).child("registeredCourses").child(intent.getStringExtra("ccKey")).setValue(true).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(RegisterCourse.this, "Course Registered", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), Home.class));
                        }else{
                            Toast.makeText(RegisterCourse.this, "Course Registeration Failed", Toast.LENGTH_SHORT).show();
                            vRegisterProgressbar.setVisibility(View.INVISIBLE);
                            vRegisterBtnText.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }
        });


    }
}