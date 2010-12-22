package gis.cityreports.android;

import java.util.List;
import java.util.prefs.Preferences;

import org.apache.commons.lang.StringUtils;

import gis.cityreports.data.ConfigSetting;
import gis.cityreports.data.ReportInfo;
import android.app.Application;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * @author Roy Cham
 * 
 * Copyright 2010 City of Portland. All rights reserved.
 *
 */

public class ApplicationState extends Application {

	private static boolean showDisclaimerMsg = true;
	
	private static ApplicationState singleton;
	private static ConfigSetting config = new ConfigSetting();
	private static ReportInfo reportInfo = new ReportInfo();
	
	//private LocationManager gpsLocationManager;
		
	private static SharedPreferences mPrefs;
		
	//ToDo in future
	//private static List<ReportInfo> failedReports;
		
	public static final String PREF_CATEGORY_ID 			= "PREF_CATEGORY_ID";
	public static final String PREF_ADDRESS_DESC 			= "PREF_ADDRESS_DESC";
	public static final String PREF_DESCRIPTION 			= "PREF_DESCRIPTION";
	public static final String PREF_DEVICE_ID 				= "PREF_DEVICE_ID";
	public static final String PREF_IMAGE_ABSOLUTE_PATH 	= "PREF_IMAGE_ABSOLUTE_PATH";
	public static final String PREF_IMAGE_FULL_URI_PATH 	= "PREF_IMAGE_FULL_URI_PATH";
	public static final String PREF_IMAGE_NAME 				= "PREF_IMAGE_NAME";
	public static final String PREF_LATITUDE 				= "PREF_LATITUDE";
	public static final String PREF_LONGITUDE 				= "PREF_LONGITUDE";
	public static final String PREF_IMAGE_WIDTH 			= "PREF_IMAGE_WIDTH";
	public static final String PREF_IMAGE_HEIGHT			= "PREF_IMAGE_HEIGHT";
	public static final String PREF_IMAGE_URI				= "PREF_IMAGE_URI";
	public static final String PREF_INSTANCE_ID				= "PREF_INSTANCE_ID";
	public static final String PREF_CONTACT_NAME			= "PREF_CONTACT_NAME";
	public static final String PREF_CONTACT_EMAIL			= "PREF_CONTACT_EMAIL";
	public static final String PREF_CONTACT_PHONE			= "PREF_CONTACT_PHONE";
	public static final String PREF_CONTACT_FLAG			= "PREF_CONTACT_FLAG";
	public static final String PREF_CONTACT_TIMESTAMP		= "PREF_CONTACT_TIMESTAMP";
	public static final String PREF_VALIDGPSLOCATION_FLAG	= "PREF_VALIDGPSLOCATION_FLAG";
	
	
	private static double currentLatitude = 0;
	private static double currentLongitude = 0;
	
	private static Uri intentShareUri;
					
	/**
	 * @return the intentShareUri
	 */
	public static Uri getIntentShareUri() {
		return intentShareUri;
	}

	/**
	 * @param set the intentShareUri
	 */
	public static void setIntentShareUri(Uri intentShareUri) {
		ApplicationState.intentShareUri = intentShareUri;
	}

	/**
	 * @return the currentLatitude
	 */
	public static double getCurrentLatitude() {
		return currentLatitude;
	}

	/**
	 * @return the currentLongitude
	 */
	public static double getCurrentLongitude() {
		return currentLongitude;
	}
	

	public static ApplicationState getInstance() {
		return singleton;
	}
		
		public static boolean showDisclaimer() {
			return showDisclaimerMsg;
		}
		
		public static String getComments() {
			return mPrefs.getString(PREF_DESCRIPTION, "");
		}
		
		public static String getContactName() {
			return mPrefs.getString(PREF_CONTACT_NAME, "");
		}
		
		public static String getContactPhone() {
			return mPrefs.getString(PREF_CONTACT_PHONE, "");
		}
		
		public static String getContactEmail() {
			return mPrefs.getString(PREF_CONTACT_EMAIL, "");
		}
				
		
		public ReportInfo getReportInfoState(){
		    return reportInfo;
		}
		
		public ConfigSetting getConfigSetting() {
			return config;
		}
		
		public static void disclaimerStatus(boolean status) {
			showDisclaimerMsg = status;
		}
		
