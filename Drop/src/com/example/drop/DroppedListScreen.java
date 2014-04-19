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
import android.widget.ListView;

public class DroppedListScreen extends DrawerActivity {

	private ProgressDialog progress;
	private ListView list;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dropped_list_screen);
        
        list = (ListView) findViewById(R.id.droppped_list_view_list);

		LoadDroppedNotesAsyncTask loadDropped = new LoadDroppedNotesAsyncTask();
		loadDropped.execute();

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
			ArrayList<File> files = new ArrayList<File>();
			ArrayList<Note> notes = new ArrayList<Note>();

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

			for (int i = 0; i < files.size(); i++) {
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
	        progress.dismiss();
	        DroppedListViewAdapter adapter = new DroppedListViewAdapter(DroppedListScreen.this, result);
	        list.setAdapter(adapter);
	    }

	}

}
