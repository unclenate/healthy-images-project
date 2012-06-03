package org.codeforhealth.hip.android;

import org.codeforhealth.hip.android.R;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Debug;
import android.text.Spannable;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author Roy Cham
 * 
 * Copyright 2010 City of Portland. All rights reserved.
 *
 */
public class LocationSearch extends Activity implements LocationListener{
	private LocationManager myManager;
	private TextView tv;

	private String progressMsg;
	
	private double accuracyThreshold;
	
	private double gpsLatitude;
	private double gpsLongitude = 0;
	private double gpsAccuracy = 0;
	
	private int sampleCount;
	
	private boolean isValidGPSLocation = false;
	
    Runnable gpsRunner = new Runnable() {
    	
    	public void run() {
    		
    		try {
    			boolean continueSearching = true;
    			int retryAttempts = 1;
    			
    			while(continueSearching) {
    				if(!isValidGPSLocation) {
	            		if(retryAttempts >= sampleCount) {
	            			// Set flag to redirect to maps
	            			continueSearching = false;
	            		} 
	            		retryAttempts++;
	        		} else {
	        			// Found a valid GPS location
	        			continueSearching = false;
	        		}
    				Thread.sleep(1000);
    			}    	 
    			stopListening();
    			
    			Intent returnIntent = new Intent();
            	returnIntent.putExtra("GPS_FINDER_LATITUDE", gpsLatitude);
            	returnIntent.putExtra("GPS_FINDER_LONGITUDE", gpsLongitude);
            	returnIntent.putExtra("GPS_ACCURACY", gpsAccuracy);
            	returnIntent.putExtra("IS_VALID_GPS", isValidGPSLocation);
            	setResult(RESULT_OK, returnIntent);
            	LocationSearch.this.finish();
    			
    			//gpsHandler.post(mUpdateGPSResults);
    		} catch (Exception e) {
    			Log.e("Error in GPS thread", e.toString()); 
    		}
    		
    	}
    };
    
    private Thread mThread = new Thread(gpsRunner);
	
	/************************************************************************** 
	 * View overrides below 
	 **************************************************************************/
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.locationsearch);
		
		Intent gpsIntent = getIntent();
	    accuracyThreshold = gpsIntent.getDoubleExtra("METERS", 10);
	    sampleCount = gpsIntent.getIntExtra("SAMPLECOUNT", 15);
	    
		
		progressMsg = getString(R.string.gpsProgressMsg);
		
		ImageView iv=(ImageView)findViewById(R.id.imgProgressWheel);
		Animation a = new RotateAnimation(0.0f, 25.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		
		//Animation a=new RotateAnimation(0, 360);

		a.setRepeatCount(Animation.INFINITE);
		a.setDuration(100);
		// maybe other configuration as needed

		iv.startAnimation(a);
		
		// get a handle to our TextView so we can write to it later
		tv = (TextView) findViewById(R.id.gpsFinderMessage);
		tv.setText(progressMsg + "\n\n");
						
		
		HttpTest(LocationSearch.this);
		mThread.start();
		startListening();
	}
	
	 /*** check network*/
	  public static void HttpTest(final Activity mActivity)
	  {
	          if( !isNetworkAvailable( mActivity) )
	          {
	                  
	                  AlertDialog.Builder builders = new AlertDialog.Builder(mActivity);
	                  builders.setTitle("Sorry, no network available");
	                  
	                  
	                  LayoutInflater _inflater = LayoutInflater.from(mActivity);
	                  View convertView = _inflater.inflate(R.layout.custom_dialog, null);
	          
	                  builders.setView(convertView);
	                  builders.setPositiveButton("Ok",  new DialogInterface.OnClickListener(){
	    
		                  public void onClick(DialogInterface dialog, int which)
		                  {
		                          mActivity.finish();
		                  }       
	                  });
	                  
	                  builders.show();
	                  
	          }
	 }   
	    
	  public static boolean isNetworkAvailable( Activity mActivity ) 
	  { 
	          Context context = mActivity.getApplicationContext();
	          ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	          
	          if (connectivity == null) {   
	        	  return false;
	          } 
	          else 
	          {  
	        	  
	        	  NetworkInfo[] info = connectivity.getAllNetworkInfo();   
	        	  
	        	  if (info != null) {   
	        		  
	        		  for (int i = 0; i <info.length; i++) { 
	        			  if (info[i].getState() == NetworkInfo.State.CONNECTED) {
	        				  return true; 
	                      }  
	        	  	   }
	        	  } 
		          	
		          return false;
	  		  }
	}
	  
	  @Override
	  public void onBackPressed() {
			
	    	LocationSearch.this.finish();
	  }

	@Override
	protected void onDestroy() {
		stopListening();
				
		super.onDestroy();
	}
	
	@Override
	protected void onPause() {
		stopListening();
		
		super.onPause();
		
		LocationSearch.this.finish();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}



	/**************************************************************************
	 *  helper functions for starting/stopping monitoring of GPS changes below 
	 **************************************************************************/
	private void startListening() {
		
		myManager = (LocationManager) getSystemService(LOCATION_SERVICE);	

	    
		try {
			
			
			//List<String> providers = myManager.getProviders(true);
			//for (String provider : providers) {
			//	Log.d("PROVIDER - ", provider.toString());
			//}
			
			myManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
		} catch (Exception ex) {
			Log.e("ERROR LOCATION", ex.toString());
		}
	}

	private void stopListening() {
		if (myManager != null)
			myManager.removeUpdates(this);

	}



	/**************************************************************************
	 * LocationListener overrides below 
	 **************************************************************************/
	public void onLocationChanged(Location location) {

		
		// we got new location info. lets display it in the textview
		String s = "";
		s += "Time: "        + location.getTime() + "\n";
		s += "\tLatitude:  " + location.getLatitude()  + "\n";
		s += "\tLongitude: " + location.getLongitude() + "\n";
		s += "\tAccuracy:  " + location.getAccuracy()  + "\n";
		
		tv.setText(progressMsg + "\n\n(currently +/- " + location.getAccuracy() + " meters)");
		
		if((location.getAccuracy() != -1) && (location.getAccuracy() <= accuracyThreshold)) {
				
//			gpsLatitude = location.getLatitude();
//    		gpsLongitude = location.getLongitude();
    		gpsAccuracy = location.getAccuracy();
    		
    		isValidGPSLocation = true;
    		
    	}

		gpsLatitude = location.getLatitude();
		gpsLongitude = location.getLongitude();


	}

	public void onProviderDisabled(String provider) {}

	public void onProviderEnabled(String provider) {}

	public void onStatusChanged(String provider, int status, Bundle extras) {}
}