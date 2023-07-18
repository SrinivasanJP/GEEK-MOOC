package Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import androidx.appcompat.widget.SearchView;
import android.widget.TextView;

import com.example.geek_mooc.CourseCategory;
import com.example.geek_mooc.R;
import com.example.geek_mooc.RegisterCourse;
import com.google.firebase.auth.FirebaseAuth;
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
    RecyclerView recyclerView, searchList, watchList;
    CardView categories[];
    int categoriesId[];
    CourseHolder courseHolderAdapter, searchAdapter, watchlistAdapter;
    TextView usernameHolder;
    ArrayList<CreateCourseHelper> createCourseHelpers, searchHelper, watchlisthelper ;
    SearchView searchView;
    String passStrings[];
    ArrayList<String> progressKeys;
    public HomeFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        passStrings = new String[]{"Programming","Computers","Trading","IoT","Science","Astronomy"};
        categoriesId = new int[]{R.id.programming, R.id.computers, R.id.trading, R.id.iot, R.id.science, R.id.astronomy};
        categories = new CardView[6];
        for(int i=0;i<6;i++){
            categories[i] = view.findViewById(categoriesId[i]);
            int finalI = i;
            categories[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getContext(), CourseCategory.class);
                    intent.putExtra("category",passStrings[finalI]);
                    startActivity(intent);
                }
            });
        }


        usernameHolder = view.findViewById(R.id.username);
        sharedPreferences = getActivity().getSharedPreferences(SharedPreferenceStore.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);

        usernameHolder.setText("Hello "+sharedPreferences.getString(SharedPreferenceStore.KEY_NAME,"Username"));
        databaseReference = FirebaseDatabase.getInstance().getReference("Courses");
        recyclerView = view.findViewById(R.id.showCourse);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        searchList = view.findViewById(R.id.searchHelper);
        searchList.setHasFixedSize(true);
        searchList.setLayoutManager(new LinearLayoutManager(getContext()));

        watchList = view.findViewById(R.id.showWatchlistCourse);
        watchList.setHasFixedSize(true);
        watchList.setLayoutManager(new LinearLayoutManager(getContext()));

        searchView = view.findViewById(R.id.searchBox);
        EditText searchText = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        searchText.setTextColor(getResources().getColor(R.color.white));
        searchText.setHintTextColor(getResources().getColor(R.color.white));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d("UT_search", "onQueryTextSubmit: "+query);
                searchHelper.clear();
                for(CreateCourseHelper c: createCourseHelpers){
                    if(c.getTitle().toLowerCase().contains(query.toLowerCase())){
                        searchHelper.add(c);
                    }
                }
                searchAdapter.notifyDataSetChanged();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchList.setVisibility(View.VISIBLE);
                searchHelper.clear();
                for(CreateCourseHelper c: createCourseHelpers){
                    if(newText.isEmpty()){
                        searchList.setVisibility(View.GONE);
                        searchHelper.clear();
                    }
                    if(c.getTitle().toLowerCase().contains(newText.toLowerCase())){
                        searchHelper.add(c);
                    }
                }
                searchAdapter.notifyDataSetChanged();
                return false;
            }
        });


        createCourseHelpers = new ArrayList<>();
        searchHelper = new ArrayList<>();
        watchlisthelper = new ArrayList<>();
        progressKeys = new ArrayList<>();
        courseHolderAdapter = new CourseHolder(getContext(),createCourseHelpers, this, false);
        searchAdapter = new CourseHolder(getContext(),searchHelper,this, false);
        watchlistAdapter = new CourseHolder(getContext(),watchlisthelper,this,true);

        recyclerView.setAdapter(courseHolderAdapter);
        searchList.setAdapter(searchAdapter);
        watchList.setAdapter(watchlistAdapter);
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

        databaseReference = FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getUid()).child("watchListCourses");

        databaseReference.addValueEventListener(new ValueEventListener() {
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

    @Override
    public void onCompletedCourseClick(int position) {
        Intent intent = new Intent(getContext(), RegisterCourse.class);
        intent.putExtra("ccName",watchlisthelper.get(position).getTitle());
        intent.putExtra("ccDes",watchlisthelper.get(position).getDescription());
        intent.putExtra("ccIntro",watchlisthelper.get(position).getIntrolink());
        intent.putExtra("ccLang",watchlisthelper.get(position).getLanguage());
        intent.putExtra("ccRating",watchlisthelper.get(position).getRatings()+"");
        intent.putExtra("ccRegistrations",watchlisthelper.get(position).getRegistrations()+"");
        intent.putExtra("ccAuthor",watchlisthelper.get(position).getAuthor());
        intent.putExtra("ccNoofRatings",watchlisthelper.get(position).getNoOfRatings()+"");
        intent.putExtra("ccUpdate",watchlisthelper.get(position).getLastUpdate());
        intent.putExtra("ccKey", watchlisthelper.get(position).getKey());
        intent.putExtra("ccPath",watchlisthelper.get(position).getCpath());
        startActivity(intent);
    }
    private void getCourses(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Courses");
        reference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                watchlisthelper.clear();
                for(DataSnapshot authors :snapshot.getChildren()){
                    for(DataSnapshot course : authors.getChildren()){
                        Log.d("UT_final", "onDataChange: "+course.getKey());
                        if(progressKeys.contains(course.getKey())) {
                            CreateCourseHelper c = course.getValue(CreateCourseHelper.class);
                            c.setCpath(course.getRef().getPath().toString());
                            watchlisthelper.add(c);
                        }
                    }
                }
                watchlistAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("UT database", error.toString());
            }
        });
    }
}