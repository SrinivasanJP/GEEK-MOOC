package RecyclerHelper;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.geek_mooc.R;

import java.util.ArrayList;

import Backend.ratings;

public class RatingsAdapter extends RecyclerView.Adapter<RatingsAdapter.myViewHolder> {
    Context context;
    ArrayList<ratings> ratings;

    public RatingsAdapter(Context context, ArrayList<Backend.ratings> ratings) {
        this.context = context;
        this.ratings = ratings;
    }

    @NonNull
    @Override
    public RatingsAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new myViewHolder(LayoutInflater.from(context).inflate(R.layout.recycler_comments,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RatingsAdapter.myViewHolder holder, int position) {
        holder.vName.setText(ratings.get(position).getName());
        holder.vRatings.setText("Rating: "+ratings.get(position).getRating());
        holder.vComments.setText("Comment: \n\t"+ratings.get(position).getComments());
    }

    @Override
    public int getItemCount() {
        return ratings.size();
    }
    public static class myViewHolder extends RecyclerView.ViewHolder{
        TextView vName,vRatings,vComments;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            vName = itemView.findViewById(R.id.name);
            vRatings = itemView.findViewById(R.id.ratings);
            vComments = itemView.findViewById(R.id.comments);
        }
    }
}
