package com.example.geek_mooc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
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
import RecyclerHelper.CourseHolder;
import RecyclerHelper.RecyclerViewInterface;

public class LectureUpload extends AppCompatActivity implements RecyclerViewInterface {
    private TextView vTitle;
    private RelativeLayout relativeLayout;
    private RecyclerView recyclerView;
//    private ArrayList<LectureHelper> lectureHelpers;
//    private CourseHolder courseHolderAdapter;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecture_upload);
        relativeLayout = findViewById(R.id.newLecture);
        vTitle = findViewById(R.id.title);
        Intent i = getIntent();
        vTitle.setText(i.getStringExtra("ccName"));
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

//        lectureHelpers = new ArrayList<>();
//        courseHolderAdapter = new CourseHolder(getApplicationContext(),createCourseHelpers, this);
//
//        recyclerView.setAdapter(courseHolderAdapter);
//
//        reference = FirebaseDatabase.getInstance().getReference("Courses").child(FirebaseAuth.getInstance().getUid())
//                        .child(i.getStringExtra("ccKey")).child("lectures");
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for(DataSnapshot dataSnapshot :snapshot.getChildren()){
//                    LectureHelper c = dataSnapshot.getValue(LectureHelper.class);
//                    createCourseHelpers.add(c);
//                }
//                courseHolderAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
    }

    @Override
    public void onCourseClick(int position) {

    }
}