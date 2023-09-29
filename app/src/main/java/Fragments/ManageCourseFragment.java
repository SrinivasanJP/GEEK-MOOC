package Fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import jp.srini.geek_mooc.LectureUpload;
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


public class ManageCourseFragment extends Fragment implements RecyclerViewInterface {


    private TextView vCount;
    private DatabaseReference reference;
    private RecyclerView recyclerView;
    private ArrayList<CreateCourseHelper> createCourseHelpers;
    private CourseHolder courseHolderAdapter;
    public ManageCourseFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_manage_course, container, false);
        vCount = view.findViewById(R.id.NoofCourseCreated);
        reference = FirebaseDatabase.getInstance().getReference("Courses").child(FirebaseAuth.getInstance().getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                vCount.setText(String.valueOf(snapshot.getChildrenCount()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        recyclerView = view.findViewById(R.id.showCourse);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        createCourseHelpers = new ArrayList<>();
        courseHolderAdapter = new CourseHolder(getContext(),createCourseHelpers, this,false);

        recyclerView.setAdapter(courseHolderAdapter);
        reference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot course : snapshot.getChildren()){
                    CreateCourseHelper c = course.getValue(CreateCourseHelper.class);
                    c.setCpath(course.getRef().getPath().toString());
                    createCourseHelpers.add(c);
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
        Intent intent = new Intent(getContext(), LectureUpload.class);
        intent.putExtra("ccName",createCourseHelpers.get(position).getTitle());
        intent.putExtra("ccDes",createCourseHelpers.get(position).getDescription());
        intent.putExtra("ccIntro",createCourseHelpers.get(position).getIntrolink());
        intent.putExtra("ccLang",createCourseHelpers.get(position).getLanguage());
        intent.putExtra("ccRating",createCourseHelpers.get(position).getRatings());
        intent.putExtra("ccRegistrations",createCourseHelpers.get(position).getRegistrations());
        intent.putExtra("ccAuthor",createCourseHelpers.get(position).getAuthor());
        intent.putExtra("ccNoofRatings",createCourseHelpers.get(position).getNoOfRatings());
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