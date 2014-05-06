package com.example.drop;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

/*
 * An effort to format text strings consistently
 */


public class TextFormatter {
	
	static String receivers(ArrayList<String> receivers){
		StringBuilder sb = new StringBuilder();
		for (String user : receivers) {
		
			try {
				JSONObject obj = new JSONObject(user);
				//Append first and last initial to receiver list				
				String fullName = obj.getString("name");							
				String[] firstAndLast = fullName.split("\\s+");				
				sb.append(firstAndLast[0]);//Add first name
				if(firstAndLast.length > 1) // If there's a last name, add the initial and a period
				{
					sb.append(" "+firstAndLast[1].substring(0, 1)+"., ");					
				}			
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return sb.deleteCharAt(sb.length()-2).toString();
	}

}
