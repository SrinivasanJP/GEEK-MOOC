package Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import androidx.appcompat.widget.SearchView;
import android.widget.TextView;

import com.example.geek_mooc.R;
import com.example.geek_mooc.RegisterCourse;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import Backend.CreateCourseHelper;
import Backend.SharedPreferenceStore;
import RecyclerHelper.CourseHolder;
import RecyclerHelper.RecyclerViewInterface;

public class HomeFragment extends Fragment implements RecyclerViewInterface {
    SharedPreferences sharedPreferences;

    DatabaseReference databaseReference;
    RecyclerView recyclerView;
    CourseHolder courseHolderAdapter;
    TextView usernameHolder;
    ArrayList<CreateCourseHelper> createCourseHelpers ;
    SearchView searchView;
    public HomeFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        usernameHolder = view.findViewById(R.id.username);
        sharedPreferences = getActivity().getSharedPreferences(SharedPreferenceStore.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);

        usernameHolder.setText("Hello "+sharedPreferences.getString(SharedPreferenceStore.KEY_NAME,"Username"));
        databaseReference = FirebaseDatabase.getInstance().getReference("Courses");
        recyclerView = view.findViewById(R.id.showCourse);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        searchView = view.findViewById(R.id.searchBox);
        EditText searchText = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        searchText.setTextColor(getResources().getColor(R.color.white));
        searchText.setHintTextColor(getResources().getColor(R.color.white));


        createCourseHelpers = new ArrayList<>();
        courseHolderAdapter = new CourseHolder(getContext(),createCourseHelpers, this);

        recyclerView.setAdapter(courseHolderAdapter);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                createCourseHelpers.clear();
                for(DataSnapshot dataSnapshot :snapshot.getChildren()){
                    for(DataSnapshot course : dataSnapshot.getChildren()){
                        CreateCourseHelper c = course.getValue(CreateCourseHelper.class);
                        c.setCpath(course.getRef().getPath().toString());
                        createCourseHelpers.add(c);
                    }
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
        Intent intent = new Intent(getContext(), RegisterCourse.class);
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
}