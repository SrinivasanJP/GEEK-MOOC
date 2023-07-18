package com.example.geek_mooc;

import androidx.annotation.NonNull;
import androidx.annotation.OptIn;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.media3.common.MediaItem;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.datasource.DataSource;
import androidx.media3.datasource.DefaultHttpDataSource;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.exoplayer.smoothstreaming.SsMediaSource;
import androidx.media3.exoplayer.source.MediaSource;
import androidx.media3.exoplayer.source.ProgressiveMediaSource;
import androidx.media3.ui.PlayerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
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

import Backend.LectureHelper;
import Quiz.QuizAdapter;
import Quiz.QuizHelper;
import Quiz.QuizStart;
import Quiz.RecyclerViewQuizInterface;
import RecyclerHelper.LectureHolder;
import RecyclerHelper.RecyclerViewInterface;

public class CourseTakingPage extends AppCompatActivity implements RecyclerViewInterface, RecyclerViewQuizInterface {
    private ExoPlayer player;
    private DataSource.Factory dataSourceFactory;
    private PlayerView vCouresPlayer;

    private TextView vTitle,vLikeCount, vViewsCount, vUnRegister;
    private CheckBox vLike,vDislike;
    private RelativeLayout vDownloads, vRateBtn;
    private RecyclerView lectureList,quizList;
    private DatabaseReference reference;
    private Intent preIntent;
    private ArrayList<LectureHelper> lectureHelper;
    private ArrayList<String> quizTitle, doneQuiz;
    private ArrayList<Integer> QuizScore;
    private LectureHolder lectureHolder;
    private QuizAdapter quizAdapter;
    private Boolean first;
    private int score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_taking_page);
        preIntent = getIntent();






        //hooks
        vTitle = findViewById(R.id.title);
        vUnRegister = findViewById(R.id.unRegister);
        vLikeCount = findViewById(R.id.likeCount);
        vViewsCount = findViewById(R.id.viewsCount);
        vLike = findViewById(R.id.like);
        vDislike = findViewById(R.id.dislike);
        vDownloads = findViewById(R.id.downloadBtn);
        lectureList = findViewById(R.id.lectureList);
        quizList = findViewById(R.id.quizlist);
        vRateBtn = findViewById(R.id.btnRate);

        vRateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),GetRating.class);
                intent.putExtra("ccName", preIntent.getStringExtra("ccName"));
                intent.putExtra("ccAuthor", preIntent.getStringExtra("ccAuthor"));
                intent.putExtra("ccPath", preIntent.getStringExtra("ccPath"));
                startActivity(intent);
            }
        });
        if(preIntent.getBooleanExtra("completed",false)){
            vRateBtn.setVisibility(View.VISIBLE);
        }

        vUnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder deleteAlert = new AlertDialog.Builder(new ContextThemeWrapper(CourseTakingPage.this, R.style.myDialog));
                deleteAlert.setTitle("Unregister Course!");
                deleteAlert.setMessage("Confirm to delete course "+preIntent.getStringExtra("ccName"));
                deleteAlert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        reference = FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getUid()).child("registeredCourses").child(preIntent.getStringExtra("ccKey"));
                        reference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(getApplicationContext(), "Course Unregistered", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getApplicationContext(), Home.class));
                                }else{
                                    Toast.makeText(getApplicationContext(), "Course Not Unregistered", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
                deleteAlert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getApplicationContext(), "Unregister Cancelled", Toast.LENGTH_SHORT).show();
                    }
                });
                AlertDialog dialog = deleteAlert.create();
                dialog.show();


            }
        });


        lectureList.setHasFixedSize(true);
        lectureList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        lectureHelper = new ArrayList<>();
        lectureHolder = new LectureHolder(this, getApplicationContext(),lectureHelper);
        lectureList.setAdapter(lectureHolder);

        quizList.setHasFixedSize(true);
        quizList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        quizTitle = new ArrayList<>();
        doneQuiz = new ArrayList<>();
        QuizScore = new ArrayList<>();
        quizAdapter = new QuizAdapter(this, getApplicationContext(),quizTitle, doneQuiz, QuizScore);
        quizList.setAdapter(quizAdapter);
        reference = FirebaseDatabase.getInstance().getReference(preIntent.getStringExtra("ccPath")).child("quiz").child("quizScore").child(FirebaseAuth.getInstance().getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                doneQuiz.clear();
                for(DataSnapshot done: snapshot.getChildren()){
                    doneQuiz.add(done.getKey());
                    QuizScore.add(done.getValue(Integer.class));
                }
                quizAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        reference = FirebaseDatabase.getInstance().getReference(preIntent.getStringExtra("ccPath")).child("quiz").child("quizdata");
        reference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                quizTitle.clear();
                for(DataSnapshot title: snapshot.getChildren()){
                    quizTitle.add(title.getKey());
                }
                quizAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        first = true;


        //data insert
        reference = FirebaseDatabase.getInstance().getReference("Courses");
        reference.addValueEventListener(new ValueEventListener() {

            @SuppressLint("RestrictedApi")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                lectureHelper.clear();
                    for (DataSnapshot users : snapshot.getChildren()) {
                        for (DataSnapshot course : users.getChildren()) {
                            if (preIntent.getStringExtra("ccKey").equals(course.getKey())) {
                                for (DataSnapshot lectures : course.child("lectures").getChildren()) {
                                    LectureHelper l = lectures.getValue(LectureHelper.class);
                                    l.setlPath(lectures.getRef().getPath().toString());
                                    lectureHelper.add(l);
                                }
                            }
                        }
                    }
                    lectureHolder.notifyDataSetChanged();
                    if(first) {
                        auxSetters(0);
                        first = false;
                    }
                }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

    @Override
    public void onCourseClick(int position) {
        auxSetters(position);
    }

    @Override
    public void onClickNotesBtn(int position) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(lectureHelper.get(position).getNotesLink()));
        startActivity(i);
    }

    @Override
    public void onCompletedCourseClick(int position) {

    }

    public void auxSetters(int position){
        if(lectureHelper.size()==0){
            vTitle.setText("No Lectures Available yet");
        }else {
            reference = FirebaseDatabase.getInstance().getReference(lectureHelper.get(position).getlPath())
                    .child("viewsCount");
            reference.setValue(lectureHelper.get(position).getViewsCount() + 1);
            vCouresPlayer = findViewById(R.id.coursePlayer);
            //ssMediaSource Generation
            dataSourceFactory = new DefaultHttpDataSource.Factory();
            player = new ExoPlayer.Builder(getApplicationContext()).build();
            player.setMediaItem(MediaItem.fromUri(Uri.parse(lectureHelper.get(position).getVideoLink())));
            vCouresPlayer.setPlayer(player);
            player.prepare();
            LectureHelper selectedLecture = lectureHelper.get(position);
            vTitle.setText(selectedLecture.getTitle());
            vLikeCount.setText(selectedLecture.getLikeCount() + "");
            vViewsCount.setText("views "+selectedLecture.getViewsCount() );
            if(lectureHelper.get(position).getFinal()){
                reference = FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getUid()).child("completedCourses");
                reference.child(preIntent.getStringExtra("ccKey")).setValue(true).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(CourseTakingPage.this, "Successfully completed this course", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(CourseTakingPage.this, "Course completed not registered", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            vLike.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference(lectureHelper.get(position).getlPath())
                            .child("likeCount");
                    if (b) {
                        reference1.setValue(lectureHelper.get(position).getLikeCount() + 1);
                    } else {
                        reference1.setValue(lectureHelper.get(position).getLikeCount() - 1);
                    }

                }
            });
            vDislike.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference(lectureHelper.get(position).getlPath())
                            .child("dislikeCount");
                    if (b) {
                        reference1.setValue(lectureHelper.get(position).getDislikeCount() + 1);
                    } else {
                        reference1.setValue(lectureHelper.get(position).getDislikeCount() - 1);
                    }
                }
            });
            vDownloads.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(lectureHelper.get(position).getVideoLink()));
                    startActivity(i);
                }
            });
        }
    }

    @Override
    public void onQuizClick(int position) {
            Intent intent = new Intent(getApplicationContext(), QuizStart.class);
            intent.putExtra("ccPath",preIntent.getStringExtra("ccPath"));
            intent.putExtra("ccTitle", preIntent.getStringExtra("ccName"));
            intent.putExtra("quizTitle",quizTitle.get(position));
            AlertDialog.Builder builder = new AlertDialog.Builder(CourseTakingPage.this);
            builder.setTitle("Confirm to start you quiz");
            builder.setMessage("Instructions:\n1. For every questions you can spare 30s of time slice.\n2.You can't come back to previous question\n\nAre you ready to start?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    startActivity(intent);
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Toast.makeText(CourseTakingPage.this, "You can always navigate to this place to take your quiz", Toast.LENGTH_SHORT).show();
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
    }
}