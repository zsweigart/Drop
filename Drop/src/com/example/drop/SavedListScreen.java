package com.example.drop;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

public class SavedListScreen extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_list_screen);
        
        GridView gridview = (GridView) findViewById(R.id.saved_list_gridview);
        gridview.setAdapter(new SavedListAdapter(this));

        gridview.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Toast.makeText(SavedListScreen.this, "" + position, Toast.LENGTH_SHORT).show();
            }
        });
    }

   
   
}
