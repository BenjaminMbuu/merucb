package com.ncpb;

import android.app.Activity;
import android.os.Bundle;
import java.util.ArrayList;
import java.util.List;
 
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
 
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SendProductsDetails extends Activity {
	// Progress Dialog
    private ProgressDialog pDialog;
 
    JSONParser jsonParser = new JSONParser();
    EditText inputName;
    EditText inputIdNo;
    EditText inputLocation;
    EditText inputPhoneNo;
    EditText inputProduct;
    EditText inputQuantity;
    EditText inputPrice;
    // url to create new product
    private static String url_create_product = "http://10.0.2.2/tester/productDetails.php";
 
    // JSON Node names
    private static final String TAG_SUCCESS = "success";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_product_details);
        
        inputName = (EditText) findViewById(R.id.InputName);
        inputIdNo = (EditText) findViewById(R.id.InputIdNo);
        inputLocation = (EditText) findViewById(R.id.InputLocation);
        inputPhoneNo = (EditText) findViewById(R.id.InputPhoneNo);
        inputProduct = (EditText) findViewById(R.id.InputProduct);
        inputQuantity = (EditText) findViewById(R.id.InputQuantity);
        inputPrice = (EditText) findViewById(R.id.InputPrice);
        
        Button send = (Button) findViewById(R.id.send);
        
        // button click event
        send.setOnClickListener(new View.OnClickListener() {
 
            @Override
            public void onClick(View view) {
                // creating new product in background thread
                new CreateNewProduct().execute();
            }
        });
    }
    
    class CreateNewProduct extends AsyncTask<String, String, String> {
    	 
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(SendProductsDetails.this);
            pDialog.setMessage("Sending Product Details....");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
        
        protected String doInBackground(String... args) {
            String name = inputName.getText().toString();
            String idno = inputIdNo.getText().toString();
            String location = inputLocation.getText().toString();
            String phone = inputPhoneNo.getText().toString();
            String product = inputProduct.getText().toString();
            String quantity = inputQuantity.getText().toString();
            String price = inputPrice.getText().toString();
 
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("Name", name));
            params.add(new BasicNameValuePair("IdNo", idno));
            params.add(new BasicNameValuePair("Location", location));
            params.add(new BasicNameValuePair("PhoneNo", phone));
            params.add(new BasicNameValuePair("ProductName", product));
            params.add(new BasicNameValuePair("AvailableQty", quantity));
            params.add(new BasicNameValuePair("UnitPrice", price));

            // getting JSON Object
            // Note that create product url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(url_create_product,
                    "POST", params);
            
            Log.d("Create Response", json.toString());
            
            try {
                int success = json.getInt(TAG_SUCCESS);
            
                if (success == 1) {
                    // successfully created product
                	Intent i=new Intent(SendProductsDetails.this, AfterSendProdDetails.class);
                    startActivity(i);
 
                    // closing this screen
                 //   finish();              
                    } 
                else {
                    // failed to create product
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
 
            return null;
        }
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
            pDialog.dismiss();
        }
 
    }

}