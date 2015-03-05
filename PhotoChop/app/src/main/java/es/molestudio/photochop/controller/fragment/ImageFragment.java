package es.molestudio.photochop.controller.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import es.molestudio.photochop.R;
import es.molestudio.photochop.controller.DataBaseManagerWrap;
import es.molestudio.photochop.controller.util.ImageLoader;
import es.molestudio.photochop.model.Image;

/**
 * Created by Chus on 31/12/14.
 */
public class ImageFragment extends Fragment  {

    public static final String ARG_IMAGE = "es.molestudio.photochop.controller.fragment.ImageFragment.IMAGE";
    public static final String ARG_IMAGE_ID = "es.molestudio.photochop.controller.fragment.ImageFragment.IMAGE_ID";

    private Image mImage;
    private Integer mImageID;

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

        if (getArguments() != null) {
            mImageID = getArguments().getInt(ARG_IMAGE_ID, 0);
            mImage = (Image) getArguments().getSerializable(ARG_IMAGE);
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View root = inflater.inflate(R.layout.fragment_image, container, false);

        final ImageView ivImage = (ImageView) root.findViewById(R.id.iv_image);

        if (mImage == null) {
            mImage = DataBaseManagerWrap.getDataBaseManager(getActivity()).selectImage(mImageID);
        }

        if (mImage != null) {

            if (mImage.isHidden()) {
                ivImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_lock_white_24dp));
            } else {
                new ImageLoader(getActivity()).displayImage(ivImage, mImage.getImageUri());
            }

        }

        return root;
    }



}
