package org.codeforhealth.hip.android;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
//import android.util.Log;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.codeforhealth.hip.android.R;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;


/**
 * @author Roy Cham
 * 
 * Copyright 2010 City of Portland. All rights reserved.
 *
 */
public class ReportDetails extends Activity {

	private static String requestUrl;
	private static WebView mWebView;

	public void onCreate(Bundle savedInstanceState) 
	{
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.report_detail);
	    
	    Intent myIntent = getIntent();
	    String itemId = myIntent.getStringExtra(MyReports.ITEM_UNIQUE_IDENTIFIER);
	    String deviceId = myIntent.getStringExtra(Report.DEVICE_ID);
	    
	    requestUrl = getString(R.string.postRequestReportInfo_PROD);

	    mWebView = (WebView) findViewById(R.id.reportWebView);
	    mWebView.getSettings().setJavaScriptEnabled(true);
	    mWebView.loadUrl(requestUrl + "?device_id=" + deviceId + "&item_id="+itemId);
	    mWebView.setWebViewClient(new CustomWebViewClient());
	    
	}
	
	private class CustomWebViewClient extends WebViewClient {
	    @Override
	    public boolean shouldOverrideUrlLoading(WebView view, String url) {

	    	//Log.d("URL", url);
	    	
	    	try {
	    		
		    	if( url.contains(getString(R.string.postRequestReportInfoRegEx)) ){
	
		    		String encodedurl = URLEncoder.encode(url.toString(),"UTF-8");
		    		String html = "<img src=\"" + encodedurl + "\" />";
		    			
		    		view.getSettings().setBuiltInZoomControls(true);
		    		view.loadData(html, "text/html", "utf-8");
	
					
		        } else if (url.contains(getString(R.string.postRequestMapInfoRegEx))) {
		        	
		        	List<NameValuePair> params = URLEncodedUtils.parse(new URI(url), "UTF-8");
		        	
		        	Iterator<NameValuePair> iterator = params.iterator();
		        	String temp;
		        	String tempLat = null, tempLong = null;
		        	NameValuePair entry;
		        	
		      	  	while ( iterator.hasNext() ){
		      	  		
		      	  		entry = iterator.next();
		      	  		temp = entry.getName();
		      	  		
		      	  		if (temp.compareToIgnoreCase("lat") == 0) 
		      	  			tempLat = entry.getValue().toString();
		      	  		
		      	  		if (temp.compareToIgnoreCase("long") == 0)
		      	  			tempLong = entry.getValue().toString();
		      	  		
		      	  	}
		       
		      	  	if (StringUtils.isNotBlank(tempLat) && StringUtils.isNotBlank(tempLong)) {
		        		Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse("geo:" + tempLat + "," + tempLong));
		      	  		startActivity(intent);
		      	  	}
					
		        }
		    	
	    	} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
        
	    	
	        return true;
	    }

	    // here you execute an action when the URL you want is about to load
	    @Override
	    public void onLoadResource(WebView view, String url){
	        
	    }
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
	        mWebView.goBack();
	        return true;
	    }
	    return super.onKeyDown(keyCode, event);
	}

	
	@Override
	protected void onDestroy() {
			
		mWebView.clearCache(false);
		mWebView.destroy();
		
		System.gc();
		
		super.onDestroy();
		
	}
	
}









