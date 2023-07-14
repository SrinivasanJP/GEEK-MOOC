package com.example.geek_mooc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
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
import Backend.LectureHelper;
import Backend.UserHelper;
import RecyclerHelper.LectureHolder;
import RecyclerHelper.RecyclerViewInterface;
import RecyclerHelper.UserAdapter;

public class ModifyCourse extends AppCompatActivity implements RecyclerViewInterface {

    private TextView vData[];
    private int ids[];
    private RecyclerView userRecycle, lectureRecycle;
    private Intent intent;
    private String coursePath;
    private CreateCourseHelper selectedCourse;
    private DatabaseReference reference;
    private ArrayList<UserHelper> users;
    private ArrayList<String> userKeys;
    private Boolean finish;
    private UserAdapter userAdapter;
    private LectureHolder lectureHolder;
    private ArrayList<LectureHelper> lectureHelper;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_course);
        intent = getIntent();
        coursePath = intent.getStringExtra("Cpath");


        ids = new int[]{R.id.title, R.id.description, R.id.key, R.id.language, R.id.lastUpdate,
        R.id.ratings, R.id.registers, R.id.noOfLectures, R.id.modifyCourse, R.id.deleteCourse};
        vData = new TextView[10];
        for (int i=0;i<10;i++){
            vData[i] = findViewById(ids[i]);
        }
        userRecycle = findViewById(R.id.recycler_user);
        lectureRecycle = findViewById(R.id.recycler_lectures);

        reference = FirebaseDatabase.getInstance().getReference(coursePath);
        //entering basic detials
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                selectedCourse = snapshot.getValue(CreateCourseHelper.class);
                vData[0].setText("Title: "+selectedCourse.getTitle());
                vData[1].setText("Description: "+selectedCourse.getDescription());
                vData[2].setText("Course Key: "+selectedCourse.getKey());
                vData[3].setText("Course Language: "+selectedCourse.getLanguage());
                vData[4].setText("Last Updated: "+selectedCourse.getLastUpdate());
                vData[5].setText("Course Ratings: "+selectedCourse.getRatings()+"");
                vData[6].setText("No of Students Registered: "+selectedCourse.getRegistrations()+"");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        reference.child("lectures").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                vData[7].setText("No of Lectures: "+snapshot.getChildrenCount()+"");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        vData[8].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), GettingCourseDetials.class);
                intent.putExtra("ccName",selectedCourse.getTitle());
                intent.putExtra("ccDes",selectedCourse.getDescription());
                intent.putExtra("ccIntro",selectedCourse.getIntrolink());
                intent.putExtra("ccLang",selectedCourse.getLanguage());
                intent.putExtra("ccRating",selectedCourse.getRatings());
                intent.putExtra("ccRegistrations",selectedCourse.getRegistrations()+"");
                intent.putExtra("ccAuthor",selectedCourse.getAuthor());
                intent.putExtra("ccNoofRatings",selectedCourse.getNoOfRatings());
                intent.putExtra("ccUpdate",selectedCourse.getLastUpdate());
                intent.putExtra("ccKey", selectedCourse.getKey());
                intent.putExtra("ccPath",selectedCourse.getCpath());
                startActivity(intent);
            }
        });
        vData[9].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder deleteAlert = new AlertDialog.Builder(new ContextThemeWrapper(ModifyCourse.this, R.style.myDialog));
                deleteAlert.setTitle("Course Delete!");
                deleteAlert.setMessage("Confirm to delete course "+selectedCourse.getTitle());
                deleteAlert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        reference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(ModifyCourse.this, "Course Deleted", Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(ModifyCourse.this, "Course Not Deleted", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
                deleteAlert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(ModifyCourse.this, "Deletion Cancelled", Toast.LENGTH_SHORT).show();
                    }
                });
                AlertDialog dialog = deleteAlert.create();
                dialog.show();
            }
        });


        userRecycle = findViewById(R.id.recycler_user);
        userRecycle.setHasFixedSize(true);
        userRecycle.setLayoutManager(new LinearLayoutManager(getApplicationContext()));


        userKeys = new ArrayList<>();
        users = new ArrayList<>();
        userAdapter = new UserAdapter(getApplicationContext(),users);
        userRecycle.setAdapter(userAdapter);
        finish = false;
        //getting user keys
        reference.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot user: snapshot.getChildren()){
                    userKeys.add(user.getKey());
                }
                //using user keys access their detials
                setUsers();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        lectureRecycle.setHasFixedSize(true);
        lectureRecycle.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        lectureHelper = new ArrayList<>();
        lectureHolder = new LectureHolder(this, getApplicationContext(),lectureHelper);
        lectureRecycle.setAdapter(lectureHolder);
        reference = FirebaseDatabase.getInstance().getReference(coursePath);
        reference.child("lectures").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                lectureHelper.clear();
                for (DataSnapshot lectures : snapshot.getChildren()) {
                        lectureHelper.add(lectures.getValue(LectureHelper.class));
                    }
                lectureHolder.notifyDataSetChanged();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });












    }
    private void setUsers(){
        for (String keys : userKeys) {
            Log.d("Keys: ", keys);
            reference = FirebaseDatabase.getInstance().getReference("users").child(keys);
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    users.add(snapshot.getValue(UserHelper.class));
                    Log.d("UT-names", snapshot.getValue(UserHelper.class).getName());
                    userAdapter.notifyDataSetChanged();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    @Override
    public void onCourseClick(int position) {

    }

    @Override
    public void onClickNotesBtn(int position) {

    }
}