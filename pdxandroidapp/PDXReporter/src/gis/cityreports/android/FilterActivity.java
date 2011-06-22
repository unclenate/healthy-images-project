package gis.cityreports.android;

import gis.cityreports.utils.Utils;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.TableRow.LayoutParams;

import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.ToggleButton;


/**
 * @author Roy Cham
 * 
 * Copyright 2010 City of Portland. All rights reserved.
 *
 */
public class FilterActivity extends Activity {

	private Collection<String> invisibleCategories = new HashSet<String>();
	private Collection<String> invisibleStatus = new HashSet<String>();
	
	private final int[] colors = new int[] { 0xffE4E4E4, Color.WHITE };
	private int colorPos;
	
	private TextView tv;
	
    public static final Map<Integer, String> statusMap = createStatusMap();

    private static Map<Integer, String> createStatusMap() {
        Map<Integer, String> status = new HashMap<Integer, String>();
        
        // Add your own custom default status values
        // ie. status.put();
        
        return Collections.unmodifiableMap(status);
    }
        	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filter_view);
        
        String hiddenCategories = ApplicationState.getInvisibleCategories();
        
        if (hiddenCategories != null) {
	        Object desInvibleCategory = Utils.stringToObject(ApplicationState.getInvisibleCategories());
	        
	        if(desInvibleCategory instanceof Collection<?>) {
	        	invisibleCategories = (Collection<String>) desInvibleCategory;
	        }
        }
        
        String hiddenStatus = ApplicationState.getInvisibleStatus();
        
        if (hiddenStatus != null) {
        	Object desInvisibleStatus = Utils.stringToObject(ApplicationState.getInvisibleStatus());
        	
        	if(desInvisibleStatus instanceof Collection<?>) {
        		invisibleStatus = (Collection<String>) desInvisibleStatus;
        	}
        }
     

        TableLayout tl = (TableLayout)findViewById(R.id.filtersByStatus);
        tl.setGravity(android.view.Gravity.BOTTOM); 
        tl.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_layout));
        TableRow tr;
        
        tr = new TableRow(this);
        
        TextView textViewByStatus = new TextView(this);
        textViewByStatus.setText("Show or Hide Reports by Status:");
        textViewByStatus.setTextColor(Color.WHITE);
        textViewByStatus.setTypeface(Typeface.SANS_SERIF, Typeface.NORMAL);
        textViewByStatus.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
        textViewByStatus.setPadding(5, 10, 5, 10);
        textViewByStatus.setGravity(Gravity.CENTER_HORIZONTAL);
        textViewByStatus.setBackgroundResource(R.drawable.header);
        
        TableRow.LayoutParams params = new TableRow.LayoutParams();
        params.span = 2;
        params.width = LayoutParams.WRAP_CONTENT;
        params.height = LayoutParams.WRAP_CONTENT;
        
        tr.setLayoutParams(params);
        
        tl.addView(textViewByStatus);
        
        String currentStatus;
        
        Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		int width = display.getWidth();
		
		int w = 1;
		Integer key;
		String value;
		Set keys = statusMap.keySet();
		
	    for (Iterator i = keys.iterator(); i.hasNext();) {
        	
        	key = (Integer) i.next();
  	      	value = (String) statusMap.get(key);
        	
        	final ToggleButton tglButton = new ToggleButton(this); 
        	currentStatus = MyReports.getStatus(value);
        	
        	/* Create a new row to be added. */
            tr = new TableRow(this);
            tr.setPadding(10, 5, 0, 5);
            
            colorPos = w % colors.length;
            tr.setBackgroundColor(colors[colorPos]);
            
            
            tr.setLayoutParams(new LayoutParams(
                           LayoutParams.FILL_PARENT,
                           LayoutParams.FILL_PARENT));
            
            if (width > 200) {
    			width = (int) ((display.getWidth()*.8) - 50);
    		}
            
            TextView textView = new TextView(this);
            textView.setText(currentStatus);
            textView.setId(key + 100);
            textView.setWidth(width);
            textView.setMinimumWidth(width);
            textView.setGravity(Gravity.CENTER | Gravity.LEFT); 
            textView.setTextColor(Color.BLACK);
            textView.setTypeface(Typeface.SANS_SERIF, Typeface.NORMAL);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
            
            
            textView.setLayoutParams(new LayoutParams(
                    LayoutParams.FILL_PARENT,
                   LayoutParams.FILL_PARENT));
            
           
            tr.addView(textView);
               
            tglButton.setTextOn("Show");
            tglButton.setTextOff("Hide");
            tglButton.setMinimumWidth(50);
            tglButton.setWidth(50);
            tglButton.setGravity(Gravity.CENTER);
            tglButton.setMaxWidth(50);
            tglButton.setId(key);
            
            tglButton.setLayoutParams(new LayoutParams(
                    LayoutParams.FILL_PARENT,
                   LayoutParams.FILL_PARENT));
            
            
            if(invisibleStatus.contains(value)) {
            	tglButton.setChecked(false);
            	textView.setTextColor(Color.LTGRAY);
            } else {
            	tglButton.setChecked(true);
            	textView.setTextColor(Color.BLACK);
            }
                        
            tglButton.setOnClickListener(new OnClickListener() { 
            	 public void onClick(View v) {
                     if (tglButton.isChecked()) {
                    	 invisibleStatus.remove(statusMap.get(tglButton.getId()));
                    	 tv = (TextView) findViewById(tglButton.getId() + 100);
                    	 tv.setTextColor(Color.BLACK);
                     } else {
                    	 invisibleStatus.add(statusMap.get(tglButton.getId()));
                    	 tv = (TextView) findViewById(tglButton.getId() + 100);
                    	 tv.setTextColor(Color.LTGRAY);
                     }
                 }
            });
            
            tr.addView(tglButton);

            
           tl.addView(tr,new TableLayout.LayoutParams(
                 LayoutParams.FILL_PARENT,
                 LayoutParams.FILL_PARENT));
            
           w++;
        }
                
        TableLayout t2 = (TableLayout)findViewById(R.id.filtersByType);
        t2.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_layout));
        
        tr = new TableRow(this);
        
        TextView textViewByType = new TextView(this);
        textViewByType.setText("Show or Hide Reports by Type:");
        textViewByType.setTextColor(Color.WHITE);
        textViewByType.setTypeface(Typeface.SANS_SERIF, Typeface.NORMAL);
        textViewByType.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
        textViewByType.setPadding(0, 10, 0, 10);
        textViewByType.setBackgroundResource(R.drawable.header);
        textViewByType.setGravity(Gravity.CENTER_HORIZONTAL);
        
        TableRow.LayoutParams paramsByType = new TableRow.LayoutParams();
        paramsByType.span = 2;
        paramsByType.width = LayoutParams.FILL_PARENT;
        paramsByType.height = LayoutParams.FILL_PARENT;
        
        tr.setLayoutParams(paramsByType);
        
        t2.addView(textViewByType);
        
        String serializedCategory = ApplicationState.getCategories();
        
        if (serializedCategory != null) {
	        Object deserializedCategory = Utils.stringToObject(ApplicationState.getCategories());
	        List<CategoryDetails> posts = (List<CategoryDetails>) deserializedCategory;
                	     
	        int r = 0;
	        
	        int tempCategory = 1;
	        int currentCategory;
	        
	        for (CategoryDetails p : posts) {
	        	currentCategory = Integer.parseInt(p.getCategory_id());
	        	if (currentCategory > tempCategory)
	        		tempCategory = currentCategory;
	        }
	        
	        final int largestCategory = tempCategory;
	                
	        for (CategoryDetails p : posts) {	
	            	        	
	        	final ToggleButton tglBtnByType = new ToggleButton(this); 
	            
	            
	        	/* Create a new row to be added. */
	            tr = new TableRow(this);
	            tr.setPadding(10, 5, 0, 5);
	            
	            colorPos = r % colors.length;
	            tr.setBackgroundColor(colors[colorPos]);
	            
	            tr.setLayoutParams(new LayoutParams(
	                           LayoutParams.FILL_PARENT,
	                           LayoutParams.FILL_PARENT));
	            
	            TextView textType = new TextView(this);
	            textType.setText(p.toString());
	            textType.setWidth(width);
	            textType.setMinimumWidth(width);
	            textType.setTextColor(Color.BLACK);
	            textType.setTypeface(Typeface.SANS_SERIF, Typeface.NORMAL);
	            textType.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
	            textType.setGravity(Gravity.CENTER | Gravity.LEFT); 
	            textType.setId(largestCategory + Integer.parseInt(p.getCategory_id()));
	            textType.setLayoutParams(new LayoutParams(
	                    LayoutParams.FILL_PARENT,
	                    LayoutParams.FILL_PARENT));
	
	            
	            tr.addView(textType);
	            
	            tglBtnByType.setMinimumWidth(50);            
	            tglBtnByType.setTextOn("Show");
	            tglBtnByType.setTextOff("Hide");
	            tglBtnByType.setWidth(50);
	            tglBtnByType.setMaxWidth(50);
	            tglBtnByType.setId(Integer.parseInt(p.getCategory_id()));
	            
	            
	            if(invisibleCategories.contains(p.getCategory_id())) {
	            	tglBtnByType.setChecked(false);
	            	textType.setTextColor(Color.LTGRAY);
	            } else {
	            	tglBtnByType.setChecked(true);
	            	textType.setTextColor(Color.BLACK);
	            }
	            
	                        
	            tglBtnByType.setOnClickListener(new OnClickListener() { 
	            	 public void onClick(View v) {
	                     if (tglBtnByType.isChecked()) {
	                    	 invisibleCategories.remove(Integer.toString(tglBtnByType.getId()));
	                    	 tv = (TextView) findViewById(largestCategory + tglBtnByType.getId());
	                    	 tv.setTextColor(Color.BLACK);
	                     } else {
	                    	 invisibleCategories.add(Integer.toString(tglBtnByType.getId()));
	                    	 tv = (TextView) findViewById(largestCategory + tglBtnByType.getId());
	                    	 tv.setTextColor(Color.LTGRAY);
	                     }
	                 }
	            });
	            
	            tr.addView(tglBtnByType);
	            
	            t2.addView(tr,new TableLayout.LayoutParams(
	            		LayoutParams.FILL_PARENT,
	            		LayoutParams.FILL_PARENT));
	            
	            r++;
	        }
        } else {
        	tr = new TableRow(this);
            tr.setPadding(0, 5, 0, 5);
            tr.setLayoutParams(new LayoutParams(
                           LayoutParams.FILL_PARENT,
                           LayoutParams.FILL_PARENT));
            
            TextView textType = new TextView(this);
            textType.setText("Unavailable at this time.");
            textType.setWidth(width);
            textType.setMinimumWidth(width);
            textType.setTextColor(Color.BLACK);
            textType.setTypeface(Typeface.SANS_SERIF, Typeface.NORMAL);
            textType.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
            textType.setGravity(Gravity.CENTER | Gravity.LEFT); 
            textType.setLayoutParams(new LayoutParams(
                    LayoutParams.FILL_PARENT,
                    LayoutParams.FILL_PARENT));

            
            tr.addView(textType);
            
            t2.addView(tr,new TableLayout.LayoutParams(
            		LayoutParams.FILL_PARENT,
            		LayoutParams.FILL_PARENT));
        }
        
    }
    
    private void saveFilterState() {
    	ApplicationState.saveInvisibleStatus(Utils.objectToString((Serializable) invisibleStatus));
    	ApplicationState.saveInvisibleCategories(Utils.objectToString((Serializable) invisibleCategories));
    }

    @Override
    public void onBackPressed() {
    	
    	saveFilterState();
    	
    	Intent returnIntent = new Intent();
    	setResult(RESULT_OK, returnIntent);
    	FilterActivity.this.finish();

    }
    
 
    @Override
    protected void onSaveInstanceState(Bundle outState) {
    	super.onSaveInstanceState(outState);
    	saveFilterState();
    }
    
    
}














