package es.molestudio.photochop.controller.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.Toast;

/**
 * Created by Chus on 31/12/14.
 */
public class ImageFragment extends Fragment {


    public static final String ARG_IMAGE_ID = "IMAGE_ID";


    public static Fragment newInstance(Bundle args) {

        ImageFragment fragment = new ImageFragment();
        fragment.setArguments(args);
        return fragment;
    }


    public ImageFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Toast.makeText(getActivity(), "Imagen: " + getArguments().get(ARG_IMAGE_ID), Toast.LENGTH_LONG).show();

    }
}
