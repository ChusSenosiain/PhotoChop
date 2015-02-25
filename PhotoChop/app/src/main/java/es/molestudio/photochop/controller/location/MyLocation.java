package es.molestudio.photochop.controller.location;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import es.molestudio.photochop.controller.util.Log;
import es.molestudio.photochop.model.Constants;

/**
 * Created by Chus on 23/02/15.
 */
public class MyLocation implements LocationListener{

    public interface ChangeLocationListener {
        public void onLocationChanged(Location location);
    }

    private LocationManager mLocationManager;
    private Context mContext;
    private ChangeLocationListener mChangeLocationListener;
    private Location mLocation;
    private int mLocationSendCounter = 0;

    public MyLocation(Context context, ChangeLocationListener listener) {

        Log.d("Start location service");
        mContext = context;
        mLocationManager = (LocationManager) mContext.getSystemService(mContext.LOCATION_SERVICE);
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
        mChangeLocationListener = listener;
        mLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        mLocationSendCounter = 0;

        if (mLocation == null) {
            mLocation = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }
        // If the location changes, notify the new location to the listener, but first of all we have
        // to set the last know location
        if (mChangeLocationListener != null) {
            mChangeLocationListener.onLocationChanged(mLocation);
        }

    }

    // Location listener implementation methods

    @Override
    public void onLocationChanged(Location location) {
        Log.d("Location Changed");
        mLocation = location;
        mLocationSendCounter++;
        if (mChangeLocationListener != null) {
            mChangeLocationListener.onLocationChanged(mLocation);
        }

        if (mLocationSendCounter >= Constants.MAX_LOCATION_INTENTS) {
            stopLocationService();
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("Status Changed");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("Provider enabled " + provider);
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d("Provider Disabled " + provider);
    }

    public void stopLocationService() {

        Log.d("Stopped location service");

        if (mLocationManager != null) {
            mLocationManager.removeUpdates(this);
        }

        if (mChangeLocationListener != null) {
            mChangeLocationListener = null;
        }
    }
}
