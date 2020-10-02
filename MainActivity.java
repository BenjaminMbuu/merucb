package com.ncpb;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.view.Menu;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;

public class MainActivity extends Activity {
	Button login;
    EditText username,password;
    CheckBox check;
    //TextView tv;
    HttpPost httppost;
    StringBuffer buffer;
    HttpResponse response;
    HttpClient httpclient;
    SharedPreferences app_preferences ;
    List<NameValuePair> nameValuePairs;
    ProgressDialog dialog = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		
        app_preferences = PreferenceManager.getDefaultSharedPreferences(this);
		login = (Button)findViewById(R.id.login); 
        username = (EditText)findViewById(R.id.username);
        password= (EditText)findViewById(R.id.password);
        check = (CheckBox) findViewById(R.id.check);
        
        String Str_user = app_preferences.getString("username","0" );
        String Str_pass = app_preferences.getString("password", "0");
        String Str_check = app_preferences.getString("checked", "no");
        if(Str_check.equals("yes"))
        {
                username.setText(Str_user);
                password.setText(Str_pass);
                check.setChecked(true);
        }
        //tv = (TextView)findViewById(R.id.);
        login.setOnClickListener(new OnClickListener() {
        	@Override
        	public void onClick(View v) {
                dialog = ProgressDialog.show(MainActivity.this, "",
                        "Validating user...", true);
                new Thread(new Runnable() {
                    public void run() {
                        login();                         
                    }
                  }).start();              
        }
    });
}
	void login(){
        try{           
              
            httpclient=new DefaultHttpClient();
            httppost= new HttpPost("http://10.0.2.2/project/login.php"); // make sure the url is correct.
            //add your data
            nameValuePairs = new ArrayList<NameValuePair>(2);
            //
            nameValuePairs.add(new BasicNameValuePair("username",username.getText().toString().trim()));  
            nameValuePairs.add(new BasicNameValuePair("password",password.getText().toString().trim()));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            //Execute HTTP Post Request
            response=httpclient.execute(httppost);
            // edited by James from coderzheaven.. <span class="IL_AD" id="IL_AD6">from here</span>....
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            final String response = httpclient.execute(httppost, responseHandler);
            System.out.println("Response : " + response);
            runOnUiThread(new Runnable() {
                public void run() {
                    //tv.setText("Response from PHP : " + response);
                    dialog.dismiss();
                }
            });
            if(response.equalsIgnoreCase("User Found")){
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(MainActivity.this,"Login Success", Toast.LENGTH_SHORT).show();
                    }
                });
                //startActivity(new Intent(MainActivity.this, UserPage.class));
                Intent i=new Intent(MainActivity.this, UserPage.class);
                startActivity(i);
            }else{
                showAlert();               
            }
        }catch(Exception e){
        dialog.dismiss();
        System.out.println("Exception : " + e.getMessage());
    }
}
        public void showAlert(){
            MainActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Login Error.");
                    builder.setMessage("Invalid Username or Password.") 
                           .setCancelable(false)
                           .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                               public void onClick(DialogInterface dialog, int id) {
                               }
                           });                    
                    AlertDialog alert = builder.create();
                    alert.show();              
                }
            });
        }
    //}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
