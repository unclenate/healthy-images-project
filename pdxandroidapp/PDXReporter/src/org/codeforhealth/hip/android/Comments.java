package org.codeforhealth.hip.android;

import org.codeforhealth.hip.android.data.ReportInfo;

import org.codeforhealth.hip.android.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * @author Roy Cham
 * 
 * Copyright 2010 City of Portland. All rights reserved.
 *
 */
public class Comments extends Activity  {

	private EditText mComments;
	private ReportInfo reportInfo;
	
	private String currentComments;
	
    
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.comments);
        
        reportInfo = ((ApplicationState)getApplication()).getReportInfoState();	
                
        mComments = (EditText) findViewById(R.id.editComments);
        currentComments = ApplicationState.getComments();
        mComments.setText(currentComments);
        
        Button btnSaveComments = (Button) findViewById(R.id.btnFinishComments);
        btnSaveComments.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        		mgr.hideSoftInputFromWindow(mComments.getWindowToken(), 0);
        		setEditTextField();
        		finish();
        	}
        });
        
        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        // only will trigger it if no physical keyboard is open
        mgr.showSoftInput(mComments, InputMethodManager.SHOW_IMPLICIT);
        
        /*
        mComments.setOnKeyListener(new OnKeyListener() {
		    public boolean onKey(View v, int keyCode, KeyEvent event) {
		    	
		    	// If the event is a key-down event on the "enter" button
		        if ((event.getAction() == KeyEvent.ACTION_UP) &&
		            (keyCode == KeyEvent.KEYCODE_BACK)) {
		        	
		        	setEditTextField();

		          return false;
		        }
		        return false;
		    }
		});
        */     
    }
    
    private void setEditTextField() {
    	if (mComments.getText() != null) {
    		reportInfo.setDescription(mComments.getText().toString().trim());
    		ApplicationState.SaveComments();
    	}
    }
    
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        
        // Checks whether a hardware keyboard is available
        if (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_NO) {
            //Toast.makeText(this, "keyboard visible", Toast.LENGTH_SHORT).show();
        	getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
       	 	
        } else if (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_YES) {
            Toast.makeText(this, "keyboard hidden", Toast.LENGTH_SHORT).show();
        }
    }
	
    @Override
    protected void onSaveInstanceState(Bundle outState) {
    	
    	setEditTextField();
    	ApplicationState.SaveComments();
    	
    	super.onSaveInstanceState(outState);
    }
    
    @Override
    public void onBackPressed() {
    	
    	/*
    	if (mComments.getText() != null) {
    		setEditTextField();
    	}
    	*/

    	Comments.this.finish();
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
    

}
