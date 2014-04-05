package com.example.drop;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

public class SavedListScreen extends OptionsMenuScreen {

	public static final String GRID_ITEM_POS = "GRID_ITEM_POS";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_list_screen);
        
        final GridView gridview = (GridView) findViewById(R.id.saved_list_gridview);
        gridview.setAdapter(new SavedListAdapter(this));    
      

        gridview.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {                
            	Intent launchGallery = new Intent(getApplicationContext(), GalleryScreen.class);
            	launchGallery.putExtra(GRID_ITEM_POS, position);
            	startActivity(launchGallery);
            }
        });
    }

   
   
}
