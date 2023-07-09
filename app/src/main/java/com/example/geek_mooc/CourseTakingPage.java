package com.example.geek_mooc;

import androidx.annotation.OptIn;
import androidx.appcompat.app.AppCompatActivity;
import androidx.media3.common.MediaItem;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.datasource.DataSource;
import androidx.media3.datasource.DefaultHttpDataSource;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.exoplayer.smoothstreaming.SsMediaSource;
import androidx.media3.exoplayer.source.MediaSource;
import androidx.media3.exoplayer.source.ProgressiveMediaSource;
import androidx.media3.ui.PlayerView;

import android.net.Uri;
import android.os.Bundle;

public class CourseTakingPage extends AppCompatActivity {
    private ExoPlayer player;
    private DataSource.Factory dataSourceFactory;
    private MediaSource mediaSource;
    private PlayerView vCouresPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_taking_page);

        vCouresPlayer = findViewById(R.id.coursePlayer);

        //ssMediaSource Generation
        dataSourceFactory = new DefaultHttpDataSource.Factory();
        //test TODO: clear this code
        String uri = "https://firebasestorage.googleapis.com/v0/b/geekmooc-fe677.appspot.com/o/Courses%2FHS90No1TtESF03P7GXZSlGT20aD3%2FPython_Full_course_for_beginners_2023HS90No%2Flecture%2F0_Test1?alt=media&token=5cbf3cc9-71ee-4ee5-a26f-b2e8d7ebe1dc";
        player = new ExoPlayer.Builder(this).build();
        player.setMediaItem(MediaItem.fromUri(Uri.parse(uri)));
        vCouresPlayer.setPlayer(player);
        player.prepare();


    }
}