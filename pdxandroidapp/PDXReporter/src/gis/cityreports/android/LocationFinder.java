package gis.cityreports.android;

import gis.cityreports.data.ConfigSetting;
import gis.cityreports.utils.Utils;

import java.lang.reflect.Field;


import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
//import com.google.android.maps.MapView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


/**
 * @author Roy Cham
 * 
 * Copyright 2010 City of Portland. All rights reserved.
 *
 */
public class LocationFinder extends MapActivity {
	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
	  
	MapController mapController;
	
	static final int STREET_VIEW = 1;
	static final int SATELLITE_VIEW = 2;
	
	static final int MYLOCATION_OFF = 0;
	static final int MYLOCATION_ON = 1;
	
	private int activeOverlay = STREET_VIEW;
	private int mylocationToggle = MYLOCATION_ON;
	private int zoomlevel = 13;
	
	private boolean toggleClick = false;
	private boolean isLoctionFinderRunning = false;
	private boolean isGPSAlertShowing = false;
	
	private double accuracyThreshold;
	private double currentAccuracy;
	
	private Button buttonToggleLayer;
	private CustomMapView myMapView;
	private FixedMyLocationOverlay myLocationOverlay;
	private GeoPoint savedPoint = null;
	private ImageView satelliteCrossHair;
	private ImageView streetCrossHair;
	private LocationManager locationManager;
	private Utils utils = new Utils();
	
	private ImageButton buttonGPSstart;
	

	private int getActiveOverlay() {
		return activeOverlay;
	}

	private int getMylocationToggle() {
		return mylocationToggle;
	}

	private void setActiveOverlay(int activeOverlay) {
		this.activeOverlay = activeOverlay;
	}

	private void setMylocationToggle(int mylocationToggle) {
		this.mylocationToggle = mylocationToggle;
	}
	
	private boolean toggleActiveLayer() {
		
		switch (getActiveOverlay()) {
        case STREET_VIEW:
        	//myMapView.setStreetView(false);
        	myMapView.setSatellite(true);
        	myMapView.setStreetView(false);
        	setActiveOverlay(SATELLITE_VIEW);
        	streetCrossHair.setVisibility(android.view.View.INVISIBLE);
        	satelliteCrossHair.setVisibility(android.view.View.VISIBLE);
        	
        	//buttonToggleLayer.setImageResource(R.drawable.airplane2);
        	buttonToggleLayer.setText("Street");
        	
        	//mapController.setZoom(20);
        	return true;
        case SATELLITE_VIEW:
        	//myMapView.setStreetView(true);
        	myMapView.setSatellite(false);
        	setActiveOverlay(STREET_VIEW);
        	satelliteCrossHair.setVisibility(android.view.View.INVISIBLE);
        	streetCrossHair.setVisibility(android.view.View.VISIBLE);
        	
        	//buttonToggleLayer.setImageResource(R.drawable.map2)
        	buttonToggleLayer.setText("Map");
        	//mapController.setZoom(21);
        	return true;
        }
		return false;
		//myMapView.invalidate();
	}
	
	private boolean toggleMyLocation() {
		
		//ImageButton buttonGPSstart = (ImageButton)findViewById(R.id.btnGPSstart);
		
		switch (getMylocationToggle()) {
		case MYLOCATION_OFF:
			toggleClick = true;
			buttonGPSstart.setImageResource(R.drawable.mylocationenabled); 
			startService();
			setMylocationToggle(MYLOCATION_ON);
			return true;
		case MYLOCATION_ON:
			buttonGPSstart.setImageResource(R.drawable.mylocationdisabled); 
			myMapView.postInvalidate(); // this should clear up any residual overlays
			stopService();
			setMylocationToggle(MYLOCATION_OFF);
			return true;
		}
		
		return false;
	}
	
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    //setContentView(R.layout.location);
       
    ApplicationState appState = ((ApplicationState)getApplicationContext());
    ConfigSetting configSetting = appState.getConfigSetting();  
        
    accuracyThreshold = Double.parseDouble(configSetting.getGpsAccuracyThreshold());
    //Log.d("getGpsAccuracyThreshold()", configSetting.getGpsAccuracyThreshold());
    
    
    Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay(); 
    int width = display.getWidth(); 
    int height = display.getHeight();
    
   
    if (width > height) {
    	setContentView(R.layout.location_landscape);
    } else if (height > 800) {
    	setContentView(R.layout.location);
    } else if (height == 800) {
    	setContentView(R.layout.location);
    } else if (height == 480) {
    	setContentView(R.layout.location_480);
    } else if (height == 320) {
    	setContentView(R.layout.location_320);
    } else {
  		setContentView(R.layout.location);
  	}
    
