package Quiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import jp.srini.geek_mooc.Home;
import com.Srini.geek_mooc.R;;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class QuizStart extends AppCompatActivity {
    private TextView vTitle,vQuestionNo,vQuestion,vOp1,vOp2,vOp3,vOp4,vSubmitText;
    private RelativeLayout vBtnSubmit,vBtnNext,vOp1Btn,vOp2Btn,vOp3Btn,vOp4Btn;
    private ProgressBar vProgressbar;
    private Intent getIntent;
    private ArrayList<QuizHelper> quizData;
    private DatabaseReference reference;
    private String coursePath;
    private int position,correct,score;

    private CountDownTimer countDownTimer;
    private int timerValue = 600;
    private ProgressBar progressTimer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_start);
        getIntent = getIntent();
        progressTimer = findViewById(R.id.progresstimer);

        countDownTimer = new CountDownTimer(600000,1000) {
            @Override
            public void onTick(long l) {
                progressTimer.setProgress(--timerValue);
            }

            @Override
            public void onFinish() {
                Dialog dialog = new Dialog(QuizStart.this);
                dialog.setContentView(R.layout.dialog_time_out);
                dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
                dialog.findViewById(R.id.Backbtn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        reference = FirebaseDatabase.getInstance().getReference(coursePath).child("quiz").child("quizScore").child(FirebaseAuth.getInstance().getUid()).child(getIntent.getStringExtra("quizTitle"));
                        reference.setValue(score).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(QuizStart.this, "Your score "+score, Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getApplicationContext(), Home.class));
                                }else{
                                    vSubmitText.setVisibility(View.VISIBLE);
                                    vProgressbar.setVisibility(View.INVISIBLE);
                                    Toast.makeText(QuizStart.this, "Unknown error occors", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
                dialog.show();
            }
        }.start();


        coursePath = getIntent.getStringExtra("ccPath");
        quizData = new ArrayList<>();
        position=0;
        score = 0;

        vTitle = findViewById(R.id.title);
        vQuestionNo = findViewById(R.id.questionNo);
        vQuestion = findViewById(R.id.question);
        vOp1 = findViewById(R.id.op1);
        vOp2 = findViewById(R.id.op2);
        vOp3 = findViewById(R.id.op3);
        vOp4 = findViewById(R.id.op4);
        vSubmitText = findViewById(R.id.BtnSubmitText);

        vOp1Btn = findViewById(R.id.op1Container);
        vOp2Btn = findViewById(R.id.op2Container);
        vOp3Btn = findViewById(R.id.op3Container);
        vOp4Btn = findViewById(R.id.op4Container);
        vBtnNext = findViewById(R.id.nextBtn);
        vBtnSubmit = findViewById(R.id.submitBtn);

        vProgressbar = findViewById(R.id.ProgressbarSubmit);
        vTitle.setText(getIntent.getStringExtra("ccTitle"));

        reference = FirebaseDatabase.getInstance().getReference(coursePath).child("quiz").child("quizdata").child(getIntent.getStringExtra("quizTitle"));
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                quizData.clear();
                for(DataSnapshot data : snapshot.getChildren()) {
                    quizData.add(data.getValue(QuizHelper.class));
                }
                setData(position);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        vBtnSubmit.setVisibility(View.INVISIBLE);
        vBtnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                position += 1;
                if(position<quizData.size()) {
                    setData(position );
                }
                else {
                    vBtnNext.setVisibility(View.INVISIBLE);

                }
            }
        });
        vOp1Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(correct==1){
                    score+=1;
                    vOp1.setBackground(getDrawable(R.drawable.correct));
                }else{
                    vOp1.setBackground(getDrawable(R.drawable.incorrect));
                    switch (correct){
                        case 2:{
                            vOp2.setBackground(getDrawable(R.drawable.correct));
                            break;
                        }
                        case 3:{
                            vOp3.setBackground(getDrawable(R.drawable.correct));
                            break;
                        }
                        case 4:{
                            vOp4.setBackground(getDrawable(R.drawable.correct));
                            break;
                        }

                    }
                }
            }
        });
        vOp2Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(correct==2){
                    score+=1;
                    vOp2.setBackground(getDrawable(R.drawable.correct));
                }else{
                    vOp2.setBackground(getDrawable(R.drawable.incorrect));
                    switch (correct){
                        case 1:{
                            vOp1.setBackground(getDrawable(R.drawable.correct));
                            break;
                        }
                        case 3:{
                            vOp3.setBackground(getDrawable(R.drawable.correct));
                            break;
                        }
                        case 4:{
                            vOp4.setBackground(getDrawable(R.drawable.correct));
                            break;
                        }

                    }
                }
            }
        });
        vOp3Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(correct==3){
                    score+=1;
                    vOp3.setBackground(getDrawable(R.drawable.correct));
                }else{
                    vOp3.setBackground(getDrawable(R.drawable.incorrect));
                    switch (correct){
                        case 1:{
                            vOp1.setBackground(getDrawable(R.drawable.correct));
                            break;
                        }
                        case 2:{
                            vOp2.setBackground(getDrawable(R.drawable.correct));
                            break;
                        }
                        case 4:{
                            vOp4.setBackground(getDrawable(R.drawable.correct));
                            break;
                        }

                    }
                }
            }
        });
        vOp4Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(correct==4){
                    score+=1;
                    vOp4.setBackground(getDrawable(R.drawable.correct));
                }else{
                    vOp4.setBackground(getDrawable(R.drawable.incorrect));
                    switch (correct){
                        case 2:{
                            vOp2.setBackground(getDrawable(R.drawable.correct));
                            break;
                        }
                        case 3:{
                            vOp3.setBackground(getDrawable(R.drawable.correct));
                            break;
                        }
                        case 1:{
                            vOp1.setBackground(getDrawable(R.drawable.correct));
                            break;
                        }

                    }
                }
            }
        });

        vBtnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vSubmitText.setVisibility(View.INVISIBLE);
                vProgressbar.setVisibility(View.VISIBLE);
                reference = FirebaseDatabase.getInstance().getReference(coursePath).child("quiz").child("quizScore").child(FirebaseAuth.getInstance().getUid()).child(getIntent.getStringExtra("quizTitle"));
                reference.setValue(score).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(QuizStart.this, "Your score "+score, Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), Home.class));
                        }else{
                            vSubmitText.setVisibility(View.VISIBLE);
                            vProgressbar.setVisibility(View.INVISIBLE);
                            Toast.makeText(QuizStart.this, "Unknown error occors", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });





    }
    private void setData(int position){
        QuizHelper quizHelper = quizData.get(position);
        vQuestionNo.setText("Question N0: "+(position+1));
        vOp1.setBackground(getDrawable(R.drawable.curve_box));
        vOp2.setBackground(getDrawable(R.drawable.curve_box));
        vOp3.setBackground(getDrawable(R.drawable.curve_box));
        vOp4.setBackground(getDrawable(R.drawable.curve_box));
        vQuestion.setText(quizHelper.getQuestion());
        vOp1.setText(quizHelper.getOp1());
        vOp2.setText(quizHelper.getOp2());
        vOp3.setText(quizHelper.getOp3());
        vOp4.setText(quizHelper.getOp4());
        correct = quizHelper.getCorrect();
        if(position==quizData.size()-1) {
            vBtnNext.setVisibility(View.INVISIBLE);
            vBtnSubmit.setVisibility(View.VISIBLE);
            Toast.makeText(QuizStart.this, "You came to last question", Toast.LENGTH_SHORT).show();
        }
    }
}