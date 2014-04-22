package com.example.drop;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

public class DroppedListViewAdapter extends ArrayAdapter<Note>{
	private final Context context;
	  private final ArrayList<Note> values;
	  CheckBox checkBoxView;

	  public DroppedListViewAdapter(Context context, ArrayList<Note> listItems) {
	    super(context, R.layout.recipients_listview_item, listItems);
	    this.context = context;
	    this.values = listItems;
	  }

	  @Override
	  public View getView(int position, View convertView, ViewGroup parent) {
	    LayoutInflater inflater = (LayoutInflater) context
	        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    View rowView = inflater.inflate(R.layout.dropped_listview_item, parent, false);
	    ImageView picture = (ImageView) rowView.findViewById(R.id.dropped_row_imageView);
	    TextView noteText = (TextView) rowView.findViewById(R.id.dropped_row_note_content);
	    TextView recipients = (TextView) rowView.findViewById(R.id.dropped_row_recievers);
	    
	    Note note = values.get(position);
	    
	    Log.i("DROP_ADAPTER", note.picture.getAbsolutePath());
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
	    	    
	    return rowView;
	  }
	  
	  public void remove(int position){
		    values.remove(values.get(position));
		  	System.gc();
		}
}