		public void clearReportInfoState() {
			
			if (reportInfo != null)
				reportInfo.clear();
		}
		
		
		@Override
		public final void onCreate() {
			super.onCreate();
			
			mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
			
			resetDefaultConfigSetting();
			
			//startLocationTracker();
			
			singleton = this;
		}
		
		@Override
		public void onTerminate () {
			super.onTerminate();
		}
		
		/*
		public void startLocationTracker() {
			
			if (gpsLocationManager == null)
				gpsLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
			
			if (gpsLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
				startListening();
			}
		}
		*/
		
		public ConfigSetting resetDefaultConfigSetting() {
			if (config != null) {
				config.setDisclaimerFrequency(getString(R.string.defaultDisclaimerFrequency));
				config.setDisclaimerText(getString(R.string.defaultDisclaimerText));
				config.setGpsAccuracyThreshold(getString(R.string.defaultGpsAccuracyThreshold));
				config.setGpsSampleCount(getString(R.string.defaultGpsSampleCount));
				config.setGpsSampleInterval(getString(R.string.defaultGpsSampleInterval));
				config.setHelpPage(getString(R.string.defaultHelpPage));
				config.setPhotoCompression(getString(R.string.defaultPhotoCompression));
				config.setPhotoMaxPixels(getString(R.string.defaultPhotoMaxPixels));
				config.setMaxRetryAttempts(Integer.parseInt(getString(R.string.defaultMaxRetryAttempts)));
				config.setAndroidGpsSampleCount(Integer.parseInt(getString(R.string.defaultAndroidGpsSampleCount)));
				
				config.setSampleSizeExtra(Integer.parseInt(getString(R.string.defaultSampleSizeExtra)));
				config.setSampleSizeLarge(Integer.parseInt(getString(R.string.defaultSampleSizeLarge)));
				config.setSampleSizeMedium(Integer.parseInt(getString(R.string.defaultSampleSizeMedium)));
				config.setSampleSizeSmall(Integer.parseInt(getString(R.string.defaultSampleSizeSmall)));
				
				config.setAndroidTimeout(Integer.parseInt(getString(R.string.defaultAndroidTimeout)));
				config.setAndroidCategoryRefresh(Integer.parseInt(getString(R.string.categoryRefreshInterval)));
			}
			
			return config;
		}
		
		
		public static void setupDefaults() {
			
	        
	        reportInfo.setCategoryId(mPrefs.getString(PREF_CATEGORY_ID, ""));
			reportInfo.setAddressDescription(mPrefs.getString(PREF_ADDRESS_DESC, ""));
			reportInfo.setDescription(mPrefs.getString(PREF_DESCRIPTION, ""));
			reportInfo.setDeviceId(mPrefs.getString(PREF_DEVICE_ID, ""));
			reportInfo.setImageAbsolutePath(mPrefs.getString(PREF_IMAGE_ABSOLUTE_PATH, ""));
			reportInfo.setImageFullUriPath(mPrefs.getString(PREF_IMAGE_FULL_URI_PATH, ""));
			reportInfo.setImageName(mPrefs.getString(PREF_IMAGE_NAME, ""));
			reportInfo.setValidGPSLocation(mPrefs.getBoolean(PREF_VALIDGPSLOCATION_FLAG, false));
			
			try {
				String latTest = mPrefs.getString(PREF_LATITUDE, "0.0");
				Double latDTest = Double.parseDouble(latTest);
				
				String lonTest = mPrefs.getString(PREF_LONGITUDE, "0.0");
				Double longDTest = Double.parseDouble(lonTest);
				
				reportInfo.setLatitude(latDTest);
				reportInfo.setLongitude(longDTest);
				
			} catch (Exception ex) {
				Log.e("CASTING ERROR", ex.toString());
			}
			
			reportInfo.setImageWidth(mPrefs.getInt(PREF_IMAGE_WIDTH, 0));
			reportInfo.setImageHeight(mPrefs.getInt(PREF_IMAGE_HEIGHT, 0));
			
			//reportInfo.setImageUri(prefs.(PREF_IMAGE_URI, null));
			reportInfo.setInstanceId(mPrefs.getInt(PREF_INSTANCE_ID, 0));
			
			reportInfo.setContactEmail(mPrefs.getString(PREF_CONTACT_EMAIL, ""));
			reportInfo.setContactName(mPrefs.getString(PREF_CONTACT_NAME, ""));
			reportInfo.setContactPhone(mPrefs.getString(PREF_CONTACT_PHONE, ""));
			
			reportInfo.setHasContact(mPrefs.getBoolean(PREF_CONTACT_FLAG, false));
		}
		
		
		public static void savePreferences() 
		{
	        Editor editor = mPrefs.edit();
	        
	        editor.putString(PREF_CATEGORY_ID, reportInfo.getCategoryId());
		    editor.putString(PREF_ADDRESS_DESC, reportInfo.getAddressDescription());
		    editor.putString(PREF_DESCRIPTION, reportInfo.getDescription());
		    editor.putString(PREF_DEVICE_ID, reportInfo.getDeviceId());
		    editor.putString(PREF_IMAGE_ABSOLUTE_PATH, reportInfo.getImageAbsolutePath());
		    editor.putString(PREF_IMAGE_FULL_URI_PATH, reportInfo.getImageFullUriPath());
		    editor.putString(PREF_IMAGE_NAME, reportInfo.getImageName());
		    
		    editor.putString(PREF_LATITUDE, Double.toString(reportInfo.getLatitude()));
		    editor.putString(PREF_LONGITUDE, Double.toString(reportInfo.getLongitude()));
		    
		    editor.putInt(PREF_IMAGE_WIDTH, reportInfo.getImageWidth());
		    editor.putInt(PREF_IMAGE_HEIGHT, reportInfo.getImageHeight());
		    editor.putInt(PREF_INSTANCE_ID, reportInfo.getInstanceId());
		    
		    editor.putBoolean(PREF_VALIDGPSLOCATION_FLAG, reportInfo.isValidGPSLocation());
	        
	        editor.commit();
	    }
		
