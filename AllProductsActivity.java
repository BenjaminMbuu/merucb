package com.ncpb;

import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ncpb.Enter_order_details.CreateNewProduct;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

public class AllProductsActivity extends Activity {

	String data = "";
	TableLayout tl;
	TableRow tr;
	TextView label;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		tl = (TableLayout) findViewById(R.id.maintable);

		Button send = (Button) findViewById(R.id.send);
		
		send.setOnClickListener(new View.OnClickListener() {
			 
            @Override
            public void onClick(View view) {
                // creating new product in background thread
            	Intent i=new Intent(AllProductsActivity.this, Enter_order_details.class);
                startActivity(i);
            }
        });
		final GetDataFromDB getdb = new GetDataFromDB();
		new Thread(new Runnable() {
			public void run() {
				data = getdb.getDataFromDB();
				System.out.println(data);
				
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						ArrayList<Users> users = parseJSON(data);
						addData(users);						
					}
				});
				
			}
		}).start();
		
	}

	public ArrayList<Users> parseJSON(String result) {
		ArrayList<Users> users = new ArrayList<Users>();
		try {
			JSONArray jArray = new JSONArray(result);
			for (int i = 0; i < jArray.length(); i++) {
				JSONObject json_data = jArray.getJSONObject(i);
				Users user = new Users();
				user.setId(json_data.getInt("id"));
				user.setName(json_data.getString("name"));
				user.setPlace(json_data.getString("place"));
				//user.setTown(json_data.getString("town"));
				users.add(user);
			}
		} catch (JSONException e) {
			Log.e("log_tag", "Error parsing data " + e.toString());  
		}
		return users;
	}

	void addHeader(){
		/** Create a TableRow dynamically **/
		tr = new TableRow(this);

		/** Creating a TextView to add to the row **/
		label = new TextView(this);
		label.setText("PRODUCT");
		label.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));
		label.setPadding(5, 5, 5, 5);
		label.setBackgroundColor(Color.RED);
		LinearLayout Ll = new LinearLayout(this);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT);
		params.setMargins(5, 5, 5, 5);
		//Ll.setPadding(10, 5, 5, 5);
		Ll.addView(label,params);
		tr.addView((View)Ll); // Adding textView to tablerow.

		/** Creating Qty Button **/
		TextView place = new TextView(this);
		place.setText("QUANTITY AVAILABLE");
		place.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));
		place.setPadding(5, 5, 5, 5);
		place.setBackgroundColor(Color.RED);
		Ll = new LinearLayout(this);
		params = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT);
		params.setMargins(0, 5, 5, 5);
		//Ll.setPadding(10, 5, 5, 5);
		Ll.addView(place,params);
		tr.addView((View)Ll); // Adding textview to tablerow.

		tl.addView(tr, new TableLayout.LayoutParams(
                LayoutParams.FILL_PARENT,
                LayoutParams.WRAP_CONTENT));
		/** Creating Qty Button **/
	}
	
	@SuppressWarnings({ "rawtypes" })
	public void addData(ArrayList<Users> users) {

		addHeader();
		
		for (Iterator i = users.iterator(); i.hasNext();) {

			Users p = (Users) i.next();

			/** Create a TableRow dynamically **/
			tr = new TableRow(this);

			/** Creating a TextView to add to the row **/
			label = new TextView(this);
			label.setText(p.getName());
			label.setId(p.getId());
			label.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT));
			label.setPadding(5, 5, 5, 5);
			label.setBackgroundColor(Color.GRAY);
			LinearLayout Ll = new LinearLayout(this);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,
					LayoutParams.WRAP_CONTENT);
			params.setMargins(5, 2, 2, 2);
			//Ll.setPadding(10, 5, 5, 5);
			Ll.addView(label,params);
			tr.addView((View)Ll); // Adding textView to tablerow.

			/** Creating Qty Button **/
			TextView place = new TextView(this);
			place.setText(p.getPlace());
			place.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT));
			place.setPadding(5, 5, 5, 5);
			place.setBackgroundColor(Color.GRAY);
			Ll = new LinearLayout(this);
			params = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,
					LayoutParams.WRAP_CONTENT);
			params.setMargins(0, 2, 2, 2);
			//Ll.setPadding(10, 5, 5, 5);
			Ll.addView(place,params);
			tr.addView((View)Ll); // Adding textview to tablerow.
			
			tl.addView(tr, new TableLayout.LayoutParams(
                    LayoutParams.FILL_PARENT,
                    LayoutParams.WRAP_CONTENT));
			
		}
	}
}
