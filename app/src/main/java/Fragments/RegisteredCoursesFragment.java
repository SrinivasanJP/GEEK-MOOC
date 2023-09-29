package Fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import jp.srini.geek_mooc.CourseTakingPage;

import com.Srini.geek_mooc.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import Backend.CreateCourseHelper;
import RecyclerHelper.CourseHolder;
import RecyclerHelper.RecyclerViewInterface;


public class RegisteredCoursesFragment extends Fragment implements RecyclerViewInterface {

    public RegisteredCoursesFragment() {
        // Required empty public constructor
    }

    private TextView vNoOfRegisters, vNoOfCompletion, vNoCourseProgress;
    private RecyclerView recyclerView,completedView;
    private CourseHolder courseHolderAdapter, completedCourseAdapter;
    private ArrayList<CreateCourseHelper> createCourseHelpers, completedCourseHelper;
    private ArrayList<String> completedKeys,progressKeys;
    private DatabaseReference reference;
    private View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_registered_courses, container, false);

        completedKeys = new ArrayList<>();
        completedCourseHelper = new ArrayList<>();
        createCourseHelpers = new ArrayList<>();
        progressKeys = new ArrayList<>();
        recyclerView = view.findViewById(R.id.showCourse);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        completedView = view.findViewById(R.id.showCompletedCourse);
        completedView.setHasFixedSize(true);
        completedView.setLayoutManager(new LinearLayoutManager(getContext()));


        courseHolderAdapter = new CourseHolder(getContext(),createCourseHelpers, this,false);

        completedCourseAdapter = new CourseHolder(getContext(),completedCourseHelper,this,true);
        completedView.setAdapter(completedCourseAdapter);

        recyclerView.setAdapter(courseHolderAdapter);


        vNoOfRegisters = view.findViewById(R.id.Noofregisters);
        vNoOfCompletion = view.findViewById(R.id.NoofCompleted);
        vNoCourseProgress = view.findViewById(R.id.noCourse);
        reference = FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getUid()).child("completedCourses");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                completedKeys.clear();
                for(DataSnapshot keys: snapshot.getChildren()){
                    completedKeys.add(keys.getKey());
                }
                vNoOfCompletion.setText(completedKeys.size()+"");
                getCourses();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        reference = FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getUid()).child("registeredCourses");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                progressKeys.clear();
                for(DataSnapshot key: snapshot.getChildren()){
                    progressKeys.add(key.getKey());
                }
               getCourses();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("UT database", error.toString());

            }
        });
        return view;
    }

    @Override
    public void onCourseClick(int position) {
        Intent intent = new Intent(getContext(), CourseTakingPage.class);
        intent.putExtra("ccName",createCourseHelpers.get(position).getTitle());
        intent.putExtra("ccDes",createCourseHelpers.get(position).getDescription());
        intent.putExtra("ccIntro",createCourseHelpers.get(position).getIntrolink());
        intent.putExtra("ccLang",createCourseHelpers.get(position).getLanguage());
        intent.putExtra("ccRating",createCourseHelpers.get(position).getRatings());
        intent.putExtra("ccRegistrations",createCourseHelpers.get(position).getRegistrations()+"");
        intent.putExtra("ccAuthor",createCourseHelpers.get(position).getAuthor());
        intent.putExtra("ccNoofRatings",createCourseHelpers.get(position).getNoOfRatings());
        intent.putExtra("ccUpdate",createCourseHelpers.get(position).getLastUpdate());
        intent.putExtra("ccKey", createCourseHelpers.get(position).getKey());
        intent.putExtra("ccPath", createCourseHelpers.get(position).getCpath());
        intent.putExtra("completed", false);
        createCourseHelpers.clear();
        startActivity(intent);

    }

    @Override
    public void onClickNotesBtn(int position) {

    }

    @Override
    public void onCompletedCourseClick(int position) {
        Intent intent = new Intent(getContext(), CourseTakingPage.class);
        intent.putExtra("ccName",completedCourseHelper.get(position).getTitle());
        intent.putExtra("ccDes",completedCourseHelper.get(position).getDescription());
        intent.putExtra("ccIntro",completedCourseHelper.get(position).getIntrolink());
        intent.putExtra("ccLang",completedCourseHelper.get(position).getLanguage());
        intent.putExtra("ccRating",completedCourseHelper.get(position).getRatings());
        intent.putExtra("ccRegistrations",completedCourseHelper.get(position).getRegistrations()+"");
        intent.putExtra("ccAuthor",completedCourseHelper.get(position).getAuthor());
        intent.putExtra("ccNoofRatings",completedCourseHelper.get(position).getNoOfRatings());
        intent.putExtra("ccUpdate",completedCourseHelper.get(position).getLastUpdate());
        intent.putExtra("ccKey", completedCourseHelper.get(position).getKey());
        intent.putExtra("ccPath", completedCourseHelper.get(position).getCpath());
        intent.putExtra("completed", true);
        completedCourseHelper.clear();
        startActivity(intent);
    }

    private void getCourses(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Courses");
            reference.addValueEventListener(new ValueEventListener() {
                @SuppressLint("RestrictedApi")
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    completedCourseHelper.clear();
                    createCourseHelpers.clear();
                    for(DataSnapshot authours :snapshot.getChildren()){
                        for(DataSnapshot course : authours.getChildren()){
                            Log.d("UT_final", "onDataChange: "+course.getKey());
                            if(completedKeys.contains(course.getKey())){
                                CreateCourseHelper c = course.getValue(CreateCourseHelper.class);
                                c.setCpath(course.getRef().getPath().toString());
                                completedCourseHelper.add(c);
                            }
                            else if(progressKeys.contains(course.getKey())) {
                                CreateCourseHelper c = course.getValue(CreateCourseHelper.class);
                                c.setCpath(course.getRef().getPath().toString());
                                createCourseHelpers.add(c);
                            }
                        }
                    }
                    vNoOfRegisters.setText(courseHolderAdapter.getItemCount()+"");
                    courseHolderAdapter.notifyDataSetChanged();
                    completedCourseAdapter.notifyDataSetChanged();
                    if(createCourseHelpers.size()==0){
                        vNoCourseProgress.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.d("UT database", error.toString());
                }
            });
        }
}