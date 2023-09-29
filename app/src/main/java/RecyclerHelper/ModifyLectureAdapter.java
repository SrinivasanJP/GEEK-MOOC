package RecyclerHelper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.Srini.geek_mooc.R;;

import java.util.ArrayList;

import Backend.LectureHelper;

public class ModifyLectureAdapter extends RecyclerView.Adapter<ModifyLectureAdapter.MyViewHolder>{
    private final RecyclerModifyInterface recyclerModifyInterface;
    Context context;
    ArrayList<LectureHelper> lectures;

    public ModifyLectureAdapter(RecyclerModifyInterface recyclerModifyInterface, Context context, ArrayList<LectureHelper> lectures) {
        this.recyclerModifyInterface = recyclerModifyInterface;
        this.context = context;
        this.lectures = lectures;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_modifylist,parent,false);
        return new MyViewHolder(view, recyclerModifyInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull ModifyLectureAdapter.MyViewHolder holder, int position) {

        LectureHelper lectureHelper = lectures.get(position);
        holder.vTitle.setText(lectureHelper.getTitle());
        holder.vNo.setText(lectureHelper.getNoLecture()+"");
    }

    @Override
    public int getItemCount() {
        return lectures.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView vTitle,vNo;
        ImageView vEditBtn;
        public MyViewHolder(@NonNull View itemView, RecyclerModifyInterface recyclerModifyInterface) {
            super(itemView);
            vTitle = itemView.findViewById(R.id.videoTitle);
            vNo = itemView.findViewById(R.id.lectureNo);
            vEditBtn = itemView.findViewById(R.id.edit);
            vEditBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(recyclerModifyInterface != null){
                        int pos = getAbsoluteAdapterPosition();
                        if(pos != RecyclerView.NO_POSITION){
                            recyclerModifyInterface.onModifyClick(pos);
                        }
                    }
                }
            });
        }
    }
}
