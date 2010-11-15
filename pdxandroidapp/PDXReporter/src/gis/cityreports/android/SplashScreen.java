package gis.cityreports.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

/**
 * @author Roy Cham
 * 
 * Copyright 2010 City of Portland. All rights reserved.
 *
 */
public class SplashScreen extends Activity {

	protected boolean _active = true;
	protected int _splashTime = 1000; 
	
	protected Handler _exitHandler = null;
	protected Runnable _exitRunnable = null;
	
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    
	    setContentView(R.layout.custom_splash_dialog);
	 	    
	    _exitRunnable = new Runnable() {
	    	public void run() {
	    		exitSplash();
	    	}
	    };
	    
	    _exitHandler = new Handler();
	    _exitHandler.postDelayed(_exitRunnable, _splashTime);

	}

	private void exitSplash()
	{
		finish();
		
		Intent intent = new Intent(); 
        intent.setClass(SplashScreen.this, Report.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        
	}
	
}




