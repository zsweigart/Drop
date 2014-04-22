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
	private final int LAZY_NUM = 8;
	private ArrayList<File> files;
	private int top;
	private int bottom;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		View layout = getLayoutInflater().inflate(
				R.layout.activity_dropped_list_screen, null);
		FrameLayout frame = (FrameLayout) findViewById(R.id.content_frame);
		frame.addView(layout);
		
		new LoadDroppedNotesAsyncTask().execute();
        
        list = (ListView) findViewById(R.id.droppped_list_view_list);
        /*list.setOnScrollListener(new OnScrollListener(){

			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				final int lastItem = firstVisibleItem + visibleItemCount;
		        
				if(files != null && totalItemCount > 0)
				{
					Log.i("DROPPED", "ON SCROLL " + firstVisibleItem + "  " + top + "   "+ lastItem + "   "+ bottom);
					if(firstVisibleItem == 0) //Scrolled to top
					{
						if(top > 0)
						{
							top--;
							bottom--;
							Log.i("DROPPED_LIST", "SCROLLED UP " + top +"   " + bottom);
							((DroppedListViewAdapter)list.getAdapter()).remove(LAZY_NUM-1);
							Note n = loadNote(top);
							((DroppedListViewAdapter)list.getAdapter()).addAtFront(n);
							((DroppedListViewAdapter)list.getAdapter()).notifyDataSetChanged();
						}
					}
					if(lastItem == totalItemCount-1) //Scrolled to bottom
					{
						if(bottom < files.size()-1)
						{
							top++;
							bottom++;
							Log.i("DROPPED_LIST", "SCROLLED DOWN " + top +"   " + bottom);
							((DroppedListViewAdapter)list.getAdapter()).remove(0);
							Note n = loadNote(bottom);
							((DroppedListViewAdapter)list.getAdapter()).addAtFront(n);
							((DroppedListViewAdapter)list.getAdapter()).notifyDataSetChanged();
						}
					}
				}
				
			}

			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				
			}
        	
        });*/

	}

	private class LoadDroppedNotesAsyncTask extends
			AsyncTask<Void, Void, ArrayList<Note>> {

		@Override
		protected void onPreExecute() {
			progress = new ProgressDialog(DroppedListScreen.this);
			progress.setTitle("Loading");
			progress.setMessage("Wait while loading...");
			progress.show();
		}

		@Override
		protected ArrayList<Note> doInBackground(Void... params) {
			Log.i("DROPPED_LIST_ASYNC", "DO IN BACKGROUND");
			File directory = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Android/data/com.example.drop/notes");
			files = new ArrayList<File>();
			ArrayList<Note> notes = new ArrayList<Note> ();

			Log.i("DROPPED_LIST_ASYNC", directory.getAbsolutePath());
			File [] fList = directory.listFiles();

			if(fList != null)
			{
				for (int i = fList.length-1; i >= 0; i--) {
					if (fList[i].isFile()) {
						files.add(fList[i]);
					}
				}
			}
			
			Log.i("DROPPED_LIST", "FILES SIZE = " +files.size());
			
			top = 0;
			bottom = LAZY_NUM - 1;
			
			for (int i = 0; i < LAZY_NUM; i++) {
				if(i < 0)
				{
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

			return notes;
		}
		
		@Override
	    protected void onPostExecute(ArrayList<Note> result)
	    {
	        super.onPostExecute(result);
	        Log.i("DROPPED_LIST", "ON POST EXECUTE "+result.size());
	        progress.dismiss();
	        DroppedListViewAdapter adapter = new DroppedListViewAdapter(DroppedListScreen.this, result);
	        list.setAdapter(adapter);
	    }

	}
	
	private Note loadNote(int loc)
	{
		Note n = null;
		try {
			FileInputStream fin = new FileInputStream(files.get(loc)
					.getAbsolutePath());
			ObjectInputStream ois = new ObjectInputStream(fin);
			n = (Note)ois.readObject();
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
		return n;
	}
}
