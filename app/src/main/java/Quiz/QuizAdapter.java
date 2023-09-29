package Quiz;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.Srini.geek_mooc.R;;

import java.util.ArrayList;


public class QuizAdapter extends RecyclerView.Adapter<QuizAdapter.myViewHolder> {

    private RecyclerViewQuizInterface recyclerViewQuizInterface;
    private Context context;
    private ArrayList<String> quizHelpers, doneQuiz;
    private ArrayList<Integer> quizScore;

    public QuizAdapter(RecyclerViewQuizInterface recyclerViewQuizInterface, Context context, ArrayList<String> quizHelpers, ArrayList<String> doneQuiz, ArrayList<Integer> quizScore) {
        this.recyclerViewQuizInterface = recyclerViewQuizInterface;
        this.context = context;
        this.quizHelpers = quizHelpers;
        this.doneQuiz = doneQuiz;
        this.quizScore = quizScore;
    }

    @NonNull
    @Override
    public QuizAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_quizlist,parent,false);
        return new myViewHolder(view,recyclerViewQuizInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull QuizAdapter.myViewHolder holder, @SuppressLint("RecyclerView") int position) {

        if(doneQuiz.contains(quizHelpers.get(position))){
            holder.quizTitle.setText("You already done this quiz.  Your score: " + quizScore.get(doneQuiz.indexOf(quizHelpers.get(position))));
            holder.imageView.setImageResource(R.drawable.baseline_lock_24);
        }else{
            holder.quizTitle.setText("Click here to start "+quizHelpers.get(position));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(recyclerViewQuizInterface!=null && position!=RecyclerView.NO_POSITION){
                        recyclerViewQuizInterface.onQuizClick(position);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return quizHelpers.size();
    }
    public static class myViewHolder extends RecyclerView.ViewHolder{
        private ImageView imageView;
        private TextView quizTitle;

        public myViewHolder(@NonNull View itemView, RecyclerViewQuizInterface recyclerViewQuizInterface) {
            super(itemView);
            imageView = itemView.findViewById(R.id.quizimage);
            quizTitle = itemView.findViewById(R.id.title);
        }
    }
}
