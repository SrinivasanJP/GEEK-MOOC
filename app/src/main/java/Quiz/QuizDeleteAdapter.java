package Quiz;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.geek_mooc.R;

import java.util.ArrayList;

public class QuizDeleteAdapter extends RecyclerView.Adapter<QuizDeleteAdapter.myViewHolder> {
    private Context context;
    private RecyclerViewQuizDeleteInterface recyclerViewQuizDeleteInterface;
    private ArrayList<String> quizTitle;

    public QuizDeleteAdapter(Context context, RecyclerViewQuizDeleteInterface recyclerViewQuizDeleteInterface, ArrayList<String> quizTitle) {
        this.context = context;
        this.recyclerViewQuizDeleteInterface = recyclerViewQuizDeleteInterface;
        this.quizTitle = quizTitle;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new myViewHolder(LayoutInflater.from(context).inflate(R.layout.recycler_quiz_delete,parent, false),recyclerViewQuizDeleteInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        holder.title.setText(quizTitle.get(position));
    }

    @Override
    public int getItemCount() {
        return quizTitle.size();
    }

    public static class myViewHolder extends RecyclerView.ViewHolder{
        private TextView title;
        private ImageView delete;

        public myViewHolder(@NonNull View itemView, RecyclerViewQuizDeleteInterface recyclerViewQuizDeleteInterface) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            delete = itemView.findViewById(R.id.delete);
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(recyclerViewQuizDeleteInterface!=null && getAbsoluteAdapterPosition() != RecyclerView.NO_POSITION){
                        recyclerViewQuizDeleteInterface.deleteQuiz(getAbsoluteAdapterPosition());
                    }
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(recyclerViewQuizDeleteInterface!=null && getAbsoluteAdapterPosition() != RecyclerView.NO_POSITION){
                        recyclerViewQuizDeleteInterface.onQuizClick(getAbsoluteAdapterPosition());
                    }
                }
            });
        }
    }
}
