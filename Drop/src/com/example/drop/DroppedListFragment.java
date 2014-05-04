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
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class DroppedListFragment extends Fragment {

	private ProgressDialog progress;
	private ListView list;
	private final int LAZY_NUM = 8;
	private ArrayList<File> files;
    static boolean updateDropped = false;
	
	static Fragment init() {
		DroppedListFragment droppedFrag = new DroppedListFragment();
        Bundle args = new Bundle();
        droppedFrag.setArguments(args);
        return droppedFrag;
    }
	
	@Override
	  public View onCreateView(LayoutInflater inflater, ViewGroup container,
	      Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		View layout = inflater.inflate(
				R.layout.activity_dropped_list_screen, container, false);
        
        list = (ListView) layout.findViewById(R.id.droppped_list_view_list);
        return layout;
	}
	
	@Override
	public void onResume()
	{
		super.onResume();
		if(updateDropped)
		{
			updateList();
			updateDropped = false;
		}
	}
	
	@Override
	public void onPause()
	{
		super.onPause();
		System.gc();
	}
	
	public void updateList()
	{
		new LoadDroppedNotesAsyncTask().execute();
	}

	private class LoadDroppedNotesAsyncTask extends
			AsyncTask<Void, Void, ArrayList<Note>> {

		@Override
		protected void onPreExecute() {
			progress = new ProgressDialog(getActivity());
			progress.setTitle("Loading");
			progress.setMessage("Wait while loading...");
			progress.show();
		}

		@Override
		protected ArrayList<Note> doInBackground(Void... params) {
			Log.i("DROPPED_LIST_ASYNC", "DO IN BACKGROUND");
			File directory = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+Drop.DROPPED_NOTE_DIR);
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
			
			for (int i = 0; i < LAZY_NUM; i++) {
				if(i > files.size()-1)
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
	        DroppedListViewAdapter adapter = new DroppedListViewAdapter(getActivity(), result);
	        list.setAdapter(adapter);
	    }

	}
}
