package org.codeforhealth.hip.android;

import android.content.Context;
import android.util.AttributeSet;
import com.google.android.maps.MapView;

/**
 * @author Roy Cham
 * 
 * Copyright 2010 City of Portland. All rights reserved.
 *
 */
public class CustomMapView extends MapView {

	
	public CustomMapView(Context context, String apiKey) {
		super(context, apiKey);
		// TODO Auto-generated constructor stub
	}

	public CustomMapView(Context context, AttributeSet attrs) {
		super(context, attrs);

	}

	public CustomMapView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

}
