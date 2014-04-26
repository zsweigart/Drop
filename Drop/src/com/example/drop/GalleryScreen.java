package com.example.drop;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;


public class GalleryScreen extends FragmentActivity  {
	//private static final String TAG = "GALLERY";
	GalleryPageAdapter pageAdapter;
	ViewPager pager;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_gallery);
	    
	    List<Fragment> fragments = getFragments();
        
        pageAdapter = new GalleryPageAdapter(getSupportFragmentManager(), fragments);
        
        pager = (ViewPager)findViewById(R.id.galleryViewPager);
        pager.setAdapter(pageAdapter);
        
	}
	
	/** Called when the activity resumes **/
	@Override
	public void onResume(){
		super.onResume();
		int pos = 0;
		//This is where we get the position passed from the SavedListScreen 
		if(this.getIntent().getExtras() != null)
		{
			pos = this.getIntent().getExtras().getInt(SavedListFragment.GRID_ITEM_POS);
		}
		//And make the ViewPager item in the same position the current item
		pager.setCurrentItem(pos);
	}
		
	private List<Fragment> getFragments(){
    	List<Fragment> fList = new ArrayList<Fragment>();       	

    	fList.add(GalleryViewFragment.newInstance(R.drawable.sample_1, "This is a picture for the first page. It's page #1."));
    	fList.add(GalleryViewFragment.newInstance(R.drawable.sample_2, "This is a picture for the second page. It's page #2."));
    	fList.add(GalleryViewFragment.newInstance(R.drawable.sample_3, "This is a picture for the third page. It's page #3."));
    	fList.add(GalleryViewFragment.newInstance(R.drawable.sample_4, "This is a picture for the third page. It's page #4."));
    	fList.add(GalleryViewFragment.newInstance(R.drawable.sample_5, "This is a picture for the third page. It's page #5."));
    	fList.add(GalleryViewFragment.newInstance(R.drawable.sample_6, "This is a picture for the third page. It's page #6."));
    	fList.add(GalleryViewFragment.newInstance(R.drawable.sample_7, "This is a picture for the third page. It's page #7."));
    	fList.add(GalleryViewFragment.newInstance(R.drawable.sample_8, "This is a picture for the third page. It's page #8."));
    	
    	return fList;
    }

}