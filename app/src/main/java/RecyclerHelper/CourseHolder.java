package RecyclerHelper;


import android.app.Application;
import android.content.Context;
import android.media.browse.MediaBrowser;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.media3.common.MediaItem;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.PlayerView;
import androidx.recyclerview.widget.RecyclerView;

import com.Srini.geek_mooc.R;

import java.util.ArrayList;

import Backend.CreateCourseHelper;


public class CourseHolder extends RecyclerView.Adapter<CourseHolder.MyViewHolder> {

    private final RecyclerViewInterface recyclerViewInterface;
    Context context;
    MediaItem mediaItem;
    ExoPlayer player;
    Boolean completed;

    ArrayList<CreateCourseHelper> courses;

    public CourseHolder(Context context, ArrayList<CreateCourseHelper> courses, RecyclerViewInterface recyclerViewInterface,Boolean completed) {
        this.context = context;
        this.courses = courses;
        this.recyclerViewInterface = recyclerViewInterface;
        this.completed = completed;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.recycler_courselist,parent,false);
        return new MyViewHolder(view, recyclerViewInterface, completed);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        CreateCourseHelper createCourseHelper = courses.get(position);
        holder.vTitle.setText(createCourseHelper.getTitle());
        holder.vDes.setText(createCourseHelper.getDescription());
        Log.d("debug", createCourseHelper.getTitle()+createCourseHelper.getIntrolink());
        player = new ExoPlayer.Builder(context).build();
        mediaItem = MediaItem.fromUri(Uri.parse(createCourseHelper.getIntrolink()));
        holder.playerView.setPlayer(player);
        player.setMediaItem(mediaItem);
        player.prepare();
//        player.play();

    }

    @Override
    public int getItemCount() {
        return courses.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        PlayerView playerView;
        TextView vTitle, vDes;

        public MyViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface, Boolean completed) {
            super(itemView);
            vTitle = itemView.findViewById(R.id.videoTitle);
            vDes = itemView.findViewById(R.id.videoDescription);
            playerView = itemView.findViewById(R.id.thumbPlayer);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(recyclerViewInterface != null){
                        int pos = getAbsoluteAdapterPosition();
                        if(completed){
                            recyclerViewInterface.onCompletedCourseClick(pos);
                        }
                        else if(pos!=RecyclerView.NO_POSITION){
                            recyclerViewInterface.onCourseClick(pos);
                        }
                    }
                }
            });


        }
    }
}
