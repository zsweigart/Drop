package com.example.drop;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.model.GraphUser;
import com.parse.ParseFacebookUtils;

public class SelectRecipients extends  Activity {
    private ArrayList<String> listItems;
    private ListView list;
    private ArrayList <JSONObject> userlist;
    private RecipientsArrayAdapter adapter;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_recipients);
        list = (ListView)findViewById(R.id.recipients_list);
        list.setFastScrollEnabled(true);
        listItems=new ArrayList<String>();
        userlist=new ArrayList<JSONObject>();
        makeMyFriendsRequest();
    }
   
    private void makeMyFriendsRequest() {
        Request request = Request.newMyFriendsRequest(ParseFacebookUtils.getSession(),
                new Request.GraphUserListCallback() {
                    public void onCompleted(List<GraphUser> users, Response response) {
                        if (users != null && users.size() > 0) { 
                        	Log.i("RECIPIENTS", users.size()+"");
                        	for(int i = 0; i < users.size(); i++)
                        	{
                        		GraphUser user = users.get(i);
	                            // Create a JSON object to hold the profile info
	                            JSONObject userProfile = new JSONObject();
	                            try {                   
	                                // Populate the JSON object 
	                                userProfile.put("facebookId", user.getId());
	                                userProfile.put("name", user.getName());
	                                if (user.getProperty("name") != null) {
	                                    userProfile.put("name", (String) user
	                                            .getProperty("name"));    
	                                }                           
	                                if (user.getProperty("gender") != null) {
	                                    userProfile.put("gender",       
	                                            (String) user.getProperty("gender"));   
	                                }                           
	                                if (user.getBirthday() != null) {
	                                    userProfile.put("birthday",     
	                                            user.getBirthday());                    
	                                }                           
	                                if (user.getProperty("relationship_status") != null) {
	                                    userProfile                     
	                                        .put("relationship_status",                 
	                                            (String) user                                          
	                                                .getProperty("relationship_status"));                               
	                                }   
	                                userProfile.put("checked", false);
	                                // Now add the data to the UI elements
	                                // ...
	                                addUserToList(userProfile);
	        
	                            } catch (JSONException e) {
	                                Log.d("RECIPIENTS",
	                                        "Error parsing returned user data.");
	                            } catch (NullPointerException ex) {
	                                Log.d("RECIPIENTS",
	                                        "ERROR NULL POINTER.");
	                            }
                        	}
                        	
                        	setAdapter();
        
                        } else if (response.getError() != null) {
                            // handle error
                        }                  
                    }               
                });
        request.executeAsync();
    }
    
    private void addUserToList(JSONObject user)
    {
    	try {
    		boolean added = false;
    		for(int i = 0; i < userlist.size(); i++)
    		{
    			if(((String) user.get("name")).compareTo((String)listItems.get(i)) < 0)
    			{
    				listItems.add(i, (String) user.get("name"));
    				userlist.add(i, user);
    				added = true;
    				break;
    			}
    		}
    		if(!added)
    		{
    			listItems.add((String) user.get("name"));
				userlist.add(user);
    		}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    private void setAdapter()
    {
    	adapter = new RecipientsArrayAdapter(getApplicationContext(), userlist);
    	list.setAdapter(adapter);
    }

	@Override
    public void finish() {
    	ArrayList <JSONObject> recipients = new ArrayList <JSONObject> ();
    	for(int i = 0; i < adapter.getCount(); i++)
    	{
    	    try {
				if (userlist.get(i).getBoolean("checked")){                                          
				    // Put the value of the id in our list
				    recipients.add(userlist.get(i));                                                       
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
		// Prepare data intent 
		Intent data = new Intent();
		Bundle b = new Bundle();
		b.putInt("numResults", recipients.size());
		for(int i = 0; i < recipients.size(); i++)
		{	
			Log.i("RECIPIENTS", "recipient"+i+"  :  "+recipients.get(i).toString());
			b.putString("recipient"+i, recipients.get(i).toString());
		}
		data.putExtras(b);
		// Activity finished ok, return the data
		setResult(RESULT_OK, data);
		super.finish();
    } 
}
