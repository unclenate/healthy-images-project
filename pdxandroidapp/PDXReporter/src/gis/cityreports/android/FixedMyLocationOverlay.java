/*
 *   Written by Pieter @  android-developers on groups.google.com
 * 
 *   This file is part of OpenGPSTracker.
 *
 *   OpenGPSTracker is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   OpenGPSTracker is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with OpenGPSTracker.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package gis.cityreports.android;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.util.Log;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Projection;

/**
 * Fix for a ClassCastException found on some Google Maps API's implementations.
 * @see <a href="http://www.spectrekking.com/download/FixedMyLocationOverlay.java">www.spectrekking.com</a> 
 * @version $Id$
 */
public class FixedMyLocationOverlay extends MyLocationOverlay {
	private boolean bugged = false;

	private Paint accuracyPaint;
	private Point center;
	private Point left;
	private Drawable drawable;
	private int width;
	private int height;
	private int zoom;
	
	private GeoPoint markedPoint;
	private MapController customMapController;
	private boolean firstrun = true;

	public FixedMyLocationOverlay(Context context, 
			MapView mapView, 
			MapController mapController,
			int zoomlevel,
			GeoPoint savedPoint) {
		
		super(context, mapView);
		customMapController = mapController;
		zoom = zoomlevel;
		markedPoint = savedPoint;
	}


	@Override
	protected void drawMyLocation(Canvas canvas, MapView mapView, Location lastFix, GeoPoint myLoc, long when) {
		if (!bugged) {
			try {
				super.drawMyLocation(canvas, mapView, lastFix, myLoc, when);
				
				if(firstrun && markedPoint == null) {
					customMapController.setCenter(myLoc);
					customMapController.setZoom(zoom);
										
					firstrun = false;
				}
								
			} catch (Exception e) {
				bugged = true;
			}
		}

		if (bugged) {
			if (drawable == null) {
				accuracyPaint = new Paint();
				accuracyPaint.setAntiAlias(true);
				accuracyPaint.setStrokeWidth(2.0f);
				
				drawable = mapView.getContext().getResources().getDrawable(R.drawable.pin);
				//drawable = mapView.getContext().getResources().getDrawable(R.drawable.mylocation);
				
				width = drawable.getIntrinsicWidth();
				height = drawable.getIntrinsicHeight();
				center = new Point();
				left = new Point();
			}
			Projection projection = mapView.getProjection();
			
			double latitude = lastFix.getLatitude();
			double longitude = lastFix.getLongitude();
			float accuracy = lastFix.getAccuracy();
			
			float[] result = new float[1];

			Location.distanceBetween(latitude, longitude, latitude, longitude + 1, result);
			float longitudeLineDistance = result[0];

			if(firstrun && markedPoint == null) {
								
				GeoPoint defaultPoint = new GeoPoint((int)(latitude*1e6), (int)(longitude*1e6));
				customMapController.setCenter(defaultPoint);
				customMapController.setZoom(zoom);
				firstrun = false;
			}
			
			GeoPoint leftGeo = new GeoPoint((int)(latitude*1e6), (int)((longitude-accuracy/longitudeLineDistance)*1e6));
			projection.toPixels(leftGeo, left);
			projection.toPixels(myLoc, center);
			int radius = center.x - left.x;
			
			accuracyPaint.setColor(0xff6666ff);
			accuracyPaint.setStyle(Style.STROKE);
			canvas.drawCircle(center.x, center.y, radius, accuracyPaint);

			accuracyPaint.setColor(0x186666ff);
			accuracyPaint.setStyle(Style.FILL);
			canvas.drawCircle(center.x, center.y, radius, accuracyPaint);
						
			drawable.setBounds(center.x - width / 2, center.y - height / 2, center.x + width / 2, center.y + height / 2);
			drawable.draw(canvas);
		}
	}

}
