package com.example.drop;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

public class RecipientsArrayAdapter extends ArrayAdapter<JSONObject> implements SectionIndexer {
	  private final Context context;
	  private final ArrayList<JSONObject> values;
	  HashMap<String, Integer> alphaIndexer;
      String[] sections;
	  RelativeLayout recipientRow;
	  private Activity parentActivity;

	  public RecipientsArrayAdapter(Context context, ArrayList<JSONObject> listItems, Activity parent) {
	    super(context, R.layout.recipients_listview_item, listItems);
	    this.context = context;
	    this.values = listItems;
	    parentActivity = parent;
	    
	    alphaIndexer = new HashMap<String, Integer>();
        // in this hashmap we will store here the positions for the sections

        int size = values.size();
        for (int i = size - 1; i >= 0; i--) {
                String element;
				try {
					element = values.get(i).getString("name");
	                alphaIndexer.put(element.substring(0, 1), i);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        //We store the first letter of the word, and its index.
        //The Hashmap will replace the value for identical keys are putted in
        }

        // now we have an hashmap containing for each first-letter
        // sections(key), the index(value) in where this sections begins

        // we have now to build the sections(letters to be displayed)
        // array .it must contains the keys, and must (I do so...) be
        // ordered alphabetically

        Set<String> keys = alphaIndexer.keySet(); // set of letters ...sets
        // cannot be sorted...

        Iterator<String> it = keys.iterator();
        ArrayList<String> keyList = new ArrayList<String>(); // list can be
        // sorted

        while (it.hasNext()) {
                String key = it.next();
                keyList.add(key);
        }

        Collections.sort(keyList);

        sections = new String[keyList.size()]; // simple conversion to an
        // array of object
        keyList.toArray(sections);
	  }

	  @Override
	  public View getView(int position, View convertView, ViewGroup parent) {
	    LayoutInflater inflater = (LayoutInflater) context
	        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    View rowView = inflater.inflate(R.layout.recipients_listview_item, parent, false);
	    TextView textView = (TextView) rowView.findViewById(R.id.recipients_name);
	    recipientRow = (RelativeLayout) rowView.findViewById(R.id.recipient_list_row);
	    final int loc = position;
	    recipientRow.setOnClickListener(new OnClickListener(){
			public void onClick(View arg0) {
				boolean isSelected = false;
				try {
					isSelected = values.get(loc).getBoolean("checked");
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				if(!isSelected)
				{
					try {
						values.get(loc).put("checked",true);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else
				{
					try {
						values.get(loc).put("checked",false);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				((SelectRecipients)parentActivity).updateSelection();
			}
	    	
	    });
	    
	    try {
			textView.setText(values.get(position).get("name").toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    return rowView;
	  }

	public int getPositionForSection(int section) {
		String letter = sections[section];
		 
        return alphaIndexer.get(letter);
	}

	public int getSectionForPosition(int position) {
		return 0;
	}

	public Object[] getSections() {
		 return sections;
	}
} 
