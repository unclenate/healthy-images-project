package gis.cityreports.android;

import gis.cityreports.data.ConfigSetting;
import gis.cityreports.data.ReportInfo;

import java.io.StringReader;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings.Secure;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

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
	
	private static final int DIALOG_ERROR_EMAIL_ID = 1;
	private static final int DIALOG_ERROR_PHONE_ID = 2;
	private static final int DIALOG_ERROR_NAME_ID = 3;
	
	
	private static Pattern NorthAmerica = Pattern.compile("^(?:\\+?1[-. ]?)?\\(?([0-9]{3})\\)?[-. ]?([0-9]{3})[-. ]?([0-9]{4})$");
	private static Pattern International = Pattern.compile("^\\+(?:[0-9] ?){6,14}[0-9]$");
	private static Pattern EmailPattern = Pattern.compile("^[A-Za-z0-9._%+\\-]+@[A-Za-z0-9.\\-]+\\.[A-Za-z]{2,4}$");
	
	private static final String PHONE_TYPE_NORTH_AMERICA = "PHONE_TYPE_NORTH_AMERICA";
	private static final String PHONE_TYPE_INTERNATIONAL = "PHONE_TYPE_INTERNATIONAL";
	private static final String PHONE_TYPE_UNDETERMINED = "PHONE_TYPE_UNDETERMINED";
	private static final String PHONE_TYPE_BLANK = "PHONE_TYPE_BLANK";
	
    /************************************************
     *  Handler for contact submittal  
     *  returned from the responseContactHandler
     *************************************************/  
    
    private Handler contactHandler = new Handler() {

        @Override
        public void handleMessage(final Message msg) {
        	
        	String bundleResult = msg.getData().getString("RESPONSE");
            
            if(!bundleResult.contains("ERROR_FOUND")) {
            	try {
                    
                	SAXParserFactory spf = SAXParserFactory.newInstance();
                    SAXParser sp = spf.newSAXParser();
                    XMLReader xr = sp.getXMLReader();
                    
                    ContactHandler handler = new ContactHandler();
                	xr.setContentHandler(handler);
    	            xr.parse(new InputSource(new StringReader(bundleResult)));
    	            
    	            if (handler.isUpdated()) {

    	            	reportInfo.setContactName(currentContactName);
    	        		reportInfo.setContactEmail(currentContactEmail);
    	        		reportInfo.setContactPhone(currentContactPhone);
    	        		
    	        		//ApplicationState.saveContactInfo();
    	        		
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
                    postParams.put(getString(R.string.param2), deviceId);
                    postParams.put(getString(R.string.param6), currentContactName );
                    postParams.put(getString(R.string.param7), currentContactEmail );
                    postParams.put(getString(R.string.param8), currentContactPhone );
                    
                    helper.performPost(getString(R.string.postRequestContact_PROD), "", "", 
                    		Integer.parseInt(getString(R.string.httpTimeOutMilliseconds)), 
                    		Integer.parseInt(getString(R.string.socketTimeoutMilliseconds)), 
                    		null, postParams);
                
            	} catch (Exception e) {
                	//Log.e("HTTP RESPONSE ERROR", e.toString());
                } 
            }
		}.start();
    	
    }
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		int width = display.getWidth();
		int height = display.getHeight();
		
		if (width >= 1024 && height >= 800) {
			setContentView(R.layout.contact_info_tablet);
		} else if (width >= 800) {
			setContentView(R.layout.contact_info_800);
		} else {
			setContentView(R.layout.contact_info);
		}
		        
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        
        ApplicationState appState = ((ApplicationState)getApplicationContext());
                
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
                
        mContactName = (EditText) findViewById(R.id.editContactName);
        mContactEmail = (EditText) findViewById(R.id.editContactEmail);
        mContactPhone = (EditText) findViewById(R.id.editContactPhone);
        
        mContactPhone.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        
        //mContactPhone.setInputType(EditorInfo.TYPE_CLASS_NUMBER); //
        mContactName.setInputType(EditorInfo.TYPE_TEXT_VARIATION_PERSON_NAME);
        mContactPhone.setInputType(EditorInfo.TYPE_CLASS_PHONE);
        mContactEmail.setInputType(EditorInfo.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        
        currentContactEmail = ApplicationState.getContactEmail();
        currentContactName = ApplicationState.getContactName();
        currentContactPhone = ApplicationState.getContactPhone();
        
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
    	
    	if (mContactName.getText() != null) {
            temp = mContactName.getText().toString().trim();
    		
    		if (currentContactName.compareToIgnoreCase(temp) != 0 ) {
    			currentContactName = temp;
    			updateContactInfo = true;
    		}
    	}
    	
    	if (mContactPhone.getText() != null) {
        	temp = mContactPhone.getText().toString().trim();
    		
    		if (currentContactPhone.compareToIgnoreCase(temp) != 0 ) {

    	        String phoneType = getPhoneType(temp);
    	        String formattedPhoneNumber = temp;
    	        
    	        if(phoneType == PHONE_TYPE_UNDETERMINED) {
    	        	mContactPhone.requestFocus();
    	        	mContactPhone.selectAll();
    	        	showDialog(DIALOG_ERROR_PHONE_ID);
    	        	return;
    	        } else if (phoneType == PHONE_TYPE_NORTH_AMERICA) {
    	        	formattedPhoneNumber = formatNumber(PHONE_TYPE_NORTH_AMERICA, temp);
    	        } /*else if (phoneType == PHONE_TYPE_BLANK) {
    	        	//do nothing
    	        } else if (phoneType == PHONE_TYPE_INTERNATIONAL) {
    	        	formattedPhoneNumber = formatNumber(PHONE_TYPE_INTERNATIONAL, temp); } */
    	        
    	    	mContactPhone.setText(formattedPhoneNumber);
	            currentContactPhone = formattedPhoneNumber;
    	        updateContactInfo = true;

    		}
    	}
    	
    	if (mContactEmail.getText() != null) {
        	temp = mContactEmail.getText().toString().trim();
   		
    		if (currentContactEmail.compareToIgnoreCase(temp) != 0 ) {
    			
    			if(!StringUtils.isBlank(temp)) 
    			{
    				if(!isValidEmailAddress(temp)) {
        				mContactEmail.requestFocus();
        				mContactEmail.selectAll();
        				showDialog(DIALOG_ERROR_EMAIL_ID);
        	        	return;
        			}
    			}
    			currentContactEmail = temp;
    			updateContactInfo = true;
    		}
    	}
    	
    	if (updateContactInfo) {
    		//set the contact info so we have something to pass in with the report
    		//in the event that the update thread runs slow due to network issues
    		reportInfo.setContactName(currentContactName);
    		reportInfo.setContactEmail(currentContactEmail);
    		reportInfo.setContactPhone(currentContactPhone);
    		
    		ApplicationState.saveContactInfo();
    		
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
	}	
	
	@Override
	protected void onResume() {
		super.onResume();
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		Dialog dialog = null;
		
		switch (id) {
		case DIALOG_ERROR_PHONE_ID:
			dialog = errorDialog("Invalid Phone Number", "phone number");
			break;
		case DIALOG_ERROR_EMAIL_ID:
			dialog = errorDialog("Invalid Email Address", "email address");
			break;
		}
		return dialog;
	}
	
	private AlertDialog errorDialog(String errorTitle, String errorMsg) {

		View customDialogView = View.inflate(ContactInfo.this, R.layout.custom_dialog, null);
		TextView customTextView = (TextView) customDialogView.findViewById(R.id.customDialogText);

		customTextView.setText(errorTitle + "\n\nThe " + errorMsg + " you entered does not appear to be valid, please correct it before continuing.");

		AlertDialog.Builder builder = new AlertDialog.Builder(ContactInfo.this);
		builder.setView(customDialogView)

		.setCancelable(false).setPositiveButton("OK",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});

		AlertDialog alert = builder.create();

		return alert;
	}
	
	private String getPhoneType(String phoneNum) {
		
		String phonetype = PHONE_TYPE_UNDETERMINED;
		
		if(!StringUtils.isBlank(phoneNum)) {
			if(NorthAmerica.matcher(phoneNum).matches()) {
	    		phonetype = PHONE_TYPE_NORTH_AMERICA;
	    	} /* else if (International.matcher(phoneNum).matches()) { 
    			phonetype = PHONE_TYPE_INTERNATIONAL;
			}*/
		} else {
			phonetype = PHONE_TYPE_BLANK;
		}
		
		return phonetype;
	}
	

	
	private static String formatNumber(String phoneType, String phoneNumber) {
		Matcher m;
		String formattedPhoneNumber = phoneNumber;
		
		try {
			if(phoneType == PHONE_TYPE_NORTH_AMERICA) {
				m = NorthAmerica.matcher(phoneNumber);
				formattedPhoneNumber = m.replaceAll("$1-$2-$3");
			}
			else if (phoneType == PHONE_TYPE_INTERNATIONAL) {
				m = International.matcher(phoneNumber);
				formattedPhoneNumber = m.replaceAll("+$1 ($2) $3 $4");
			}
		} catch (Exception ex) {
			formattedPhoneNumber = phoneNumber;
		}
		
		return formattedPhoneNumber;
		
	}
	
	// **********************************************************************
	public static boolean isValidEmailAddress(String emailAddress) {
		Matcher matcher = EmailPattern.matcher(emailAddress);
		if (!matcher.find()) {
			return false;
		}
		return true;
	}
    

}
