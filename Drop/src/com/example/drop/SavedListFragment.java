package com.example.drop;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class SavedListFragment extends Fragment {

	public static final String GRID_ITEM_POS = "GRID_ITEM_POS";
	private GridView gridview;
	private View layout;
	
	static Fragment init() {
		SavedListFragment savedFrag = new SavedListFragment();
        Bundle args = new Bundle();
        savedFrag.setArguments(args);
        return savedFrag;
    }
	
	@Override
	  public View onCreateView(LayoutInflater inflater, ViewGroup container,
	      Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        layout =  inflater.inflate(R.layout.activity_saved_list_screen, container, false);
        
        return layout;
    }
	
	@Override
	public void onResume()
	{
		super.onResume();
		gridview = (GridView) layout.findViewById(R.id.saved_list_gridview);
		gridview.setAdapter(new SavedListAdapter(getActivity()));    

        gridview.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) { 
            	//When you click an item in the grid, grab the position and send it on to the Gallery
            	Intent launchGallery = new Intent(getActivity(), GalleryScreen.class);
            	launchGallery.putExtra(GRID_ITEM_POS, position);
            	startActivity(launchGallery);
            }
        });
	}
	
	@Override
	public void onPause()
	{
		super.onPause();
		gridview = null;
		System.gc();
	}

   
   
}
