package Quiz;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.Srini.geek_mooc.R;;

import java.util.ArrayList;

public class MarkListAdapter extends RecyclerView.Adapter<MarkListAdapter.myViewHolder> {
    Context context;
    ArrayList<Marks> marks;

    public MarkListAdapter(Context context, ArrayList<Marks> marks) {
        this.context = context;
        this.marks = marks;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new myViewHolder(LayoutInflater.from(context).inflate(R.layout.recycler_mark_list,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        holder.name.setText(marks.get(position).getName());
        holder.marks.setText("Marks scored: "+marks.get(position).getMarks()+"");
    }

    @Override
    public int getItemCount() {
        return marks.size();
    }

    public static class myViewHolder extends RecyclerView.ViewHolder{

        private TextView name;
        private TextView marks;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            marks = itemView.findViewById(R.id.marks);

        }
    }
}
