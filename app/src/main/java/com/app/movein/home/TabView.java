package com.app.movein.home;
 
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

import com.app.movein.R;
 
public class TabView extends TabActivity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tabviewhome);
         
        TabHost tabHost = getTabHost();
         
        // Tab for Photos
        TabSpec photospec = tabHost.newTabSpec("Photos");
        // setting Title and Icon for the Tab
        photospec.setIndicator("Photos", getResources().getDrawable(R.drawable.ic_launcher));
        Intent photosIntent = new Intent(this, SignInFragment.class);
        photospec.setContent(photosIntent);
         
        // Tab for Songs
        TabSpec songspec = tabHost.newTabSpec("Songs");       
        songspec.setIndicator("Songs", getResources().getDrawable(R.drawable.ic_launcher));
        Intent songsIntent = new Intent(this, SignUpFragment.class);
        songspec.setContent(songsIntent);
         
        // Tab for Videos
       /* TabSpec videospec = tabHost.newTabSpec("Videos");
        videospec.setIndicator("Videos", getResources().getDrawable(R.drawable.icon_videos_tab));
        Intent videosIntent = new Intent(this, VideosActivity.class);
        videospec.setContent(videosIntent);
         */
        // Adding all TabSpec to TabHost
        tabHost.addTab(photospec); // Adding photos tab
        tabHost.addTab(songspec); // Adding songs tab
        // Adding videos tab
    }
}