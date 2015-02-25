package es.molestudio.photochop.controller.location;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;

/**
 * Created by Chus on 25/11/14.
 */
public class GetGPSLocationFromAdressTask extends AsyncTask<String, Void, Location> {

    public interface GetAddressTaskListener {
        public void onDone(Location location, Exception error);
    }

    private GetAddressTaskListener mListener;
    private Context mContext;
    private Exception mException = null;

    public GetGPSLocationFromAdressTask(Context context, GetAddressTaskListener listener) {
        this.mListener = listener;
        this.mContext = context;
    }


    @Override
    protected void onPostExecute(Location geoPoint) {
        super.onPostExecute(geoPoint);
        mListener.onDone(geoPoint, mException);
    }

    @Override
    protected Location doInBackground(String... params) {

        double lat= 0.0, lng= 0.0;

        Location location = null;
        String address = params[0];

        Geocoder geoCoder = new Geocoder(mContext, Locale.getDefault());
        try
        {
            List<Address> addresses = geoCoder.getFromLocationName(address , 1);
            if (addresses.size() > 0)
            {
                location = new Location("dummyProvider");
                location.setLatitude((double) addresses.get(0).getLatitude());
                location.setLongitude((double) addresses.get(0).getLongitude());

                Log.d("Latitude", "" + lat);
                Log.d("Longitude", "" + lng);
            }

        }
        catch(Exception e)
        {
            mException = e;
            e.printStackTrace();
        }

        if (location == null) {
            location = getLatLongFromAddressByHttp(address);
        }

        return location;
    }



    private Location getLatLongFromAddressByHttp(String address) {

        String uri = "http://maps.google.com/maps/api/geocode/json?address=" +
                address + "&sensor=false";


        Location location = null;

        HttpGet httpGet = new HttpGet(uri);
        HttpClient client = new DefaultHttpClient();
        HttpResponse response;
        StringBuilder stringBuilder = new StringBuilder();

        try {
            response = client.execute(httpGet);
            HttpEntity entity = response.getEntity();
            InputStream stream = entity.getContent();
            int b;
            while ((b = stream.read()) != -1) {
                stringBuilder.append((char) b);
            }
        } catch (ClientProtocolException e) {
            mException = e;
            e.printStackTrace();
        } catch (IOException e) {
            mException = e;
            e.printStackTrace();
        }

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = new JSONObject(stringBuilder.toString());

            double lng = ((JSONArray)jsonObject.get("results")).getJSONObject(0)
                    .getJSONObject("geometry").getJSONObject("location")
                    .getDouble("lng");

            double lat = ((JSONArray)jsonObject.get("results")).getJSONObject(0)
                    .getJSONObject("geometry").getJSONObject("location")
                    .getDouble("lat");

            location = new Location("dummyProvider");
            location.setLatitude(lat);
            location.setLongitude(lng);

            Log.d("latitude", "" + lat);
            Log.d("longitude", "" + lng);
        } catch (JSONException e) {
            mException = e;
            e.printStackTrace();
        }
        return location;

    }



}
