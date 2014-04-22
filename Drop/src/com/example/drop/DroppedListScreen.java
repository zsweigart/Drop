package com.example.drop;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OptionalDataException;
import java.util.ArrayList;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

public class DroppedListScreen extends DrawerActivity {

	private ProgressDialog progress;
	private ListView list;
	private ArrayList<Note> notes;
	private final int LAZY_NUM = 10;
	private int preLast;
	private int loc;
    private boolean loading;
    private int previousTotal;
    private int totalFiles;
    private int currentItems; 
    private int topItem;
    private int bottomItem;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		View layout = getLayoutInflater().inflate(
				R.layout.activity_dropped_list_screen, null);
		FrameLayout frame = (FrameLayout) findViewById(R.id.content_frame);
		frame.addView(layout);
		
		previousTotal = 0;
		currentItems = 0;
		totalFiles = -1;
		topItem = -1;
		bottomItem = -1;

		notes = new ArrayList<Note>();
		
        loc = -1;
		new LoadDroppedNotesAsyncTask().execute();
        
        list = (ListView) findViewById(R.id.droppped_list_view_list);
        list.setOnScrollListener(new OnScrollListener(){

			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				final int lastItem = firstVisibleItem + visibleItemCount;
				if(topItem < totalFiles - firstVisibleItem)
				{
					((DroppedListViewAdapter)list.getAdapter()).remove(topItem);
				}
				if(bottomItem > totalFiles - lastItem)
				{
					((DroppedListViewAdapter)list.getAdapter()).remove(bottomItem);
				}
				topItem = totalFiles - firstVisibleItem;
				bottomItem = totalFiles - lastItem;
				if (loading) {
		            if (totalItemCount > previousTotal) {
		                loading = false;
		                previousTotal = totalItemCount;
		                currentItems = totalItemCount;
		            }
		        }
		        if ((!loading && hasMoreData()) && (lastItem == totalItemCount)) {
		              if(preLast!=lastItem){ //to avoid multiple calls for last item
		                Log.d("Last", "Last");
		                preLast = lastItem;
		        		new LoadDroppedNotesAsyncTask().execute();
		              }
		           }
				
			}

			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				
			}
        	
        });

	}

	private class LoadDroppedNotesAsyncTask extends
			AsyncTask<Void, Void, ArrayList<Note>> {

		@Override
		protected void onPreExecute() {
			loading = true;
			progress = new ProgressDialog(DroppedListScreen.this);
			progress.setTitle("Loading");
			progress.setMessage("Wait while loading...");
			progress.show();
		}

		@Override
		protected ArrayList<Note> doInBackground(Void... params) {
			Log.i("DROPPED_LIST_ASYNC", "DO IN BACKGROUND");
			File directory = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Android/data/com.example.drop/notes");
			ArrayList<File> files = new ArrayList<File>();

			Log.i("DROPPED_LIST_ASYNC", directory.getAbsolutePath());
			File[] fList = directory.listFiles();

			if(fList != null)
			{
				for (File file : fList) {
					if (file.isFile()) {
						files.add(file);
					}
				}
			}
			
			if(totalFiles == -1)
			{
				totalFiles = files.size()-1;
			}
			
			if(loc == -1)
			{
				loc = files.size()-1;
			}
			
			for (int i = loc; i > loc-LAZY_NUM; i--) {
				if(i <= 0)
				{
					loc = 0;
					break;
				}
				
				try {
					FileInputStream fin = new FileInputStream(files.get(i)
							.getAbsolutePath());
					ObjectInputStream ois = new ObjectInputStream(fin);
					notes.add((Note) ois.readObject());
					ois.close();
				} catch (OptionalDataException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			loc -= LAZY_NUM;

			return notes;
		}
		
		@Override
	    protected void onPostExecute(ArrayList<Note> result)
	    {
	        super.onPostExecute(result);
	        loading = false;
	        progress.dismiss();
	        DroppedListViewAdapter adapter = new DroppedListViewAdapter(DroppedListScreen.this, result);
	        list.setAdapter(adapter);
	    }

	}

	private boolean hasMoreData()
	{
		if(totalFiles > currentItems)
		{
			return true;
		}
		return false;
	}
}