		public static void saveContactInfo()
		{    		
			Editor editor = mPrefs.edit();
			editor.putString(PREF_CONTACT_NAME, reportInfo.getContactName());
		    editor.putString(PREF_CONTACT_EMAIL, reportInfo.getContactEmail());
		    editor.putString(PREF_CONTACT_PHONE, reportInfo.getContactPhone());
		    
		    if(StringUtils.isBlank(reportInfo.getContactName()) 
    				&& StringUtils.isBlank(reportInfo.getContactEmail()) 
    				&& StringUtils.isBlank(reportInfo.getContactPhone()) ) {
    			
    			editor.putBoolean(PREF_CONTACT_FLAG, false);
    			reportInfo.setHasContact(false);
    		} else {
    			editor.putBoolean(PREF_CONTACT_FLAG, true);
    			reportInfo.setHasContact(true);
    		}
		    editor.commit();
		}
		
		public static void SaveComments() 
		{
			Editor editor = mPrefs.edit();
			editor.putString(PREF_DESCRIPTION, reportInfo.getDescription());
			editor.commit();
			
		}
		
		public static long getContactAlertTimestamp() {
			return mPrefs.getLong(PREF_CONTACT_TIMESTAMP, 0);
		}
		
		public static void setContactAlertTimestamp(long timediff)
		{
			Editor editor = mPrefs.edit();
			editor.putLong(PREF_CONTACT_TIMESTAMP, timediff);
			editor.commit();
		}
		
		/**************************************************************************
		 *  helper functions for starting/stopping monitoring of GPS changes below 
		 **************************************************************************/
		
		/*
		public void startListening() {
			gpsLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
		}

		public void stopListening() {
			if (gpsLocationManager != null)
				gpsLocationManager.removeUpdates(locationListener);
		}
		*/

		/**************************************************************************
		 * LocationListener overrides below 
		 **************************************************************************/
		/*
		private static String locationDetail;
		
		private final LocationListener locationListener = new LocationListener() {
		    public void onLocationChanged(Location location) {
		    	// we got new location info. lets display it in the textview
				locationDetail = "";
				locationDetail += "Time: "        + location.getTime() + "\n";
				locationDetail += "\tLatitude:  " + location.getLatitude()  + "\n";
				locationDetail += "\tLongitude: " + location.getLongitude() + "\n";
				locationDetail += "\tAccuracy:  " + location.getAccuracy()  + "\n";
				
				Log.d("PDX GPS", locationDetail);
				
				currentLatitude = location.getLatitude();
				currentLongitude = location.getLongitude();
		    }
				 
		    public void onProviderDisabled(String provider){ }
		    public void onProviderEnabled(String provider){ }
		    public void onStatusChanged(String provider, int status, Bundle extras){ }
		};
		*/	
		

}
