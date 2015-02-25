package es.molestudio.photochop.controller.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;

import es.molestudio.photochop.R;
import es.molestudio.photochop.controller.DataStorage;
import es.molestudio.photochop.controller.activity.ImageActivity;
import es.molestudio.photochop.controller.adapter.ADPGridImage;
import es.molestudio.photochop.model.Image;

/**
 * Created by Chus on 24/02/15.
 */
public class GridFragment extends Fragment {

    public static Fragment newInstance(Bundle args) {
        GridFragment fragment = new GridFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public GridFragment() {


    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View root = inflater.inflate(R.layout.fragment_grid, container, false);

        GridView gridView = (GridView) root.findViewById(R.id.image_grid);

        final ADPGridImage adpGridImage = new ADPGridImage(getActivity());
        ArrayList<Image> images = DataStorage.getDataStorage(getActivity()).getImages();
        adpGridImage.updateImages(images);

        gridView.setAdapter(adpGridImage);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Image image = (Image) adpGridImage.getItem(position);

                Intent imageDetailsIntent = new Intent(getActivity(), ImageActivity.class);
                imageDetailsIntent.putExtra(ImageActivity.IMAGE, image);
                startActivity(imageDetailsIntent);
            }
        });


        return root;
    }
}
