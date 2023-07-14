package Quiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.geek_mooc.Home;
import com.example.geek_mooc.LectureUpload;
import com.example.geek_mooc.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class QuizCreate extends AppCompatActivity {
    private TextView vTitle,vQuestionNo,vBtntext;
    private EditText vQuestion,vOp1,vOp2,vOp3,vOp4,vAnswer;
    private RelativeLayout vNextBtn, vCreateBtn;
    private ProgressBar vProgressbar;
    private ArrayList<QuizHelper> quizData;
    private Intent getIntent;
    private String coursePath;
    private DatabaseReference reference;
    private long quizCount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_create);
        getIntent = getIntent();

        vTitle = findViewById(R.id.title);
        vQuestionNo = findViewById(R.id.questionNo);
        vBtntext = findViewById(R.id.BtnCreateText);

        vQuestion = findViewById(R.id.question);
        vOp1 = findViewById(R.id.op1);
        vOp2 = findViewById(R.id.op2);
        vOp3 = findViewById(R.id.op3);
        vOp4 = findViewById(R.id.op4);
        vAnswer = findViewById(R.id.ans);

        vNextBtn = findViewById(R.id.nextBtn);
        vCreateBtn = findViewById(R.id.createBtn);

        vProgressbar = findViewById(R.id.ProgressbarCreate);

        quizData = new ArrayList<>();
        vQuestionNo.setText("Question No: "+quizData.size());



        vTitle.setText(getIntent.getStringExtra("ccTitle"));
        coursePath = getIntent.getStringExtra("ccPath");
        reference = FirebaseDatabase.getInstance().getReference(coursePath).child("quiz").child("quizdata");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                quizCount = snapshot.getChildrenCount();
                for(int i=0;i<quizCount;i++){
                    if(snapshot.hasChild(quizCount+"_Quiz")){
                        quizCount+=1;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        vNextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                storeLocalData();
            }
        });
        vCreateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                storeLocalData();
                vBtntext.setVisibility(View.INVISIBLE);
                vProgressbar.setVisibility(View.VISIBLE);
                reference = FirebaseDatabase.getInstance().getReference(coursePath).child("quiz").child("quizdata").child(quizCount+"_Quiz");
                reference.setValue(quizData).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @SuppressLint("RestrictedApi")
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Log.d("UT-datanotaffected", reference.getPath().toString());
                            Toast.makeText(QuizCreate.this, "Quiz created Successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), Home.class));
                        }else{
                            vBtntext.setVisibility(View.VISIBLE);
                            vProgressbar.setVisibility(View.INVISIBLE);
                            Toast.makeText(QuizCreate.this, "Quiz Creation is terminated", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });




    }
    private void storeLocalData(){
        QuizHelper quizHelper = new QuizHelper();
        quizHelper.setQuestion(vQuestion.getText().toString().trim());
        quizHelper.setOp1(vOp1.getText().toString().trim());
        quizHelper.setOp2(vOp2.getText().toString().trim());
        quizHelper.setOp3(vOp3.getText().toString().trim());
        quizHelper.setOp4(vOp4.getText().toString().trim());
        quizHelper.setCorrect(Integer.parseInt(vAnswer.getText().toString().trim()));
        quizData.add(quizHelper);
        clearTexts();
    }
    private void clearTexts(){
        vQuestionNo.setText("Question No: "+quizData.size());
        vQuestion.setText("");
        vOp1.setText("");
        vOp2.setText("");
        vOp3.setText("");
        vOp4.setText("");
        vAnswer.setText("");
    }
}