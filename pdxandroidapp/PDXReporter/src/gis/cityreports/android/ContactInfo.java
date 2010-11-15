package gis.cityreports.android;

import gis.cityreports.data.ConfigSetting;
import gis.cityreports.data.ReportInfo;

import java.io.File;
import java.io.StringReader;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.provider.Settings.Secure;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnKeyListener;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.commons.lang.StringUtils;
import org.apache.http.client.ResponseHandler;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

/**
 * @author Roy Cham
 * 
 * Copyright 2010 City of Portland. All rights reserved.
 *
 */
public class ContactInfo extends Activity  {

	private ConfigSetting configSetting;
	private ReportInfo reportInfo;
	
	private EditText mContactName;
	private EditText mContactEmail;
	private EditText mContactPhone;
	
	private String currentContactName;
	private String currentContactEmail;
	private String currentContactPhone;
	
	private int retryAttemptsForContact;
	
	private boolean updateContactInfo = false;
	
	private SharedPreferences prefs;
	
	private static String deviceId;
	public static final String REQUEST_CONTACT = "REQUEST_CONTACT";
	
    /************************************************
     *  Handler for contact submittal  
     *  returned from the responseContactHandler
     *************************************************/  
    
    private Handler contactHandler = new Handler() {

        @Override
        public void handleMessage(final Message msg) {
        	
        	String bundleResult = msg.getData().getString("RESPONSE");
            //Log.d("BUNDLE_RESULT", bundleResult);
            
            if(!bundleResult.contains("ERROR_FOUND")) {
            	try {
                    
                	SAXParserFactory spf = SAXParserFactory.newInstance();
                    SAXParser sp = spf.newSAXParser();
                    XMLReader xr = sp.getXMLReader();
                    
                    ContactHandler handler = new ContactHandler();
                	xr.setContentHandler(handler);
    	            xr.parse(new InputSource(new StringReader(bundleResult)));
    	            
    	            if (handler.isUpdated()) {
    	            	
    	            	//Log.d("CONTACT INFO", "Successfully updated contact info.");
    	            	// Now we can update the reportInfo and preference object with new
    	            	// contact info
    	            	
    	            	/*
    	            	Editor editor = prefs.edit();
    	            	
    	            	editor.putString(Report.PREF_CONTACT_NAME, currentContactName);
    	    		    editor.putString(Report.PREF_CONTACT_EMAIL, currentContactEmail);
    	    		    editor.putString(Report.PREF_CONTACT_PHONE, currentContactPhone);
    	    		    */
    	            	
    	            	reportInfo.setContactName(currentContactName);
    	        		reportInfo.setContactEmail(currentContactEmail);
    	        		reportInfo.setContactPhone(currentContactPhone);
    	        		
    	        		ApplicationState.saveContactInfo();
    	        		
    	        		/*
    	        		if(StringUtils.isBlank(currentContactName) 
    	        				&& StringUtils.isBlank(currentContactEmail) 
    	        				&& StringUtils.isBlank(currentContactPhone) ) {
    	        			
    	        			editor.putBoolean(Report.PREF_CONTACT_FLAG, false);
    	        			reportInfo.setHasContact(false);
    	        		} else {
    	        			editor.putBoolean(Report.PREF_CONTACT_FLAG, true);
    	        			reportInfo.setHasContact(true);
    	        		}
    	        		
    	        		editor.commit();  
    	        		*/
    	            }
            	} catch (Exception e) {
            		Log.e(Constants.LOGTAG, "ERROR - " + e.toString());
            	}
            	
            } else {
            	retryAttemptsForContact++;
            	
            	if(retryAttemptsForContact >= configSetting.getMaxRetryAttempts()) {
            		retryAttemptsForContact = 0;
            	} else {
            		httpRequest();
            	}
            	
            }
        }
    };    
    
    private ResponseHandler<String> responseContactHandler = HTTPRequestHelper.getResponseHandlerInstance(contactHandler);
    
