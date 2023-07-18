package com.example.geek_mooc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import Backend.CreateCourseHelper;
import Backend.LectureHelper;
import Quiz.QuizCreate;
import Quiz.QuizDeleteAdapter;
import Quiz.QuizHelper;
import Quiz.RecyclerViewQuizDeleteInterface;
import RecyclerHelper.CourseHolder;
import RecyclerHelper.ModifyLectureAdapter;
import RecyclerHelper.RecyclerModifyInterface;
import RecyclerHelper.RecyclerViewInterface;

public class LectureUpload extends AppCompatActivity implements RecyclerModifyInterface, RecyclerViewQuizDeleteInterface {
    private TextView vTitle;
    private RelativeLayout relativeLayout, createQuiz;
    private RecyclerView recyclerView, quizList;
    private ArrayList<LectureHelper> lectures;
    private ArrayList<String> quizTitle;
    private ModifyLectureAdapter modifyLectureAdapter;
    private QuizDeleteAdapter quizDeleteAdapter;
    private DatabaseReference reference;
    private Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecture_upload);
        relativeLayout = findViewById(R.id.newLecture);
        vTitle = findViewById(R.id.title);
        i = getIntent();
        vTitle.setText(i.getStringExtra("ccName"));
        createQuiz = findViewById(R.id.createQuiz);
        createQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), QuizCreate.class);
                intent.putExtra("ccPath", i.getStringExtra("ccPath"));
                intent.putExtra("ccTitle", i.getStringExtra("ccName"));
                startActivity(intent);
            }
        });
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), GettingNewLectureDetials.class);
                intent.putExtra("ccKey",i.getStringExtra("ccKey"));
                startActivity(intent);
                finish();
            }
        });

        recyclerView = findViewById(R.id.lectureList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        lectures = new ArrayList<>();
        modifyLectureAdapter = new ModifyLectureAdapter(this,getApplicationContext(),lectures);
        recyclerView.setAdapter(modifyLectureAdapter);

        reference = FirebaseDatabase.getInstance().getReference("Courses").child(FirebaseAuth.getInstance().getUid())
                        .child(i.getStringExtra("ccKey")).child("lectures");
        reference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot :snapshot.getChildren()){
                    LectureHelper c = dataSnapshot.getValue(LectureHelper.class);
                    c.setlPath(dataSnapshot.getRef().getPath().toString());
                    lectures.add(c);
                }
                modifyLectureAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        quizList = findViewById(R.id.quizList);
        quizList.setHasFixedSize(true);
        quizList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        quizTitle = new ArrayList<>();
        quizDeleteAdapter = new QuizDeleteAdapter(getApplicationContext(),this,quizTitle);
        quizList.setAdapter(quizDeleteAdapter);
        reference = FirebaseDatabase.getInstance().getReference("Courses").child(FirebaseAuth.getInstance().getUid())
                .child(i.getStringExtra("ccKey")).child("quiz").child("quizdata");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                quizTitle.clear();
                for(DataSnapshot title:snapshot.getChildren()){
                    quizTitle.add(title.getKey());
                }
                quizDeleteAdapter.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


    @Override
    public void onModifyClick(int position) {
        LectureHelper lectureHelper = lectures.get(position);
        Intent intent = new Intent(getApplicationContext(), LectureModify.class);
        intent.putExtra("Lpath",lectureHelper.getlPath());
        intent.putExtra("Ltitle",lectureHelper.getTitle());
        intent.putExtra("LvideoLink",lectureHelper.getVideoLink());
        intent.putExtra("LnotesLink",lectureHelper.getNotesLink());
        intent.putExtra("Lfinal",lectureHelper.getFinal());
        startActivity(intent);
    }

    @Override
    public void deleteQuiz(int position) {
        String title = quizTitle.get(position);
        reference = FirebaseDatabase.getInstance().getReference("Courses").child(FirebaseAuth.getInstance().getUid())
                .child(i.getStringExtra("ccKey")).child("quiz").child("quizdata").child(title);
        reference.removeValue();
        reference = FirebaseDatabase.getInstance().getReference("Courses").child(FirebaseAuth.getInstance().getUid())
                .child(i.getStringExtra("ccKey")).child("quiz").child("quizScore");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot users: snapshot.getChildren()){
                    Log.d("UT_Delete", "onDataChange: "+users.getKey());
                    if(users.hasChild(title)){
                        reference = reference.child(users.getKey()).child(title);
                        reference.removeValue();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public void onQuizClick(int position) {
        Intent intent = new Intent(getApplicationContext(),ShowQuizMarks.class);
        intent.putExtra("quizTitle", quizTitle.get(position));
        intent.putExtra("ccKey", i.getStringExtra("ccKey"));
        startActivity(intent);
    }
}