package com.example.geek_mooc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.media3.common.MediaItem;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.PlayerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
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

import java.util.ArrayList;

import Backend.CreateCourseHelper;
import Backend.ratings;
import RecyclerHelper.RatingsAdapter;

public class RegisterCourse extends AppCompatActivity {
    private TextView vTitle, vDes, vRatings, vNoOfRatings, vStudents, vAuthor, vLastUpdated, vLanguage, vRegisterBtnText;
    private ProgressBar vRegisterProgressbar;
    private RelativeLayout vWatchListBtn, vRegisterBtn;
    private PlayerView vPreviewPlayer;
    private ExoPlayer player;
    private MediaItem mediaItem;
    private DatabaseReference reference;
    Boolean current ;
    private Intent intent;
    private RecyclerView recyclerView;
    private ArrayList<ratings> ratingshelper;
    private RatingsAdapter ratingsAdapter;
    int ratingsCount, noOfratings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_course);
        intent = getIntent();

        ratingsCount=0;
        //hooks
        vTitle = findViewById(R.id.title);
        vDes = findViewById(R.id.description);
        vRatings = findViewById(R.id.rating);
        vNoOfRatings = findViewById(R.id.noOfRatings);
        vAuthor = findViewById(R.id.author);
        vStudents = findViewById(R.id.noOfStudents);
        vLastUpdated = findViewById(R.id.updated);
        vLanguage = findViewById(R.id.lang);
        vWatchListBtn = findViewById(R.id.watchListContainer);
        vRegisterBtn = findViewById(R.id.registerContainer);
        vPreviewPlayer = findViewById(R.id.previewPlayer);
        vRegisterProgressbar = findViewById(R.id.ProgressbarRegister);
        vRegisterBtnText = findViewById(R.id.BtnRegister);
        recyclerView = findViewById(R.id.showComments);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        ratingshelper = new ArrayList<>();
        ratingsAdapter = new RatingsAdapter(getApplicationContext(),ratingshelper);
        recyclerView.setAdapter(ratingsAdapter);

        reference = FirebaseDatabase.getInstance().getReference(intent.getStringExtra("ccPath")).child("commentsRatings");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                noOfratings = (int)snapshot.getChildrenCount();
                ratingshelper.clear();
                for(DataSnapshot users: snapshot.getChildren()){
                    ratings rate = users.getValue(ratings.class);
                    ratingsCount+=rate.getRating();
                    rate.setId(users.getKey());
                    ratingshelper.add(rate);
                }
                getName();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        //filling placeholders
        vTitle.setText(intent.getStringExtra("ccName"));
        vDes.setText(intent.getStringExtra("ccDes"));
        vLanguage.setText("Language: "+intent.getStringExtra("ccLang"));
        vStudents.setText("No of Students: "+intent.getStringExtra("ccRegistrations"));
        vAuthor.setText("Created by: "+intent.getStringExtra("ccAuthor"));
        vLastUpdated.setText(intent.getStringExtra("ccUpdate"));


        //setting up preview player
        player = new ExoPlayer.Builder(this).build();
        mediaItem = MediaItem.fromUri(Uri.parse(intent.getStringExtra("ccIntro")));
        vPreviewPlayer.setPlayer(player);
        player.setMediaItem(mediaItem);
        player.prepare();
        reference = FirebaseDatabase.getInstance().getReference("users");


        vWatchListBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                final Boolean[] flag = {true};
                reference = FirebaseDatabase.getInstance().getReference("users");
                reference.child(FirebaseAuth.getInstance().getUid()).child("watchListCourses").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(flag[0]) {
                            flag[0] = false;
                            if (snapshot.hasChild(intent.getStringExtra("ccKey"))) {
                                current = true;
                            } else {
                                current = false;
                            }
                            setwatchlist();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                vWatchListBtn.setBackground(getDrawable(R.drawable.curve_box));
            }
        });
        vRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vRegisterProgressbar.setVisibility(View.VISIBLE);
                vRegisterBtnText.setVisibility(View.INVISIBLE);

                DatabaseReference cReference = FirebaseDatabase.getInstance().getReference(intent.getStringExtra("ccPath"));
                cReference.child("registrations").setValue(Integer.parseInt(intent.getStringExtra("ccRegistrations"))+1);
                cReference.child("users").child(FirebaseAuth.getInstance().getUid()).setValue(true);


                reference = FirebaseDatabase.getInstance().getReference("users");
                reference.child(FirebaseAuth.getInstance().getUid()).child("registeredCourses").child(intent.getStringExtra("ccKey")).setValue(true).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(RegisterCourse.this, "Course Registered", Toast.LENGTH_SHORT).show();
                            finish();
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
    private void setwatchlist(){
        reference = FirebaseDatabase.getInstance().getReference("users");
        if(current){
            reference.child(FirebaseAuth.getInstance().getUid()).child("watchListCourses").child(intent.getStringExtra("ccKey")).removeValue();
        }else{
            reference.child(FirebaseAuth.getInstance().getUid()).child("watchListCourses").child(intent.getStringExtra("ccKey")).setValue(true);
        }
    }
    private void getName(){
        if(noOfratings>0) {
            vRatings.setText("Rating: " + ratingsCount / noOfratings);
            vNoOfRatings.setText("No of Ratings:" + noOfratings);
        }else{
            vRatings.setText("Rating: 0");
            vNoOfRatings.setText("No of Ratings: 0");
        }
        for(int i=0;i< ratingshelper.size();i++){
            reference = FirebaseDatabase.getInstance().getReference("users").child(ratingshelper.get(i).getId()).child("name");
            int finalI = i;
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    ratingshelper.get(finalI).setName(snapshot.getValue(String.class));
                    ratingsAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }

            });

        }

    }
}