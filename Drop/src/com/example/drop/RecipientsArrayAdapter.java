package com.example.drop;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

public class RecipientsArrayAdapter extends ArrayAdapter<JSONObject> {
	  private final Context context;
	  private final ArrayList<JSONObject> values;
	  CheckBox checkBoxView;

	  public RecipientsArrayAdapter(Context context, ArrayList<JSONObject> listItems) {
	    super(context, R.layout.recipients_listview_item, listItems);
	    this.context = context;
	    this.values = listItems;
	  }

	  @Override
	  public View getView(int position, View convertView, ViewGroup parent) {
	    LayoutInflater inflater = (LayoutInflater) context
	        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    View rowView = inflater.inflate(R.layout.recipients_listview_item, parent, false);
	    TextView textView = (TextView) rowView.findViewById(R.id.recipients_name);
	    checkBoxView = (CheckBox) rowView.findViewById(R.id.recipients_checked);
	    final int loc = position;
	    checkBoxView.setOnCheckedChangeListener(new OnCheckedChangeListener(){
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if(isChecked)
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
} 