    private void httpRequest() {
    	
    	new Thread() {

            @Override
            public void run() {
            	
            	try {
                    HashMap<String, String> postParams = new HashMap<String, String>();
                    
                    HTTPRequestHelper helper = new HTTPRequestHelper(responseContactHandler);
                    
                    postParams.put(getString(R.string.param1), getString(R.string.pval) );
                    
                    //Log.d("Device ID", reportInfo.getDeviceId());
                    
                    postParams.put(getString(R.string.param2), deviceId); 
                    postParams.put(getString(R.string.param6), currentContactName );
                    postParams.put(getString(R.string.param7), currentContactEmail );
                    postParams.put(getString(R.string.param8), currentContactPhone );
                    
                    helper.performPost(getString(R.string.postRequestContact_PROD), "", "", 
                    		Integer.parseInt(getString(R.string.httpTimeOutMilliseconds)), 
                    		Integer.parseInt(getString(R.string.socketTimeoutMilliseconds)), 
                    		null, postParams);
                
            	} catch (Exception e) {
                	Log.e("HTTP RESPONSE ERROR", e.toString());
                } 
            }
		}.start();
    	
    }
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.contact_info);
        
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        
        ApplicationState appState = ((ApplicationState)getApplicationContext());
        
        //prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        
        configSetting = ((ApplicationState)getApplication()).getConfigSetting();
        reportInfo = ((ApplicationState)getApplication()).getReportInfoState();	
        
        deviceId = "";
        
        try {
        	deviceId = Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        	
        	if(!Report.USING_EMULATOR) {
        		StringBuffer deviceInfo = new StringBuffer();
    	        deviceInfo.append(deviceId);
    	        //deviceInfo.append("_");
    	        //deviceInfo.append(Build.BRAND);
    	        deviceInfo.append("_");
    	        deviceInfo.append(Build.MANUFACTURER);
    	        deviceInfo.append("_");
    	        deviceInfo.append(Build.MODEL);
    	        //deviceInfo.append("_");
    	        //deviceInfo.append(Build.DISPLAY);
    		    //Log.d("ANDROID_ID", deviceInfo.toString());
    		    
    		    deviceId = deviceInfo.toString();
    	    }
        	
        } catch (Exception ex) { }
        
        //configSetting = appState.getConfigSetting();    
        //reportInfo = appState.getReportInfoState();
        
        mContactName = (EditText) findViewById(R.id.editContactName);
        mContactEmail = (EditText) findViewById(R.id.editContactEmail);
        mContactPhone = (EditText) findViewById(R.id.editContactPhone);
        
        //mContactPhone.setInputType(EditorInfo.TYPE_CLASS_NUMBER); //
        mContactName.setInputType(EditorInfo.TYPE_TEXT_VARIATION_PERSON_NAME);
        mContactPhone.setInputType(EditorInfo.TYPE_CLASS_PHONE);
        mContactEmail.setInputType(EditorInfo.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        
        currentContactEmail = ApplicationState.getContactEmail(); //prefs.getString(Report.PREF_CONTACT_EMAIL, "");
        currentContactName = ApplicationState.getContactName(); //prefs.getString(Report.PREF_CONTACT_NAME, "");
        currentContactPhone = ApplicationState.getContactPhone(); //prefs.getString(Report.PREF_CONTACT_PHONE, "");
		
		/*
        currentContactName = reportInfo.getContactName();
        currentContactEmail = reportInfo.getContactEmail();
        currentContactPhone = reportInfo.getContactPhone();
        */
        
        mContactName.setText(currentContactName);
        mContactEmail.setText(currentContactEmail);
        mContactPhone.setText(currentContactPhone);
               
    }
    
    @Override
    protected void onSaveInstanceState(Bundle outState) {
    	super.onSaveInstanceState(outState);
    }
    
    @Override
    public void onBackPressed() {

    	String temp;
    	
    	//if (mContactName.getText() != null && StringUtils.isNotBlank(mContactName.getText().toString()) ) {
    	if (mContactName.getText() != null) {
            temp = mContactName.getText().toString().trim();
    		
    		if (currentContactName.compareToIgnoreCase(temp) != 0 ) {
    			currentContactName = temp;
    			updateContactInfo = true;
    		}
    	}
    	
    	if (mContactEmail.getText() != null) {
        	temp = mContactEmail.getText().toString().trim();
   		
    		if (currentContactEmail.compareToIgnoreCase(temp) != 0 ) {
    			currentContactEmail = temp;
    			updateContactInfo = true;
    		}
    	}
    	
    	if (mContactPhone.getText() != null) {
        	temp = mContactPhone.getText().toString().trim();
    		
    		if (currentContactPhone.compareToIgnoreCase(temp) != 0 ) {
    			currentContactPhone = temp;
    			updateContactInfo = true;
    		}
    	}
    	    	
    	if (updateContactInfo) {
    		httpRequest();
    	}
    	
    	ContactInfo.this.finish();
    }

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		//Toast.makeText(getApplicationContext(), "Doing onStop()", Toast.LENGTH_SHORT).show();
	}	
	
	@Override
	protected void onResume() {
		super.onResume();
	}
	
	
	public static boolean isValidEmailAddress(String emailAddress) {
		String emailRegEx;
		Pattern pattern;
		// Regex for a valid email address
		emailRegEx = "^[A-Za-z0-9._%+\\-]+@[A-Za-z0-9.\\-]+\\.[A-Za-z]{2,4}$";
		// Compare the regex with the email address
		pattern = Pattern.compile(emailRegEx);
		Matcher matcher = pattern.matcher(emailAddress);
		if (!matcher.find()) {
			return false;
		}
		return true;
	}
    

}
