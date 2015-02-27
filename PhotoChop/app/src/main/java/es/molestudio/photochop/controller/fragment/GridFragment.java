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
import es.molestudio.photochop.controller.activity.GalleryActivity;
import es.molestudio.photochop.controller.activity.ImageActivity;
import es.molestudio.photochop.controller.activity.SwipeGalleryActivity;
import es.molestudio.photochop.controller.adapter.ADPGridImage;
import es.molestudio.photochop.controller.util.GetImagesFromGalleryTask;
import es.molestudio.photochop.model.Image;

/**
 * Created by Chus on 24/02/15.
 */
public class GridFragment extends Fragment implements AdapterView.OnItemLongClickListener,
        AdapterView.OnItemClickListener,
        GetImagesFromGalleryTask.ImageReaderListener {

    public static final String ARG_SELECTION = "es.molestudio.photochop.controller.fragment.GridFragment.SelectFromGallery";

    private GridView mPhotoGrid;
    private ADPGridImage mADPGridImage;
    private Boolean mIsInSelecionMode = false;

    public static Fragment newInstance(Bundle args) {
        GridFragment fragment = new GridFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public GridFragment() {


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mIsInSelecionMode = getArguments().getBoolean(ARG_SELECTION, false);
        }

        if (mIsInSelecionMode) {
            setHasOptionsMenu(true);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View root = inflater.inflate(R.layout.fragment_grid, container, false);

        mPhotoGrid = (GridView) root.findViewById(R.id.image_grid);

        mADPGridImage = new ADPGridImage(getActivity());

        ArrayList<Image> images = new ArrayList<Image>();

        if (mIsInSelecionMode) {
            // Coger todas las imágenes de la galería
            GetImagesFromGalleryTask getImagesFromGalleryTask = new GetImagesFromGalleryTask(getActivity(), this);
            getImagesFromGalleryTask.execute();
        } else {
            images = DataStorage.getDataStorage(getActivity()).getImages();
            mADPGridImage.updateImages(images);
        }


        mPhotoGrid.setAdapter(mADPGridImage);

        mPhotoGrid.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE);
        mPhotoGrid.setOnItemLongClickListener(this);

        mPhotoGrid.setOnItemClickListener(this);

        return root;
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

        // Si está chequeado
        mPhotoGrid.setItemChecked(position, true);
        view.setAlpha(0.5f);

        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Image image = (Image) mADPGridImage.getItem(position);

        if (mIsInSelecionMode) {
            Intent imageGallery = new Intent(getActivity(), SwipeGalleryActivity.class);
            imageGallery.putExtra(SwipeGalleryActivity.EXTRA_IMAGE_ID, image.getImageId());
            startActivity(imageGallery);
        } else {
            Intent imageDetails = new Intent(getActivity(), ImageActivity.class);
            imageDetails.putExtra(ImageActivity.EXTRA_IMAGE, image);
            startActivity(imageDetails);
        }
    }


    @Override
    public void onProgressUpdate(ArrayList<Image> image) {

    }

    @Override
    public void onFinish(ArrayList<Image> images) {
        mADPGridImage.updateImages(images);
        mADPGridImage.notifyDataSetChanged();
    }
}


