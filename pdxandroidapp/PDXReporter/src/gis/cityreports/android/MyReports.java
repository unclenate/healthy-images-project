package gis.cityreports.android;

import gis.cityreports.utils.Utils;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.lang.StringUtils;
import org.apache.http.client.ResponseHandler;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import com.markupartist.android.widget.PullToRefreshListView;
import com.markupartist.android.widget.PullToRefreshListView.OnRefreshListener;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings.Secure;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;


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
    
    public final static String CLOSED 			= "Closed";
    public final static String LAST_UPDATED 	= "Last Updated: ";
    public final static String OPEN 			= "Open";
    public final static String REFERRED 		= "Referred";
    public final static String REPORT_STATUS 	= "REPORT_STATUS";
    public final static String WORKING 			= "Working";
    public final static String ARCHIVED			= "Archived";
    public final static String WORK_IN_PROGRESS = "Work in Progress";
    
    private final static String results 			= "No Reports Found.";
    private final static String msgNoConnection 	= "Connection not available.";
    private final static String msgRetrieving		= "Retrieving reports . . .";
    
    private final static int FILTER_RETURN = 1;
    private static int retryAttemptsForMyReports;

    private static List<Map<String, String>> resourceNames;
    private static ItemReportHandler handler;
    
    private static ItemsAdapter reports;
    private static String deviceId;
    private static String statusLineTxt;    
    
    private static String categories = "";
    private static String categoryStatus;
    
    private ProgressBar progressWidget;
        
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
                    displayLayout(msgNoConnection);
                    progressWidget.setVisibility(View.INVISIBLE);
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

        progressWidget = (ProgressBar)findViewById(R.id.progress_small);
        
	    if (StringUtils.isNotBlank(deviceId)) {
	    	doRequest();
	    } else {
		    AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
	    
	    Button buttonFilter = (Button)findViewById(R.id.btnFilter);
        buttonFilter.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
        		Intent i = new Intent(MyReports.this, FilterActivity.class);
        		
        		//startActivity(i);
        		startActivityForResult(i, FILTER_RETURN);
        	}
        });
        
       ((PullToRefreshListView) getListView()).setOnRefreshListener(new OnRefreshListener() {
            public void onRefresh() {
                new GetDataTask().execute();
            }
        });
        
    }
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case FILTER_RETURN:
			if (resultCode == RESULT_OK) {
				doRequest();
				break;
			}
		}
	}
	
	private void doRequest() 
	{
		categories = returnCategories();
		categoryStatus = returnSearchByStatus();
		
		if(StringUtils.isNotBlank(categories) && StringUtils.isNotBlank(categoryStatus)) {
			progressWidget.setVisibility(View.VISIBLE);
        	displayLayout(msgRetrieving);
        	performRequest(null, null, REQUEST_MY_REPORTS);
        } else {
        	displayLayout(results);
        }
		
	}
	
	private void displayLayout(String message) {
		
		listPlaceHolder = createPlaceHolders();
		listPlaceHolder.addFirst(message.toString());
				
		ListAdapter adapter = new ArrayAdapter<String>(this, R.layout.custom_listview, R.id.customMsg, listPlaceHolder);
		setListAdapter(adapter);
	}
	
	private String returnSearchByStatus()
	{
		String statusList = getString(R.string.pval5);
		
		String hiddenStatus = ApplicationState.getInvisibleStatus();
		
		if (hiddenStatus != null) {
			Object desInvibleStatus = Utils.stringToObject(hiddenStatus);
	        Collection<String> invisibleStatus = (Collection<String>) desInvibleStatus;
	        	        
	        Map<Integer, String> data = (Map<Integer, String>) FilterActivity.statusMap;
	        StringBuilder finalList = new StringBuilder();
	        String currentStatus;
	        boolean add = false;
	        
	        for (Object key: data.keySet()) {
	        	add = true;
	        	currentStatus = data.get(key);
	            
	            for (String status : invisibleStatus) {
		        	if (currentStatus.compareToIgnoreCase(status) == 0) {
		        		add = false;
		        		break;
		        	}
		        }
		        
		        if(add){
	        		if (finalList.length() > 0)  
	        			finalList.append(",");   
					
	        		finalList.append(currentStatus);
	        	}
	            
	        }

			statusList = finalList.toString();
		} 
		
		return statusList;
	}
    
	
	private String returnCategories() 
    {
    	String categoryList = null;
    	
    	String serializedCategory = ApplicationState.getCategories();
    	String hiddenCategories = ApplicationState.getInvisibleCategories();
        
        if (serializedCategory != null) {
	        Object deserializedCategory = Utils.stringToObject(serializedCategory);
	        
	        List<CategoryDetails> posts = null;
	        
	        if (deserializedCategory instanceof List<?>) {
	        	posts = (List<CategoryDetails>) deserializedCategory;
	        }
	        
	        if (posts == null)
	        	return null;
	        
	        		   
	        //Create new list if filter is found
	        if (hiddenCategories != null) {
		        Object desInvibleCategory = Utils.stringToObject(hiddenCategories);
		        Collection<String> invisibleCategories = (Collection<String>) desInvibleCategory;
		        
		        String currentCategory;
		        
		        boolean add = false;
		        StringBuilder sb = new StringBuilder();
		        
		        for (CategoryDetails p : posts) {
		        	add = true;
		        	currentCategory = p.getCategory_id();
		        	
			        for (String cat : invisibleCategories) {
			        	if (currentCategory.compareToIgnoreCase(cat) == 0) {
			        		add = false;
			        		break;
			        	}
			        }
			        
			        if(add){
		        		if (sb.length() > 0)  
							sb.append(",");   
						
						sb.append(currentCategory);
		        	}
		        }
		        
				categoryList = sb.toString();
	        }
	        else 
	        {
	        	//Create default list if no filter is setup
	        	StringBuilder sbDefaultList = new StringBuilder();
	        	
	        	for (CategoryDetails d : posts) {
		        	
			        if (sbDefaultList.length() > 0)  
			        	sbDefaultList.append(",");   
						
			        sbDefaultList.append(d.getCategory_id());
		        }
	        	categoryList = sbDefaultList.toString();
	        }
        }
        
        return categoryList;
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
            
            resourceNames = new ArrayList<Map<String, String>>();

            String currentCategory = null;
            
            int listCount = items.size();
            int position = 0;
            boolean isPlaceholder = false;
            
            if (listCount > 0) 
            {
                Hashtable<String, String> attributes;
                String value = "";
                
                Item item;
                Hashtable<String, String> placeholderAttribute = new Hashtable<String, String>();
                placeholderAttribute.put(ITEM_STATUS, "placeholder");
                placeholderAttribute.put(ITEM_IPHONE_LASTUPDATED, "placeholder2");
                
                item = new Item("99999999", "zzz_placeholder", placeholderAttribute);
                items.add(item);
                
                Collections.sort(items);
                
                for (Item o : items) {
                    data = new HashMap<String, String>();
                    attributes = o.getAttributes();
                    
                    if ((currentCategory == null) || (currentCategory.compareToIgnoreCase(o.getCategory()) != 0)) {
                        categoryHeaders.put(position, o.getCategory());
                        
                        placeHolderData = new HashMap<String, String>();
                        placeHolderData.put(REPORT_STATUS, "TESTER");
                        itemHashtable.put(position, o.getCategory());
                        
                        resourceNames.add(placeHolderData);
                        position++;
                        
                        if( o.getCategory().compareToIgnoreCase("zzz_placeholder") == 0 ) {
                        	isPlaceholder = true;
                        } else {
                        	isPlaceholder = false;
                        }
                    }
             
                    if (attributes.containsKey(ITEM_STATUS)) {
                        value = getStatus((String) attributes.get(ITEM_STATUS));
                    }
                    
                    if(!isPlaceholder) {
	                    data.put(Integer.toString(position), value + "\n" + LAST_UPDATED + (String) attributes.get(ITEM_IPHONE_LASTUPDATED));
	                    
	                    itemHashtable.put(position, value + "\n" + LAST_UPDATED + (String) attributes.get(ITEM_IPHONE_LASTUPDATED));
	                    
	                    resourceNames.add(data);
	                    currentCategory = o.getCategory();
	                    itemPositions.put(position, o.getItemId());
	                    position++;
                    }
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
            	displayLayout(results);
            }
        } catch (Exception e) {
            Log.e(Constants.LOGTAG, " ERROR - " + e.toString());
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
            
            progressWidget.setVisibility(View.INVISIBLE);
            System.gc();
        }

    }
    
    public static String getStatus(String prefix) {
    	String status = null;
    	
    	if (prefix.equalsIgnoreCase("O")) {
    		status = OPEN;
        } else if (prefix.equalsIgnoreCase("C")) {
        	status = CLOSED;
        } else if (prefix.equalsIgnoreCase("R")) {
        	status = REFERRED;
        } else if (prefix.equalsIgnoreCase("W")) {
        	status = WORK_IN_PROGRESS;
        } else if (prefix.equalsIgnoreCase("A")) {
        	status = ARCHIVED;
        }
    	
    	return status;
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
                    postParams.put(getString(R.string.param5), categoryStatus );
                    postParams.put(getString(R.string.param9), categories );
                                        
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
        
    private LinkedList<String> listPlaceHolder;
    
    private final static LinkedList<String> createPlaceHolders() {
    	LinkedList<String> holder = new LinkedList<String>();
    	//for (int i = 0; i < 10; i++) { holder.add(""); }
    	return holder;
    }
    	
    
    final Handler listUpdateHandler = new Handler();
    
    Runnable listRefresh = new Runnable() {
		public void run() {
			
			if (!Utils.isNetworkAvailable(MyReports.this)) {
    			displayLayout(msgNoConnection);
    			return;
    		} 
			
			doRequest();
		}
    };
    
    private class GetDataTask extends AsyncTask<Void, Void, String[]> {

        @Override
        protected String[] doInBackground(Void... params) {
            try {
            	listUpdateHandler.post(listRefresh);
            } catch (Exception e) {
                Log.e("ERROR", e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String[] result) {
            ((PullToRefreshListView) getListView()).onRefreshComplete();
            super.onPostExecute(result);
        }
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
        	
        	colorPos = position % colors.length;
        	
        	if (categories.containsKey(position) && categories.get(position).compareToIgnoreCase("zzz_placeholder") == 0) {
        		convertView = mInflater.inflate( R.layout.custom_placeholder_row, null);
        		return convertView;
        	}
        	
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
            	
            	if (type == 1) {
            		tv = (TextView) v.findViewById(R.id.reportHeader);
            		tv.setText(text);
            	} else {
            		tv = (TextView) v.findViewById(R.id.reportStatus);
            		
            		tv.setOnClickListener(new OnClickListener() {

        				public void onClick(View v) {
        					
        					if (itemValue != "" ) {
        						Intent intent = new Intent(MyReports.this, ReportDetails.class);
        						intent.putExtra(ITEM_UNIQUE_IDENTIFIER, itemValue);
        						intent.putExtra(Report.DEVICE_ID, deviceId); //deviceId
        						
        			        	startActivity(intent);
        					}
        					
        				}
                		
                	});
            		
            		tv.setText(statusLineTxt + " " + text);
            	}
            	
                v.setTag(this);
            }
        }
    }
}













