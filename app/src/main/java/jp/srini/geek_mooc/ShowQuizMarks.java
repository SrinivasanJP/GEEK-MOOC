package jp.srini.geek_mooc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.Srini.geek_mooc.R;;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import Quiz.MarkListAdapter;
import Quiz.Marks;

public class ShowQuizMarks extends AppCompatActivity {
    private RecyclerView marksView;
    private DatabaseReference reference;
    private ArrayList<Marks> marks;
    private MarkListAdapter markListAdapter;
    private Intent getIntent;
    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_quiz_marks);
        getIntent = getIntent();
        title = getIntent.getStringExtra("quizTitle");
        marksView = findViewById(R.id.marksList);
        marksView.setHasFixedSize(true);
        marksView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        marks = new ArrayList<>();
        markListAdapter = new MarkListAdapter(getApplicationContext(),marks);
        marksView.setAdapter(markListAdapter);

        marksView = findViewById(R.id.marksList);
        reference = FirebaseDatabase.getInstance().getReference("Courses").child(FirebaseAuth.getInstance().getUid()).child(getIntent.getStringExtra("ccKey")).child("quiz").child("quizScore");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                marks.clear();
                for(DataSnapshot users: snapshot.getChildren()){
                    if(users.hasChild(title)){
                        for(DataSnapshot quiz: users.getChildren()){
                            if(title.equals(quiz.getKey())){
                                Marks qmark = new Marks("",users.getKey(),quiz.getValue(Integer.class));
                                marks.add(qmark);
                                Log.d("UT-database", "onDataChange: "+qmark.getId()+"/"+qmark.getMarks());
                            }
                        }
                    }
                }
                getNames();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }
    public void getNames(){

        for(Marks mark: marks){
            Log.d("UT-idcheck", "getNames: "+mark.getId());
            reference = FirebaseDatabase.getInstance().getReference("users").child(mark.getId()).child("name");
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    mark.setName(snapshot.getValue(String.class));
                    Log.d("UT-name", "onDataChange: "+mark.getName());
                    markListAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
}