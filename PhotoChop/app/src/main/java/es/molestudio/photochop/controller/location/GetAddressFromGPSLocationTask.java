package es.molestudio.photochop.controller.location;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.List;
import java.util.Locale;


/**
 * Created by Chus on 23/09/2014.
 */
public class GetAddressFromGPSLocationTask extends AsyncTask<Location, Void, String> {


    private static final String TAG = "GetAddressTask";
    private Context mContext;
    private GetAddressTaskListener mListener;
    private Exception mException;

    public interface GetAddressTaskListener {
        public void onDone(String address, Exception error);
    }

    public GetAddressFromGPSLocationTask(Context context, GetAddressTaskListener listener) {
        mContext = context;
        mListener = listener;
    }

    /**
     * Get a Geocoder instance, get the latitude and longitude
     * look up the address, and return it
     *
     * @params params One or more Location objects
     * @return A string containing the address of the current
     * location, or an empty string if no address can be found,
     * or an error message
     */
    @Override
    protected String doInBackground(Location... params) {

        Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());

        Location loc = params[0];

        String address = null;

        // Create a list to contain the result address
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(loc.getLatitude(),
                    loc.getLongitude(), 1);

            // If the reverse geocode returned an address
            if (addresses != null && addresses.size() > 0) {
                // Get the first unformattedAdress
                Address unformattedAdress = addresses.get(0);
            /*
             * Format the first line of unformattedAdress (if available),
             * city, and country name.
             */
                address = String.format(
                        "%s, %s, %s",
                        // If there's a street unformattedAdress, add it
                        unformattedAdress.getMaxAddressLineIndex() > 0 ?
                                unformattedAdress.getAddressLine(0) : "",
                        // Locality is usually a city
                        unformattedAdress.getLocality(),
                        // The country of the unformattedAdress
                        unformattedAdress.getCountryName());

            }

        } catch (Exception e) {
            Log.d(TAG, "Error en geocoder", e);
            mException = e;
        }

        if (address == null) {
            mException = null;
            address = getAdressFromLatLonByHttp(loc);
        }

        return address;
    }

    @Override
    protected void onPostExecute(String address) {
        super.onPostExecute(address);
        mListener.onDone(address, null);
    }


    private String getAdressFromLatLonByHttp(Location location) {

        double lat = location.getLatitude();
        double lng = location.getLongitude();

        String returnAddress = null;

        String httpaddress = String.format(Locale.ENGLISH,
                "http://maps.googleapis.com/maps/api/geocode/json?latlng=" + Double.toString(lat) + "," +
                        Double.toString(lng) + "&sensor=true&language=" + Locale.getDefault().getCountry(), lat, lng);

        try {

            HttpGet httpGet = new HttpGet(httpaddress);
            HttpClient client = new DefaultHttpClient();
            HttpResponse response;
            StringBuilder stringBuilder = new StringBuilder();

            response = client.execute(httpGet);
            HttpEntity entity = response.getEntity();
            InputStream stream = entity.getContent();
            int b;
            while ((b = stream.read()) != -1) {
                stringBuilder.append((char) b);
            }

            JSONObject jsonObject = new JSONObject(stringBuilder.toString());

            if("OK".equalsIgnoreCase(jsonObject.getString("status"))) {

                Address mAddress = new Address(Locale.ENGLISH);

                for(int i=1;i<((JSONArray)jsonObject.get("results")).length()-2;i++){

                    JSONArray addrComp = ((JSONArray)jsonObject.get("results")).getJSONObject(i).getJSONArray("address_components");

                    for(int j=0 ; j < addrComp.length() ; j++){

                        String neighborhood = ((JSONArray)((JSONObject)addrComp.get(j)).get("types")).getString(0);
                        if (neighborhood.compareTo("neighborhood") == 0) {
                            String neighborhood1 = ((JSONObject)addrComp.get(j)).getString("long_name");
                            mAddress.setSubThoroughfare(neighborhood1);
                        }
                        String locality = ((JSONArray)((JSONObject)addrComp.get(j)).get("types")).getString(0);
                        if (locality.compareTo("locality") == 0) {
                            String locality1 = ((JSONObject)addrComp.get(0)).getString("long_name");
                            mAddress.setLocality(locality1);
                        }

                        String subadminArea = ((JSONArray)((JSONObject)addrComp.get(j)).get("types")).getString(0);
                        if (locality.compareTo("administrative_area_level_2") == 0) {
                            String subadminArea1 = ((JSONObject)addrComp.get(j)).getString("long_name");
                            mAddress.setSubAdminArea(subadminArea1);
                        }
                        String adminArea = ((JSONArray)((JSONObject)addrComp.get(j)).get("types")).getString(0);
                        if (adminArea.compareTo("administrative_area_level_1") == 0) {
                            String adminArea1 = ((JSONObject)addrComp.get(j)).getString("long_name");
                            mAddress.setAdminArea(adminArea1);
                        }

                        String postalcode = ((JSONArray)((JSONObject)addrComp.get(j)).get("types")).getString(0);
                        if (postalcode.compareTo("postal_code") == 0) {
                            String postalcode1 = ((JSONObject)addrComp.get(j)).getString("long_name");
                            mAddress.setPostalCode(postalcode1);
                        }
                        String sublocality = ((JSONArray)((JSONObject)addrComp.get(j)).get("types")).getString(0);
                        if (sublocality.compareTo("sublocality") == 0) {
                            String sublocality1 = ((JSONObject)addrComp.get(j)).getString("long_name");
                            mAddress.setSubLocality(sublocality1);
                        }
                        String countr = ((JSONArray)((JSONObject)addrComp.get(j)).get("types")).getString(0);
                        if (countr.compareTo("country") == 0) {
                            String countr1 = ((JSONObject)addrComp.get(j)).getString("long_name");

                            mAddress.setCountryName(countr1);
                        }

                    }
                }

                returnAddress = mAddress.getPostalCode() + ", " + mAddress.getCountryName() + ", " + mAddress.getLocality();
                //Log.d(TAG, "getAddressFromHttpTask" + " " + mAddress.getPostalCode() + ", " + mAddress.getCountryName() + ", " + mAddress.getLocality());
            }
        } catch (Exception e) {
            mException = e;
            Log.d(TAG, "Error en getAddressFromHttpTask", e);
        }

        return returnAddress;

    }




}
