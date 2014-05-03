package com.example.drop;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class DroppedListViewAdapter extends BaseAdapter {
	 private Activity activity;
	    private ArrayList <Note> data;
	    private static LayoutInflater inflater=null;
	    public ImageLoader imageLoader; 
	    private Note note;
	    
	    public DroppedListViewAdapter(Activity a, ArrayList <Note> d) {
	        activity = a;
	        data=d;
	        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	        imageLoader=new ImageLoader(activity.getApplicationContext());
	    }

	    public int getCount() {
	        return data.size();
	    }

	    public Note getItem(int position) {
	        return data.get(position);
	    }

	    public long getItemId(int position) {
	        return position;
	    }
	    
	    public View getView(final int position, View convertView, ViewGroup parent) {
	    	final Note note;
	    	View rowView = convertView;
	        if(convertView==null)
	        {
	        	rowView = inflater.inflate(R.layout.dropped_listview_item, null);
	        }
	    ImageView picture = (ImageView) rowView.findViewById(R.id.dropped_row_imageView);
	    TextView noteText = (TextView) rowView.findViewById(R.id.dropped_row_note_content);
	    TextView recipients = (TextView) rowView.findViewById(R.id.dropped_row_recievers);
	    
	    note = data.get(position);
	    
	    Log.i("DROP_ADAPTER", note.getPictureFile().getAbsolutePath());
	    picture.setImageBitmap(note.getPicture(picture.getHeight(), picture.getWidth()));
        
	    noteText.setText(note.getMessage());
        String recievers = "";
        ArrayList <String> r = note.getReceivers();
        for(int i = 0; i < r.size(); i++)
        {
        	try {
				JSONObject obj = new JSONObject(r.get(i));
				recievers += obj.getString("name") +", ";
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        recipients.setText(recievers.substring(0, recievers.length()-3));
        
        RelativeLayout row = (RelativeLayout) rowView.findViewById(R.id.dropped_row);
        
        row.setOnClickListener(new OnClickListener(){

			public void onClick(View arg0) {
				Log.d("DroppedListViewAdapter", "Clicked on position "+position);
				Intent i = new Intent(activity, ViewNoteScreen.class);
				i.putExtra("com.example.drop.Note", note);
				i.putExtra("wasFound", false);
				activity.startActivity(i);
			}
        	
        });
	    	    
	    return rowView;
	  }
}
