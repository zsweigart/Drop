package com.example.drop;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

public class DroppedListScreen extends DrawerActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View layout =  getLayoutInflater().inflate(R.layout.activity_dropped_list_screen, null);
        FrameLayout frame = (FrameLayout) findViewById(R.id.content_frame);
        frame.addView(layout);
    }

   
}
