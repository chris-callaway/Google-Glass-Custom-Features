package com.example.glass_test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.hardware.Camera.Parameters;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;
 
public class CameraActivity extends Activity {

//Original Implementation
	
//private Preview mPreview;
//private RelativeLayout mLayout;

//    @Override
//	protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        
//        // Hide the window title.
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        
//        // Create our Preview view and set it as the content of our activity.
//        mPreview = new Preview(this);
//        
//        mLayout = new RelativeLayout(this);
//        
//        //Set text
//        final TextView tv1 = new TextView(this);
//        tv1.setText("Welcome"); 
//        tv1.setId(5);  
//        mLayout.addView(tv1); 
//         
//        mLayout.addView(mPreview, 0); 
//         
//        setContentView(mLayout); 
//       
//        
//    }

//New Implementation
    
	private Preview mPreview;
	private RelativeLayout mLayout;
	private String postalCode;
	TextToSpeech tts;
    TextView tv1;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Hide the window title.
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        //Keep screen on without dim
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        
        // Create our Preview view and set it as the content of our activity.
        mPreview = new Preview(this);
        
        mLayout = new RelativeLayout(this);
         
        //Add camera to view
        mLayout.addView(mPreview, 0); 
        
        //Initialize view
        setContentView(mLayout); 
        
        //Get location
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        LocationManager mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        String provider = mLocationManager.getBestProvider(criteria, true);
        boolean isEnabled = mLocationManager.isProviderEnabled(provider);
        if (isEnabled) {
        // Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {

        // Called when a new location is found by the network location provider.
        if (location != null) {
        Geocoder geocoder = new Geocoder(CameraActivity.this.getBaseContext(), Locale.getDefault());
        // lat,lng, your current location
        List<Address> addresses = null;

        try {
        addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
        }
        catch (IOException e) {
        System.out.println(e);
        e.printStackTrace();
        }

        if (addresses != null && addresses.size() != 0) {
        // This is how you will get Postal Code
        postalCode = addresses.get(0).getPostalCode();
        
        //Send curl request to API
        new HttpAsyncTask().execute("http://02d791c.netsolhost.com/glassAPI/weatherAPI/index.php?zip=" + postalCode); 

        }

        }

        }

        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }

        };

        // Register the listener with the Location Manager to receive location updates
        mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 0, locationListener);
        }
        
        //Send curl request to API
//        new HttpAsyncTask().execute("http://02d791c.netsolhost.com//glassAPI/index.php?zip=" + postalCode); 
    }  
 
    public static String GET(String url){
        InputStream inputStream = null;
        String result = "";
        try {
 
            // create HttpClient
            HttpClient httpclient = new DefaultHttpClient();
 
            // make GET request to the given URL
            HttpResponse httpResponse = httpclient.execute(new HttpGet(url));
 
            // receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();
 
            // convert inputstream to string
            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";
 
        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }
 
        return result;
    }
 
    private static String convertInputStreamToString(InputStream inputStream) throws IOException{
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;
 
        inputStream.close();
        return result;
 
    }
 
    public boolean isConnected(){
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) 
                return true;
            else
                return false;    
    }
    
    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
 
            return GET(urls[0]);
        }
        
        // onPostExecute displays the results of the AsyncTask.
        
        @Override
        protected void onPostExecute(String result) {
          Toast.makeText(getBaseContext(), "Connected!", Toast.LENGTH_LONG).show();
            
          //Set text
            TextView tv1 = new TextView(CameraActivity.this); 
            tv1.setText(result); 
            mLayout.addView(tv1);     
             
       }
    }
    
    @Override
	public boolean onKeyDown(int keycode, KeyEvent event) {
		//Main Click
		if (keycode == KeyEvent.KEYCODE_DPAD_CENTER) {
			//User tapped touchpad, do something
			
            return true;
		}
		//Camera click
		if (keycode == KeyEvent.KEYCODE_CAMERA) {

			return true;
		}
		//Swipe down
		if (keycode == KeyEvent.KEYCODE_BACK) {
			//User swiped down, do something
			finish();
			Intent newView = new Intent(CameraActivity.this, ScrollingCards.class);
            startActivity(newView); 
			//makeCard("You swiped down");
			return true;
		}
		
		return false;
	}
	
}
