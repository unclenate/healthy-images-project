package gis.cityreports.android;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings.Secure;
//import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

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
public class MyReports extends ListActivity
{ 
	public final static String ITEM_UNIQUE_IDENTIFIER                 	= "item_id";
    public final static String ITEM_STATUS                         		= "status";
    public final static String ITEM_IPHONE_INPUT_ALIAS            	 	= "iphone_input_alias"; 
    public final static String ITEM_IPHONE_BINARY_VALUE             	= "iphone_binary_input_value";  
    public final static String ITEM_IPHONE_BINARY_FILENAME        		= "iphone_binary_input_file_name"; 
    public final static String ITEM_IPHONE_INPUT_MIMETYPE            	= "iphone_binary_input_mime_type"; 
    public final static String ITEM_IPHONE_TEXT_VALUE                 	= "iphone_text_input_value"; 
    public final static String ITEM_IPHONE_ADDRESS_LATITUDE_VALUE     	= "iphone_address_input_value_lat";  
    public final static String ITEM_IPHONE_ADDRESS_LONGITUDE_VALUE 		= "iphone_address_input_value_long"; 
    public final static String ITEM_IPHONE_INPUT_VALUE             		= "iphone_status_input_value";  
    public final static String ITEM_IPHONE_LASTUPDATED             		= "last_updated";
    public final static String REQUEST_MY_REPORTS 						= "REQUEST_MY_REPORTS"; 
    
    private final static String CLOSED 			= "Closed";
    private final static String LAST_UPDATED 	= "Last Updated: ";
    private final static String OPEN 			= "Open";
    private final static String REFERRED 		= "Referred";
    private final static String REPORT_STATUS 	= "REPORT_STATUS";
    private final static String WORKING 		= "Working";
    private final static String[] results 		= new String[] { "No Reports Found." };
    
    private static int retryAttemptsForMyReports;

    private static List<Map<String, String>> resourceNames;
    private static ItemReportHandler handler;
    
    private static ItemsAdapter reports;
    private static String deviceId;
    private static String statusLineTxt;    
    
