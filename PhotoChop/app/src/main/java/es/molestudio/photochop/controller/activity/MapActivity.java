package es.molestudio.photochop.controller.activity;

import android.graphics.Bitmap;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import es.molestudio.photochop.R;
import es.molestudio.photochop.controller.DataStorage;
import es.molestudio.photochop.controller.util.CreateThumbnailFromImageTask;
import es.molestudio.photochop.model.Image;

public class MapActivity extends ActionBarActivity
implements OnMapReadyCallback {

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

        mImage = (Image) getIntent().getSerializableExtra(IMAGE);

        SupportMapFragment mapFragment = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map));
        mapFragment.getMapAsync(this);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_map, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
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

        // Obtener thumbnail de la imagen
        new CreateThumbnailFromImageTask(this, new CreateThumbnailFromImageTask.ThumbCreationListener() {
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

    }
}
