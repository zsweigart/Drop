package com.example.drop;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class DropNoteScreen extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drop_note_screen);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_drop_note_screen, menu);
        return true;
    }
}
