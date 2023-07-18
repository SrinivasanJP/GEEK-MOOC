package com.example.geek_mooc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import Backend.CreateCourseHelper;
import RecyclerHelper.CourseHolder;
import RecyclerHelper.RecyclerViewInterface;

public class CourseCategory extends AppCompatActivity implements RecyclerViewInterface {
    private String category;
    private RecyclerView recyclerView;
    private ArrayList<CreateCourseHelper> createCourseHelpers;
    private CourseHolder courseHolderAdapter;
    private DatabaseReference databaseReference;
    private TextView categoryText;
    private ImageView categoryImg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_category);
        category = getIntent().getStringExtra("category");
        categoryText = findViewById(R.id.bannerText);
        categoryImg = findViewById(R.id.bannerImg);
        categoryText.setText(category);
        switch (category.toLowerCase()){
            case "programming":{
                categoryImg.setImageResource(R.drawable.programming);
                break;
            }

            case "computers":{
                categoryImg.setImageResource(R.drawable.computers);
                break;
            }
            case "trading":{
                categoryImg.setImageResource(R.drawable.trading);
                break;
            }
            case "iot":{
                categoryImg.setImageResource(R.drawable.iot);
                break;
            }
            case "science":{
                categoryImg.setImageResource(R.drawable.science);
                break;
            }
            case "astronomy":{
                categoryImg.setImageResource(R.drawable.austro);
                break;
            }

        }
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        createCourseHelpers = new ArrayList<>();
        courseHolderAdapter = new CourseHolder(getApplicationContext(),createCourseHelpers, this, false);
        recyclerView.setAdapter(courseHolderAdapter);
        databaseReference = FirebaseDatabase.getInstance().getReference("Courses");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                createCourseHelpers.clear();
                for(DataSnapshot dataSnapshot :snapshot.getChildren()){
                    for(DataSnapshot course : dataSnapshot.getChildren()){
                        CreateCourseHelper c = course.getValue(CreateCourseHelper.class);

                        if(c.getBasket().toLowerCase().equals(category.toLowerCase())){
                            c.setCpath(course.getRef().getPath().toString());
                            createCourseHelpers.add(c);
                        }
                    }
                }
                Log.d("UT_Adapter", "onDataChange: "+createCourseHelpers.size());
                courseHolderAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    public void onCourseClick(int position) {
        Intent intent = new Intent(getApplicationContext(), RegisterCourse.class);
        intent.putExtra("ccName",createCourseHelpers.get(position).getTitle());
        intent.putExtra("ccDes",createCourseHelpers.get(position).getDescription());
        intent.putExtra("ccIntro",createCourseHelpers.get(position).getIntrolink());
        intent.putExtra("ccLang",createCourseHelpers.get(position).getLanguage());
        intent.putExtra("ccRating",createCourseHelpers.get(position).getRatings()+"");
        intent.putExtra("ccRegistrations",createCourseHelpers.get(position).getRegistrations()+"");
        intent.putExtra("ccAuthor",createCourseHelpers.get(position).getAuthor());
        intent.putExtra("ccNoofRatings",createCourseHelpers.get(position).getNoOfRatings()+"");
        intent.putExtra("ccUpdate",createCourseHelpers.get(position).getLastUpdate());
        intent.putExtra("ccKey", createCourseHelpers.get(position).getKey());
        intent.putExtra("ccPath",createCourseHelpers.get(position).getCpath());
        startActivity(intent);


    }

    @Override
    public void onClickNotesBtn(int position) {

    }

    @Override
    public void onCompletedCourseClick(int position) {

    }
}