    //accuracyThreshold = Double.parseDouble(this.getString(R.string.gpsAccuracyThreshold));
    

    
    //Button buttonGPSStop = (Button)findViewById(R.id.btnGPSStop);
    //buttonGPSStop.setOnClickListener(mStopGPSListener);
    
    buttonGPSstart = (ImageButton)findViewById(R.id.btnGPSstart);
    //buttonGPSstart.setOnClickListener(mStartGPSListener);
    
    buttonToggleLayer = (Button)findViewById(R.id.btnLayerToggle);
    buttonToggleLayer.setOnClickListener(mToggleLayerListener);
    
    Button buttonCancel = (Button)findViewById(R.id.btnMapViewCancel);
    buttonCancel.setOnClickListener(mCancelListener);
    
    Button buttonSave = (Button)findViewById(R.id.btnSave);
    buttonSave.setOnClickListener(mSaveListener);
	
    myMapView = (CustomMapView)findViewById(R.id.myMapView);
    
    streetCrossHair = (ImageView)findViewById(R.id.crosshairblack);
    satelliteCrossHair = (ImageView)findViewById(R.id.crosshairyellow);
    
    mapController = myMapView.getController();
        
    myMapView.setSatellite(false);
    myMapView.setBuiltInZoomControls(true);
    myMapView.displayZoomControls(true);
    
    
    Intent gpsIntent = getIntent();
    double defaultLatitude = gpsIntent.getDoubleExtra(Report.CROSS_HAIR_LATITUDE, 0);
    double defaultLongitude = gpsIntent.getDoubleExtra(Report.CROSS_HAIR_LONGITUDE, 0);
    
    zoomlevel = Integer.parseInt(getString(R.string.gpszoomlevel));
    
    if (defaultLatitude != 0 || defaultLongitude != 0 ) {
        Double geoLat = defaultLatitude*1E6;
        Double geoLng = defaultLongitude*1E6;
        
        savedPoint = new GeoPoint(geoLat.intValue(), geoLng.intValue());
        mapController.setCenter(savedPoint);
        mapController.setZoom(zoomlevel);
    }
    		
    LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );
    
    if ( manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
        //Log.d("GPS PROVIDER NOT ALLOWED", " GPS Location provider not allowed");
        //myLocationOverlay = new MyLocationOverlay(this, myMapView);
        myLocationOverlay = new FixedMyLocationOverlay(this, myMapView, mapController, zoomlevel, savedPoint);
        myMapView.getOverlays().add(myLocationOverlay);
        startService();
        isLoctionFinderRunning = true;
        buttonGPSstart.setOnClickListener(mStartGPSListener);
        
    } else {

    	if (savedPoint == null) {
	    	defaultLatitude = Double.parseDouble(getString(R.string.defaultLatitude));
	    	defaultLongitude = Double.parseDouble(getString(R.string.defaultLongitude));
	    	
	    	Double geoLat = defaultLatitude*1E6;
	      	Double geoLng = defaultLongitude*1E6;
	      
	      	zoomlevel = Integer.parseInt(getString(R.string.defaultzoomlevel));
	      	GeoPoint defaultPoint = new GeoPoint(geoLat.intValue(), geoLng.intValue());
	      
	      	mapController.setCenter(defaultPoint);
	      	
    	} else {
    		mapController.setCenter(savedPoint);
    	}
    	mapController.setZoom(zoomlevel);
    	
    	isLoctionFinderRunning = false;
    	buttonGPSstart.setImageResource(R.drawable.mylocationdisabled);
    	buttonGPSstart.setBackgroundResource(R.drawable.generic_map_highlight_button);
    }
    
                                          
  }
  
 
  private void enableOverlayCriteria() {
	  if (myLocationOverlay != null) {
		  //myLocationOverlay.enableCompass();
		  myLocationOverlay.enableMyLocation();
	  }
  }

  private OnClickListener mCancelListener = new OnClickListener() {
      public void onClick(View v) {
    	  stopService();
    	  finish();
      }
  };

  private OnClickListener mSaveListener = new OnClickListener() {
        public void onClick(View v) {
          
        	GeoPoint mapCenter = myMapView.getMapCenter();
        	
        	double crossHairLatitude = ((int)mapCenter.getLatitudeE6())/1.0E6;
        	double crossHairLongitude = ((int)mapCenter.getLongitudeE6()/ 1.0E6);
        	        	
        	boolean result = utils.locationIsInside(crossHairLatitude, crossHairLongitude);
        	if(!result) {
        		disclaimerPopup(getString(R.string.invalidLocation));
        		return;
        	}
        		
        	Intent returnIntent = new Intent();
        	returnIntent.putExtra(Report.CROSS_HAIR_LATITUDE, crossHairLatitude);
        	returnIntent.putExtra(Report.CROSS_HAIR_LONGITUDE, crossHairLongitude);
        	setResult(RESULT_OK, returnIntent);
        	finish();
        }
    };

  private OnClickListener mStartGPSListener = new OnClickListener() {
      public void onClick(View v) {
    	  
    	  if (isLoctionFinderRunning)
    		  toggleMyLocation();
    	  
      }
  };
  
  public void alertNoGPS() {
		
	  isGPSAlertShowing = true;
	  
	  View customDialogView = View.inflate(this, R.layout.custom_dialog, null);
	  TextView customTextView = (TextView) customDialogView.findViewById(R.id.customDialogText);
	  customTextView.setMinWidth(300);

	  customTextView.setText("GPS Not Enabled");
	  customTextView.setMovementMethod(LinkMovementMethod.getInstance());

	  Linkify.addLinks(customTextView, Linkify.WEB_URLS | Linkify.PHONE_NUMBERS | Linkify.EMAIL_ADDRESSES);

	  AlertDialog.Builder builder = new AlertDialog.Builder(this);
	  builder.setView(customDialogView).setCancelable(false)
	  			.setPositiveButton("Turn on GPS",
	  					new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.dismiss();
								isGPSAlertShowing = false;
								try {
									Intent i = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
									startActivity(i);						
								} catch (android.content.ActivityNotFoundException ax) {									
								}					
							}		
						}).setNegativeButton("Ignore",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.dismiss();
								isGPSAlertShowing = false;
							}
						});

	  AlertDialog alert = builder.create();
	  alert.show();
	}

  private void disclaimerPopup(String disclaimer)
  {
  	if(disclaimer != null && disclaimer.trim() != "") {
  		
      	View customDialogView = View.inflate(this, R.layout.custom_dialog, null); 
          TextView customTextView = (TextView) customDialogView.findViewById(R.id.customDialogText); 
      	
      	customTextView.setText(disclaimer); 
      	customTextView.setMovementMethod(LinkMovementMethod.getInstance()); 
      	Linkify.addLinks( customTextView, Linkify.WEB_URLS | Linkify.PHONE_NUMBERS | Linkify.EMAIL_ADDRESSES);
  		
		   	AlertDialog.Builder builder = new AlertDialog.Builder(this);
		   	//setLinks(disclaimer);	
		   	builder.setView(customDialogView)
		   			.setCancelable(false)
			    	.setPositiveButton("OK", new DialogInterface.OnClickListener() {
		    			public void onClick(DialogInterface dialog, int id) {
		            	 	dialog.dismiss();
		               }
		    		});
		    			
		   	AlertDialog alert = builder.create();
		    alert.show();
  	}
  }

  
  private OnClickListener mToggleLayerListener = new OnClickListener() {
      public void onClick(View v) {
    	  toggleActiveLayer();
      }
  };
 
  
  private final LocationListener locationListener = new LocationListener() {
    public void onLocationChanged(Location location) {
      updateWithNewLocation(location);
    }
	 
    public void onProviderDisabled(String provider){
      updateWithNewLocation(null);
    }

    public void onProviderEnabled(String provider){ }
    public void onStatusChanged(String provider, int status, 
                                Bundle extras){ }
  };
  
  	
	@Override
	protected void onDestroy() {
		
		Class<?> tileClass;
		try {
			
			stopService();
			
			/*
			myMapView.destroyDrawingCache();
			myMapView.invalidate();
			
			mapController = null;
			myMapView = null;
			*/
			
			mCancelListener = null;
			mSaveListener = null;
			myLocationOverlay = null;
			locationManager = null;
            
			
            Field fConverterInView = MapView.class.getDeclaredField("mConverter");
            fConverterInView.setAccessible(true);
            fConverterInView.set(myMapView, null);
            
            Field fControllerInView = MapView.class.getDeclaredField("mController");
            fControllerInView.setAccessible(true);
            fControllerInView.set(myMapView, null);
             
            Field fZoomHelperInView = MapView.class.getDeclaredField("mZoomHelper");
            fZoomHelperInView.setAccessible(true);
            fZoomHelperInView.set(myMapView, null);
			
			tileClass = Class.forName("com.google.googlenav.map.Tile");
			Field fTileCache = tileClass.getDeclaredField("tileObjectCache");
			fTileCache.setAccessible(true);
			Object[] tileObjectCache = (Object[]) fTileCache.get(null);
			for (int i=0; i<tileObjectCache.length; i++) {
				tileObjectCache[i] = null;
			}
			
			setContentView(R.layout.blank);
			
			System.gc();
			
			super.onDestroy();
			
			
			
			/*
			Field fMapInActivity = MapActivity.class.getDeclaredField("mMap");
			fMapInActivity.setAccessible(true);
			fMapInActivity.set((MapActivity) this, null);
			*/
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			Log.e("ERROR", e.toString());
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
			Log.e("ERROR", e.toString());
			
		} catch (SecurityException e) {
			e.printStackTrace();
			Log.e("ERROR", e.toString());
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("ERROR", e.toString());
		}
			

		
		System.gc();
		
		try {
			
			Field fMapInActivity = MapActivity.class.getDeclaredField("mMap");
			fMapInActivity.setAccessible(true);
			fMapInActivity.set((MapActivity) this, null);
			
			super.onDestroy();
		} catch(Exception e) {
			Log.e("MAP ERROR", e.toString());
		}
	}
	
	
	@Override
	protected void onPause() {
		super.onPause();
		
		stopService();
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
	    LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );
	    //ImageButton buttonGPSstart = (ImageButton)findViewById(R.id.btnGPSstart);
	    
	    if ( manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
	        //Log.d("GPS PROVIDER NOT ALLOWED", " GPS Location provider not allowed");
	    	
	    	if (myLocationOverlay == null) {
	    		myLocationOverlay = new FixedMyLocationOverlay(this, myMapView, mapController, zoomlevel, savedPoint);
	    		myMapView.getOverlays().add(myLocationOverlay);
	    	}
	    	startService();
	        isLoctionFinderRunning = true;
	        
	        buttonGPSstart.setOnClickListener(mStartGPSListener);
	        
	        //Toast.makeText(LocationFinder.this, "MY LOCATION ON", Toast.LENGTH_SHORT).show();
	        
	        setMylocationToggle(MYLOCATION_ON);
	    	buttonGPSstart.setImageResource(R.drawable.mylocationenabled);
	    	buttonGPSstart.setBackgroundResource(R.drawable.generic_map_button);
	        
	    } else {
	    	isLoctionFinderRunning = false;
	    	
	    	//Toast.makeText(LocationFinder.this, "MY LOCATION OFF", Toast.LENGTH_SHORT).show();
	        
	    	setMylocationToggle(MYLOCATION_OFF);
	    	buttonGPSstart.setImageResource(R.drawable.mylocationdisabled);
	    	buttonGPSstart.setBackgroundResource(R.drawable.generic_map_highlight_button);
	    	
	    	if (!isGPSAlertShowing)
	    		alertNoGPS();
	    	
	    }
	    
	}

	
	public void startService() 
	{
		locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		
		
	    Criteria criteria = new Criteria();
	    criteria.setAccuracy(Criteria.ACCURACY_COARSE);
	    criteria.setAltitudeRequired(false);
	    criteria.setBearingRequired(false);
	    criteria.setSpeedRequired(false);
	    criteria.setCostAllowed(true);
	    criteria.setPowerRequirement(Criteria.POWER_HIGH);
	    
	    String provider = locationManager.getBestProvider(criteria, true);

	    
		locationManager.requestLocationUpdates(provider, 0, 0, locationListener);
		//locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 10, locationListener);
		
		
		enableOverlayCriteria();
		
		
	}
	
	public void stopService() 
	{
		
		if (myLocationOverlay != null) {
			if (myLocationOverlay.isMyLocationEnabled())
				myLocationOverlay.disableMyLocation();
			
			//myLocationOverlay = null;
		}
      			
		if (locationManager != null) {
			locationManager.removeUpdates(locationListener);
		}
	}

	private void updateWithNewLocation(Location location) {
    
		if (location != null) {
		    
			if (toggleClick) 
			{
				
				Double geoLat = location.getLatitude()*1E6;
				Double geoLng = location.getLongitude()*1E6;
			    
			    GeoPoint point = new GeoPoint(geoLat.intValue(), geoLng.intValue());
				
				mapController.setCenter(point);
				mapController.setZoom(Integer.parseInt(getString(R.string.gpszoomlevel)));
										    							
				/*
				boolean result = utils.locationIsInside(currentLatitude, currentLongitude);
								
				if(!result) {
					geoLat = Double.parseDouble(getString(R.string.defaultLatitude))*1E6;
					geoLng = Double.parseDouble(getString(R.string.defaultLongitude))*1E6;
    		        		      		    
					mapController.setCenter(new GeoPoint(geoLat.intValue(), geoLng.intValue()));
					mapController.setZoom(Integer.parseInt(getString(R.string.defaultzoomlevel)));
    		    
				} else {
					
					geoLat = currentLatitude*1E6;
				    geoLng = currentLongitude*1E6;
				    
				    GeoPoint point = new GeoPoint(geoLat.intValue(), geoLng.intValue());
					
					mapController.setCenter(point);
					mapController.setZoom(Integer.parseInt(getString(R.string.gpszoomlevel)));
				}
				*/
			
				toggleClick = false;
			}

		} 
	}

  
  
}