package org.codeforhealth.hip.android;


import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.codeforhealth.hip.android.data.ConfigSetting;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;

/**
 * @author Roy Cham
 * 
 * Copyright 2010 City of Portland. All rights reserved.
 *
 */
public class SettingHandler extends DefaultHandler {

    private static final String PARENT_NODE = "settings";

    private Map<String, String> data;
    //private StringTokenizer mElements;
    private String currentElement = null;
    private ConfigSetting mConfig;

    /*
    private void add(String key, String value) {
    	if (StringUtils.isNotBlank(key) && StringUtils.isNotBlank(value)) {
    		data.put(key, value);
    	}
    }
    */
    
	public SettingHandler(ConfigSetting config) {
		data = new HashMap<String, String>();
		accumulator = new StringBuffer();
		mConfig = config;
		//mElements = elements;
	}
	
    @Override
    public void startDocument() throws SAXException {
    }

    @Override
    public void endDocument() throws SAXException {
    }
    

    public Map<String, String> getData() {
        return data;
    }
    
    private StringBuffer accumulator;
    
    private boolean androidGpsSampleCount = false;
    private boolean disclaimerFrequency = false;
    private boolean disclaimerText = false;
    private boolean gpsAccuracyThreshold = false;
    private boolean gpsSampleCount = false;
    private boolean gpsSampleInterval = false;
    private boolean helpPage = false;
    private boolean maxRetryAttempts = false;
    private boolean photoCompression = false;
    private boolean photoMaxPixels = false;
    
    private boolean sampleSizeExtra = false;
    private boolean sampleSizeLarge = false;
    private boolean sampleSizeMedium = false;
    private boolean sampleSizeSmall = false;
    
    private boolean androidTimeout = false;
    private boolean androidCategoryRefresh = false;
    
    @Override
    public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
        
    	accumulator.setLength(0);
    	
    		/* 
    		 * Note, this assumes the xml only had 1 element of each, else the last one found
    		 * will overwrite the previous.  
    		 * 
    		 * ToDo: Add a tracker to which node you are currently in.
    		 */
    	
    		if (localName.equalsIgnoreCase(Report.disclaimer_frequency))    
    			disclaimerFrequency = true;
    		
    		if (localName.equalsIgnoreCase(Report.disclaimer_text))    
    			disclaimerText = true;
    		
    		if (localName.equalsIgnoreCase(Report.gps_accuracy_threshold))    
    			gpsAccuracyThreshold = true;
    		
    		if (localName.equalsIgnoreCase(Report.gps_sample_count))    
    			gpsSampleCount = true;
    		
    		if (localName.equalsIgnoreCase(Report.gps_sample_interval))    
    			gpsSampleInterval = true;
    		
    		if (localName.equalsIgnoreCase(Report.help_page))    
    			helpPage = true;
    		
    		if (localName.equalsIgnoreCase(Report.photo_compression))    
    			photoCompression = true;
    		
    		if (localName.equalsIgnoreCase(Report.photo_max_pixels))    
    			photoMaxPixels = true;
    		
    		if (localName.equalsIgnoreCase(Report.max_retry_attempts))
    			maxRetryAttempts = true;
    
    		if (localName.equalsIgnoreCase(Report.android_gps_sample_count))
    			androidGpsSampleCount = true;
    		
    		if (localName.equalsIgnoreCase(Report.sample_size_extra))
    			sampleSizeExtra = true;
    		
    		if (localName.equalsIgnoreCase(Report.sample_size_large))
    			sampleSizeLarge = true;
    		
    		if (localName.equalsIgnoreCase(Report.sample_size_medium))
    			sampleSizeMedium = true;
    		
    		if (localName.equalsIgnoreCase(Report.sample_size_small))
    			sampleSizeSmall = true;
    		
    		if (localName.equalsIgnoreCase(Report.android_timeout))
    			androidTimeout = true;
    		
    		if (localName.equalsIgnoreCase(Report.android_category_refresh))
    			androidCategoryRefresh = true;
    		
        	//Log.d("ELEMENT SIZE", "size is " + Integer.toString(mElements.countTokens()));
        	 
        	 
        	/*
        	while (mElements.hasMoreTokens()) {
        		temp = mElements.nextToken();
        		add(temp, getAttributeValue(temp, atts));
        	}
        	*/
        	
        	/*
    		for (String thisElement : mElements) {
    			Log.d("ELEMENT NAME - ", thisElement);
    			add(thisElement, getAttributeValue(thisElement, atts));
    		}
    		*/
        	
