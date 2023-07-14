package Fragments;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import com.example.geek_mooc.GettingCourseDetials;
import com.example.geek_mooc.ModifyCourse;
import com.example.geek_mooc.R;
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


public class CreateCourseFragment extends Fragment implements RecyclerViewInterface {
    RelativeLayout vNewCourse;
    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    CourseHolder courseHolderAdapter;
    private ArrayList<CreateCourseHelper> existingCourses;

    public CreateCourseFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_create_course, container, false);
        vNewCourse = view.findViewById(R.id.newCourse);
        vNewCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), GettingCourseDetials.class));
            }
        });
        databaseReference = FirebaseDatabase.getInstance().getReference("Courses").child(FirebaseAuth.getInstance().getUid());
        recyclerView = view.findViewById(R.id.existingCourse);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        existingCourses = new ArrayList<>();
        courseHolderAdapter = new CourseHolder(getContext(),existingCourses, this);
        recyclerView.setAdapter(courseHolderAdapter);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                existingCourses.clear();
                for(DataSnapshot dataSnapshot :snapshot.getChildren()){
                    CreateCourseHelper c = dataSnapshot.getValue(CreateCourseHelper.class);
                    c.setCpath(dataSnapshot.getRef().getPath().toString());
                    existingCourses.add(c);
                }
                courseHolderAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return view;
    }

    @Override
    public void onCourseClick(int position) {
        Intent intent = new Intent(getContext(), ModifyCourse.class);
        intent.putExtra("Cpath", existingCourses.get(position).getCpath());
        startActivity(intent);
    }

    @Override
    public void onClickNotesBtn(int position) {

    }
}