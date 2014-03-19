package com.example.drop;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class Gallery extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_gallery, menu);
        return true;
    }
}