    private final Handler myReportsHandler = new Handler() {

        @Override
        public void handleMessage(final Message msg) {

            String bundleResult = msg.getData().getString("RESPONSE");
            //Log.d("BUNDLE_RESULT", bundleResult);
            
            if(!bundleResult.contains("ERROR_FOUND")) {
                parseXMLResult(bundleResult);
            } else {
                retryAttemptsForMyReports++;
                
                if(retryAttemptsForMyReports > 2) {
                    retryAttemptsForMyReports = 0;
                } else {
                    performRequest(null, null, REQUEST_MY_REPORTS);
                }
                
            }
        }
    };
        
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_reports);

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
    		    
    		    deviceId = deviceInfo.toString();
    	    }
        	
        } catch (Exception ex) { }
            	
	    if (StringUtils.isNotBlank(deviceId)) {
	    	performRequest(null, null, REQUEST_MY_REPORTS);
	    } else {
		    AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
			builder.setMessage(getString(R.string.unknownDevice))
					.setCancelable(false)
				    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
			    	public void onClick(DialogInterface dialog, int id) {
			    		MyReports.this.finish();
			    	}
			    	});
			    			
			   	AlertDialog alert = builder.create();
			    alert.show();
		    
	    }
	    statusLineTxt = getString(R.string.myReportStatusLine);
    }

	@Override
	protected void onDestroy() {
		
		resourceNames = null;
		handler = null;
		reports = null;
		
		System.gc();
		
		super.onDestroy();
	}
    
    @Override
    public boolean onSearchRequested() {
        return true;
    }
    
    private void parseXMLResult(String xmlString) {
        
        Hashtable<Integer, String> itemHashtable = new Hashtable<Integer, String>();
        
        //Log.d("RESPONSE", xmlString);
        
        Map<String, String> data;
        Map<String, String> placeHolderData;
       
        Map<Integer, String> categoryHeaders = new HashMap<Integer, String>();
        Map<Integer, String> itemPositions = new HashMap<Integer, String>();
        
        List<Item> items;
        
        try {
            SAXParserFactory spf = SAXParserFactory.newInstance();
            SAXParser sp = spf.newSAXParser();
            XMLReader xr = sp.getXMLReader();
            
            handler = new ItemReportHandler();
            
            xr.setContentHandler(handler);
            xr.parse(new InputSource(new StringReader(xmlString)));
            
            items = handler.getItems();
            Collections.sort(items);
            
            resourceNames = new ArrayList<Map<String, String>>();

            String currentCategory = null;
            
            int listCount = items.size();
            int position = 0;
            
            if (listCount > 0) 
            {
                Hashtable<String, String> attributes;
                String value = "";
                
                for (Item o : items) {
                    //Log.d("CATEGORY - ITEMID", o.getCategory() + " - " + o.getItemId());
                    
                    data = new HashMap<String, String>();
                    attributes = o.getAttributes();
                    
                    if ((currentCategory == null) || (currentCategory.compareToIgnoreCase(o.getCategory()) != 0)) {
                        categoryHeaders.put(position, o.getCategory());
                        
                        placeHolderData = new HashMap<String, String>();
                        placeHolderData.put(REPORT_STATUS, "TESTER");
                        itemHashtable.put(position, o.getCategory());
                        
                        resourceNames.add(placeHolderData);
                        position++;
                    }
             
                    if (attributes.containsKey(ITEM_STATUS)) {
                        value = (String) attributes.get(ITEM_STATUS);
                        
                        if (value.equalsIgnoreCase("O")) {
                            value = OPEN;
                        } else if (value.equalsIgnoreCase("C")) {
                            value = CLOSED;
                        } else if (value.equalsIgnoreCase("R")) {
                            value = REFERRED;
                        } else if (value.equalsIgnoreCase("W")) {
                            value = WORKING;
                        }
                    }
                                        
                    data.put(Integer.toString(position), value + "\n" + LAST_UPDATED + (String) attributes.get(ITEM_IPHONE_LASTUPDATED));
                    
                    itemHashtable.put(position, value + "\n" + LAST_UPDATED + (String) attributes.get(ITEM_IPHONE_LASTUPDATED));
                    
                    resourceNames.add(data);
                    currentCategory = o.getCategory();
                    itemPositions.put(position, o.getItemId());
                    position++;
                }
                
                items.clear();
                items = null;
                
                reports = new ItemsAdapter(
                		getApplicationContext(),
                        resourceNames,
                        R.layout.my_report_row,
                        new String[] { REPORT_STATUS },
                        new int[] { R.id.reportStatus },
                        categoryHeaders,
                        itemPositions,
                        itemHashtable); 
                       
                setListAdapter(reports);
                
            } else {
            	ListAdapter adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, results);
                setListAdapter(adapter);
            }
        } catch (Exception e) {
            //Log.e(Constants.LOGTAG, " ERROR - " + e.toString());
        } finally {
        	resourceNames = null;
        	handler.clear();
        	handler = null;
        	categoryHeaders = null;
            itemPositions = null;
            itemHashtable = null;
            items = null;
            data = null;
            placeHolderData = null;
            
            System.gc();
        }

    }

    private void performRequest(final String user, final String pass, final String requestType) {

        final ResponseHandler<String> responseHandler = HTTPRequestHelper.getResponseHandlerInstance(myReportsHandler);

        new Thread() {

            @Override
            public void run() {
                HTTPRequestHelper helper = new HTTPRequestHelper(responseHandler);
                
                String url = getString(R.string.postRequestReports_PROD);
                
                HashMap<String, String> postParams = new HashMap<String, String>();
                
                if (requestType == REQUEST_MY_REPORTS) {
                   
                    
                    postParams.put(getString(R.string.param1), getString(R.string.pval) );
                    postParams.put(getString(R.string.param2), deviceId ); //deviceId
                    postParams.put(getString(R.string.param5), getString(R.string.pval5) );
                    
                    
                    helper.performPost(url, user, pass, 
                            Integer.parseInt(getString(R.string.httpTimeOutMilliseconds)), 
                            Integer.parseInt(getString(R.string.socketTimeoutMilliseconds)), 
                            null, postParams);
                    
                }
                postParams = null;
                helper = null;
            }
        }.start();

    }
    
    private class ItemsAdapter extends SimpleAdapter {

        private final int[] colors = new int[] { Color.WHITE, 0xffE4E4E4 };
        
        private Map<Integer, String> categories;
        private Map<Integer, String> positionMap;
        private Hashtable<Integer, String> reportItems;
        
        private LayoutInflater mInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        private int colorPos;
        
        public ItemsAdapter(Context context, 
                List<Map<String, String>> items, 
                int resource, 
                String[] from, 
                int[] to, 
                Map<Integer, String> categoryHeaders,
                Map<Integer, String> itemPositionMap,
                Hashtable<Integer, String> itemReports) {
            
            super(context, items, resource, from, to);

            categories = categoryHeaders;
            positionMap = itemPositionMap;
            reportItems = itemReports;
            
        }
        
        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }
        
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
         	
        	ReferenceHolder refHolder = new ReferenceHolder();
        	
        	//if(convertView == null ) {
        	//	Log.d("convertView", "convertView is null at postion " + position);
        	//}
        	
        	colorPos = position % colors.length;
        	
            if (categories.containsKey(position))
            {
            	convertView = mInflater.inflate( R.layout.custom_issue_row, null);
    			refHolder.getIdsAndSetTag(convertView, 1, categories.get(position), "");
            } else {
            	convertView = mInflater.inflate( R.layout.my_report_row, null);
            	convertView.setBackgroundColor(colors[colorPos]);
    			
    			String itemValue = "";
    			
    			if (positionMap.containsKey(position)) 
    				itemValue = positionMap.get(position);
					
    			refHolder.getIdsAndSetTag(convertView, 2, reportItems.get(position), itemValue);
            }
            return convertView;
        }
        

        private class ReferenceHolder {
        	TextView tv;

            void getIdsAndSetTag(View v, int type, String text, final String itemValue){
            	if (type == 1) 
            		tv = (TextView) v.findViewById(R.id.reportHeader);
            	else {
            		tv = (TextView) v.findViewById(R.id.reportStatus);
            		
            		tv.setOnClickListener(new OnClickListener() {

        				@Override
        				public void onClick(View v) {
        					
        					if (itemValue != "" ) {
        						Intent intent = new Intent(MyReports.this, ReportDetails.class);
        						intent.putExtra(ITEM_UNIQUE_IDENTIFIER, itemValue);
        						intent.putExtra(Report.DEVICE_ID, deviceId); //deviceId
        						
        			        	startActivity(intent);
        					}
        					
        				}
                		
                	});
                	
            	}
        		tv.setText(statusLineTxt + " " + text);
                v.setTag(this);
            }
        }
    }
}













