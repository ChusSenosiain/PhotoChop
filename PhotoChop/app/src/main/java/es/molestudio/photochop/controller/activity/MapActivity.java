package es.molestudio.photochop.controller.activity;

import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import es.molestudio.photochop.R;
import es.molestudio.photochop.controller.DataStorage;
import es.molestudio.photochop.controller.util.Log;
import es.molestudio.photochop.model.Image;

public class MapActivity extends ActionBarActivity
implements OnMapReadyCallback,
        GoogleMap.OnMarkerDragListener{

    public static final String IMAGE = "es.molestudio.photochop.controller.activity.MapActivity.Image";
    public static final int RQ_LOCATION_SELECTED = 1001;
    public static final String NEW_LOCATION = "es.molestudio.photochop.controller.activity.MapActivity.New_location";

    private GoogleMap mMap;
    private Image mImage;
    private ArrayList<Integer> mImageIDs = new ArrayList<Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        // Set up toolbar as actionbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.tb_toolbar);
        setSupportActionBar(toolbar);

        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);



        mImage = (Image) getIntent().getSerializableExtra(IMAGE);

        SupportMapFragment mapFragment = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map));
        mapFragment.getMapAsync(this);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:

                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (mMap == null) {
            return;
        }
        loadMap();
    }

    private void loadMap() {

        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);

        if (mImage != null) {
            // Mostrar solo una imagen
            drawMarkOnMap(mImage);
            mMap.setOnMarkerDragListener(this);
        } else {
            // Mostrar todas las imagenes de la galeria
            ArrayList<Image> images = DataStorage.getDataStorage(this).getImages();
            if (images != null) {
                for (Image image: images) {
                    drawMarkOnMap(image);
                }
            }
        }
    }


    private void drawMarkOnMap(Image image) {

        // Obtener coordenadas de la imagen
        final LatLng latLng = new LatLng(image.getImageLatitude(), image.getImageLongitude());
                mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .draggable(true)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA))
                .title(image.getImageName()));

        // Hacer zoom
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));


        // Obtener thumbnail de la imagen
        /*new CreateThumbnailFromImageTask(this, new CreateThumbnailFromImageTask.ThumbCreationListener() {
            @Override
            public void onImageCreated(Bitmap bitmap, Exception error) {

                // crear marca
                mMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .draggable(true)
                        .icon(BitmapDescriptorFactory.fromBitmap(bitmap))
                        .title("Marker"));

                // Hacer zoom
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));

            }
        }).execute(image.getImageUri().toString());
        */

    }


    // OnMarkerDragListener implementation
    @Override
    public void onMarkerDragStart(Marker marker) {
        Log.d("OnMarkerDragStart: " +  marker.getPosition().latitude + " " + marker.getPosition().longitude);
    }

    @Override
    public void onMarkerDrag(Marker marker) {
        Log.d("OnMarkerDrag: " +  marker.getPosition().latitude + " " + marker.getPosition().longitude);
    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        Log.d("OnMarkerDragEnd: " +  marker.getPosition().latitude + " " + marker.getPosition().longitude);
        mImage.setImageLatitude(marker.getPosition().latitude);
        mImage.setImageLongitude(marker.getPosition().longitude);
    }


}
