package RecyclerHelper;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.example.geek_mooc.R;
import java.util.ArrayList;
import Backend.LectureHelper;

public class LectureHolder extends  RecyclerView.Adapter<LectureHolder.MyViewHolder> {

    private final RecyclerViewInterface recyclerViewInterface;
    Context context;
    ArrayList<LectureHelper> lectures;

    public LectureHolder(RecyclerViewInterface recyclerViewInterface, Context context, ArrayList<LectureHelper> lectures) {
        this.recyclerViewInterface = recyclerViewInterface;
        this.context = context;
        this.lectures = lectures;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_lecture,parent,false);
        return new MyViewHolder(view, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        LectureHelper lectureHelper = lectures.get(position);
        holder.vTitle.setText(lectureHelper.getTitle());
        holder.vNoLecture.setText(lectureHelper.getNoLecture()+"");

    }



    @Override
    public int getItemCount() {
        return lectures.size();
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView vTitle, vNoLecture;
        Button vGetNotes;
        public MyViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            vTitle = itemView.findViewById(R.id.videoTitle);
            vNoLecture = itemView.findViewById(R.id.noLecture);
            vGetNotes = itemView.findViewById(R.id.getNotes);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(recyclerViewInterface != null){
                        int pos = getAbsoluteAdapterPosition();
                        if(pos != RecyclerView.NO_POSITION){
                            recyclerViewInterface.onCourseClick(pos);
                        }
                    }
                }
            });
            vGetNotes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(recyclerViewInterface != null){
                        int pos = getAbsoluteAdapterPosition();
                        if(pos != RecyclerView.NO_POSITION){
                            recyclerViewInterface.onClickNotesBtn(pos);
                        }
                    }
                }
            });
        }
    }
}
