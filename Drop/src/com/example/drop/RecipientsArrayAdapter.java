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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

public class RecipientsArrayAdapter extends ArrayAdapter<JSONObject> implements SectionIndexer, Filterable {
	  private final Context context;
	  private ArrayList<JSONObject> values;
	  private ArrayList<JSONObject> listItems;
	  HashMap<String, Integer> alphaIndexer;
      String[] sections;
	  private Activity parentActivity;

	  public RecipientsArrayAdapter(Context context, ArrayList<JSONObject> listItems, Activity parent) {
	    super(context, R.layout.recipients_listview_item, listItems);
	    this.context = context;
	    this.values = listItems;
	    this.listItems = listItems;
	    parentActivity = parent;
	    
	    alphaIndexer = new HashMap<String, Integer>();
        // in this hashmap we will store here the positions for the sections

        int size = values.size();
        for (int i = size - 1; i >= 0; i--) {
                String element;
				try {
					element = values.get(i).getString("name");
	                alphaIndexer.put(element.substring(0, 1), i);
	                values.get(i).put("orig_id", i);
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
	  public int getCount(){
		  return values.size();
	  }
	  
	  @Override
	  public View getView(int position, View convertView, ViewGroup parent) {
	    LayoutInflater inflater = (LayoutInflater) context
	        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    View rowView = inflater.inflate(R.layout.recipients_listview_item, parent, false);
	    TextView textView = (TextView) rowView.findViewById(R.id.recipients_name);
	    final ImageView icon = (ImageView) rowView.findViewById(R.id.recipients_icon);
	    final RelativeLayout recipientRow = (RelativeLayout) rowView.findViewById(R.id.recipient_list_row);
	    final int loc = position;
	    Log.d("ArrayAdapter", loc + "/" + values.size());
	    if (values.size() > loc) try {
			boolean isSelected = values.get(loc).getBoolean("checked");
			if (isSelected){
				recipientRow.setSelected(true);
				recipientRow.setActivated(true);
				icon.setVisibility(View.VISIBLE);
			}
			else{
				recipientRow.setSelected(false);
				recipientRow.setActivated(false);
				icon.setVisibility(View.INVISIBLE);
			}
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	    recipientRow.invalidate();
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
					icon.setVisibility(View.VISIBLE);
					recipientRow.setSelected(true);
					recipientRow.setActivated(true);
					try {
						values.get(loc).put("checked",true);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else
				{
					icon.setVisibility(View.INVISIBLE);
					recipientRow.setSelected(false);
					recipientRow.setActivated(false);
					try {
						values.get(loc).put("checked",false);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				Log.i("RECIPIENTS", "IS SELECTED: " + recipientRow.isSelected());
				
				((SelectRecipients)parentActivity).updateSelection();
			}
	    	
	    });
	    
	    try {
	    	if (position < values.size())
	    		textView.setText(values.get(position).getString("name").toString());
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
	@Override
    public Filter getFilter() {

        Filter filter = new android.widget.Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                values = (ArrayList<JSONObject>) results.values;
                notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                FilterResults results = new FilterResults();
                ArrayList<JSONObject> FilteredArrayNames = new ArrayList<JSONObject>();

                // perform your search here using the searchConstraint String.

                constraint = constraint.toString().toLowerCase();
                for (int i = 0; i < listItems.size(); i++) {
                    String dataNames;
					try {
						JSONObject user = listItems.get(i);
						dataNames = listItems.get(i).getString("name").toString();
						if (dataNames.toLowerCase().startsWith(constraint.toString()))  {
	                        FilteredArrayNames.add(user);
	                    }
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                    
                }

                results.count = FilteredArrayNames.size();
                results.values = FilteredArrayNames;

                return results;
            }
        };

        return filter;
    }
} 
