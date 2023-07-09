package Fragments;

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

import com.example.geek_mooc.CourseTakingPage;
import com.example.geek_mooc.LectureUpload;
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


public class RegisteredCoursesFragment extends Fragment implements RecyclerViewInterface {

    public RegisteredCoursesFragment() {
        // Required empty public constructor
    }

    private RecyclerView recyclerView;
    private CourseHolder courseHolderAdapter;
    private ArrayList<CreateCourseHelper> createCourseHelpers;
    private DatabaseReference reference;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_registered_courses, container, false);
        recyclerView = view.findViewById(R.id.showCourse);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        createCourseHelpers = new ArrayList<>();
        courseHolderAdapter = new CourseHolder(getContext(),createCourseHelpers, this);
        recyclerView.setAdapter(courseHolderAdapter);

        reference = FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getUid()).child("registeredCourses");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot keys: snapshot.getChildren()){
                    reference = FirebaseDatabase.getInstance().getReference("Courses");
                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(DataSnapshot dataSnapshot :snapshot.getChildren()){
                                for(DataSnapshot course : dataSnapshot.getChildren()){
                                    if(course.getKey().equals(keys.getKey())) {
                                        CreateCourseHelper c = course.getValue(CreateCourseHelper.class);
                                        createCourseHelpers.add(c);
                                    }
                                }
                            }
                            courseHolderAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.d("UT database", error.toString());
                        }
                    });
                }
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
        intent.putExtra("ccRegistrations",createCourseHelpers.get(position).getRegistrations());
        intent.putExtra("ccAuthor",createCourseHelpers.get(position).getAuthor());
        intent.putExtra("ccNoofRatings",createCourseHelpers.get(position).getNoOfRatings());
        intent.putExtra("ccUpdate",createCourseHelpers.get(position).getLastUpdate());
        intent.putExtra("ccKey", createCourseHelpers.get(position).getKey());
        startActivity(intent);

    }
}