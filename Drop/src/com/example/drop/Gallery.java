package com.example.drop;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class Gallery extends FragmentActivity {

	GalleryPagerAdapter mGalleryPagerAdapter;
	ViewPager mViewPager;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        
        // ViewPager and its adapters use support library
        // fragments, so use getSupportFragmentManager.
        mGalleryPagerAdapter =
                new GalleryPagerAdapter(
                        getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mGalleryPagerAdapter);
    }
    
	 // Since this is an object collection, use a FragmentStatePagerAdapter,
	 // and NOT a FragmentPagerAdapter.
	 public class GalleryPagerAdapter extends FragmentStatePagerAdapter {
	     public GalleryPagerAdapter(FragmentManager fm) {
	         super(fm);
	     }
	
	     @Override
	     public Fragment getItem(int i) {
	         Fragment fragment = new GalleryNoteFragment();
	         Bundle args = new Bundle();
	         // Our object is just an integer :-P
	         args.putInt(GalleryNoteFragment.ARG_OBJECT, i + 1);
	         fragment.setArguments(args);
	         return fragment;
	     }
	
	     @Override
	     public int getCount() {
	         return 100;
	     }
	
	     @Override
	     public CharSequence getPageTitle(int position) {
	         return "OBJECT " + (position + 1);
	     }
	 }
	 
	// Instances of this class are fragments representing a single
	// object in our collection.
	public static class GalleryNoteFragment extends Fragment {
	    public static final String ARG_OBJECT = "object";

	    @Override
	    public View onCreateView(LayoutInflater inflater,
	            ViewGroup container, Bundle savedInstanceState) {
	        // The last two arguments ensure LayoutParams are inflated
	        // properly.
	        View rootView = inflater.inflate(
	                R.layout.gallery_note_fragment, container, false);
	        Bundle args = getArguments();
	        ((TextView) rootView.findViewById(android.R.id.text1)).setText(
	                Integer.toString(args.getInt(ARG_OBJECT)));
	        return rootView;
	    }
	}

    
}