    		/*
            add("disclaimer_frequency", getAttributeValue("disclaimer_frequency", atts));
            add("disclaimer_text", getAttributeValue("disclaimer_text", atts));
            add("gps_accuracy_threshold", getAttributeValue("gps_accuracy_threshold", atts));
            add("gps_sample_count", getAttributeValue("gps_sample_count", atts));
            add("gps_sample_interval", getAttributeValue("gps_sample_interval", atts));
            add("help_page", getAttributeValue("help_page", atts));
            add("photo_compression", getAttributeValue("photo_compression", atts));
            add("photo_max_pixels", getAttributeValue("photo_max_pixels", atts));
            */
        
    }

       
    @Override
    
    public void endElement(String namespaceURI, String localName, String qName) {
    	
    	String temp = accumulator.toString().trim();
    	
    	if (disclaimerFrequency) {    
        	//add(Report.disclaimer_frequency, accumulator.toString().trim());
        	
    		if ( StringUtils.isNotBlank(temp) && StringUtils.isNumeric(temp) )
        		mConfig.setDisclaimerFrequency(temp);
        	
        	disclaimerFrequency = false;
    	}
    	
    	if (disclaimerText) {
        	//add(Report.disclaimer_text, accumulator.toString().trim());
        	
        	if ( StringUtils.isNotBlank(temp) )
        		mConfig.setDisclaimerText(temp);
        	
        	disclaimerText = false;
    	}
    	
    	if (gpsAccuracyThreshold) {    
        	//add(Report.gps_accuracy_threshold, accumulator.toString().trim());
        	
        	if ( StringUtils.isNotBlank(temp) && StringUtils.isNumeric(temp) )
        		mConfig.setGpsAccuracyThreshold(temp);
        	
        	gpsAccuracyThreshold = false;
    	}

    	if (gpsSampleCount) {    
        	//add(Report.gps_sample_count, accumulator.toString().trim());
    		
    		if ( StringUtils.isNotBlank(temp) && StringUtils.isNumeric(temp) )
        		mConfig.setGpsSampleCount(temp);
    		
        	gpsSampleCount = false;
    	}
    	
    	if (gpsSampleInterval) {
        	//add(Report.gps_sample_interval, accumulator.toString().trim());
    		
    		if ( StringUtils.isNotBlank(temp) && StringUtils.isNumeric(temp) )
        		mConfig.setGpsSampleInterval(temp);
    		
        	gpsSampleInterval = false;
    	}

    	if (helpPage) {    
        	//add(Report.help_page, accumulator.toString().trim());
        	
    		if ( StringUtils.isNotBlank(temp) )
        		mConfig.setHelpPage(temp);
        	
        	helpPage = false;
    	}
    	
    	if (photoCompression) {    
        	//add(Report.photo_compression, accumulator.toString().trim());
        	
    		if ( StringUtils.isNotBlank(temp) )
        		mConfig.setPhotoCompression(temp);
        	
        	photoCompression = false;
    	}
        
    	if (photoMaxPixels) {    
        	//add(Report.photo_max_pixels, accumulator.toString().trim());
    		
    		if ( StringUtils.isNotBlank(temp) && StringUtils.isNumeric(temp) )
        		mConfig.setPhotoMaxPixels(temp);
    		
        	photoMaxPixels = false;
    	}
    	
    	if (maxRetryAttempts) {
    		
    		if ( StringUtils.isNotBlank(temp) && StringUtils.isNumeric(temp) ) 
    			mConfig.setMaxRetryAttempts(Integer.parseInt(temp));
    		
    		maxRetryAttempts = false;
    	}
    	
    	if (androidGpsSampleCount) {
    		if ( StringUtils.isNotBlank(temp) && StringUtils.isNumeric(temp) ) 
    			mConfig.setAndroidGpsSampleCount(Integer.parseInt(temp));
    		
    		androidGpsSampleCount = false;
    	}
    	
    	if (sampleSizeExtra) {
    		if ( StringUtils.isNotBlank(temp) && StringUtils.isNumeric(temp) ) 
    			mConfig.setSampleSizeExtra(Integer.parseInt(temp));
    		
    		sampleSizeExtra = false;
    	}
    	
    	if (sampleSizeLarge) {
    		if ( StringUtils.isNotBlank(temp) && StringUtils.isNumeric(temp) ) 
    			mConfig.setSampleSizeLarge(Integer.parseInt(temp));
    		
    		sampleSizeLarge = false;
    	}
    	
    	if (sampleSizeMedium) {
    		if ( StringUtils.isNotBlank(temp) && StringUtils.isNumeric(temp) ) 
    			mConfig.setSampleSizeMedium(Integer.parseInt(temp));
    		
    		sampleSizeMedium = false;
    	}
    	
    	if (sampleSizeSmall) {
    		if ( StringUtils.isNotBlank(temp) && StringUtils.isNumeric(temp) ) 
    			mConfig.setSampleSizeSmall(Integer.parseInt(temp));
    		
    		sampleSizeSmall = false;
    	}
    	
    	if (androidTimeout) {
    		if ( StringUtils.isNotBlank(temp) && StringUtils.isNumeric(temp) ) 
    			mConfig.setAndroidTimeout(Integer.parseInt(temp));
    		
    		androidTimeout = false;
    	}
    	
    	if (androidCategoryRefresh) {
    		if ( StringUtils.isNotBlank(temp) && StringUtils.isNumeric(temp) ) 
    			mConfig.setAndroidCategoryRefresh(Integer.parseInt(temp));
    		
    		androidCategoryRefresh = false;
    	}
    	
    	
    	
    }

    @Override
    public void characters(char ch[], int start, int length) {
    	accumulator.append(ch, start, length);
    }

    
    private String getAttributeValue(String attName, Attributes atts) {
        String result = null;
        for (int i = 0; i < atts.getLength(); i++) {
            String thisAtt = atts.getLocalName(i);
            if (attName.equals(thisAtt)) {
                result = atts.getValue(i);
                break;
            }
        }
        return result;
    }

}
