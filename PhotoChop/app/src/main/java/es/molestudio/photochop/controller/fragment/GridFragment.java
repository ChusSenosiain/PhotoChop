package es.molestudio.photochop.controller.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import es.molestudio.photochop.R;
import es.molestudio.photochop.controller.DataStorage;
import es.molestudio.photochop.controller.activity.ImageActivity;
import es.molestudio.photochop.controller.activity.SwipeGalleryActivity;
import es.molestudio.photochop.controller.adapter.ADPGridImage;
import es.molestudio.photochop.controller.util.GetImagesFromGalleryTask;
import es.molestudio.photochop.controller.util.ImageLoader;
import es.molestudio.photochop.controller.util.Log;
import es.molestudio.photochop.model.Image;

/**
 * Created by Chus on 24/02/15.
 */
public class GridFragment extends Fragment implements GetImagesFromGalleryTask.ImageReaderListener,
        AdapterView.OnItemLongClickListener,
        AdapterView.OnItemClickListener,
        ADPGridImage.ItemSelectorListener,
        ActionMode.Callback{


    public static final String ARG_SELECTION = "es.molestudio.photochop.controller.fragment.GridFragment.SelectFromGallery";

    private GridView mPhotoGrid;
    private ADPGridImage mADPGridImage;
    private Boolean mIsInSelecionMode = false;    private ActionMode mActionMode;
    private ActionBar mActionBar;
    private int mSelectedItemsCount;


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

        setHasOptionsMenu(true);
   }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View root = inflater.inflate(R.layout.fragment_grid, container, false);

        mActionBar = ((ActionBarActivity) getActivity()).getSupportActionBar();

        mPhotoGrid = (GridView) root.findViewById(R.id.image_grid);

        mADPGridImage = new ADPGridImage(getActivity(), this);
        mPhotoGrid.setAdapter(mADPGridImage);

        mPhotoGrid.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE);
        mPhotoGrid.setOnItemLongClickListener(this);
        mPhotoGrid.setOnItemClickListener(this);

        ArrayList<Image> images = new ArrayList<Image>();

        if (mIsInSelecionMode) {
            // Images From Gallery
            GetImagesFromGalleryTask getImagesFromGalleryTask = new GetImagesFromGalleryTask(getActivity(), this);
            getImagesFromGalleryTask.execute();
        } else {
            // Images From BD
            images = DataStorage.getDataStorage(getActivity()).getImages();
            updateImages(images);
        }

        return root;
    }

    // ///////////////////////////////////////////////////////////////// //
    // INTERFACE IMPLEMENTATION
    // ///////////////////////////////////////////////////////////////// //

    // AdapterView.OnItemLongClickListener
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        CheckBox chkChecked = (CheckBox) view.findViewById(R.id.chk_selection);
        chkChecked.setChecked(!mADPGridImage.isItemSelected(position));
        return true;
    }

    // AdapterView.OnItemClickListener implementation
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Image image = (Image) mADPGridImage.getItem(position);

        if (!mIsInSelecionMode) {
            Intent imageGallery = new Intent(getActivity(), SwipeGalleryActivity.class);
            imageGallery.putExtra(SwipeGalleryActivity.EXTRA_IMAGE_ID, image.getImageId());
            startActivity(imageGallery);
        } else {
            Intent imageDetails = new Intent(getActivity(), ImageActivity.class);
            imageDetails.putExtra(ImageActivity.EXTRA_IMAGE, image);
            startActivity(imageDetails);
        }
    }

    // GetImagesFromGalleryTask.ImageReaderListener implementation
    @Override
    public void onProgressUpdate(ArrayList<Image> image) {

    }

    @Override
    public void onFinish(ArrayList<Image> images) {
        updateImages(images);
    }

    // ADPGridImage.ItemSelectorListener implementation
    @Override
    public void onItemSelected(int selectedItemCount) {

        switch (selectedItemCount) {
            case 0:
                if (mActionMode != null) {
                    mActionMode.finish();
                }
                break;
            default:
                if (mActionMode == null) {
                    mActionMode = mActionBar.startActionMode(this);
                }

                Menu menu = mActionMode.getMenu();
                mActionMode.setTitle(selectedItemCount + " Selected");
                MenuInflater inflater = mActionMode.getMenuInflater();

                // 1 o varios items
                if (selectedItemCount == 1 && mSelectedItemsCount != 1) {
                    menu.clear();
                    if (mIsInSelecionMode) {
                        inflater.inflate(R.menu.menu_images_selection, menu);
                    } else {
                        inflater.inflate(R.menu.menu_images_main, menu);
                    }
                } else if (selectedItemCount > 1 && mSelectedItemsCount == 1 && mIsInSelecionMode) {
                    menu.clear();
                    inflater.inflate(R.menu.menu_images_multiple_selection, menu);
                }
                break;
        }

        mSelectedItemsCount = selectedItemCount;

    }


    // ActionMode.Callback implementation
    @Override
    public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {

        switch (menuItem.getItemId()) {
            case R.id.action_done:
                importImagesToBD();
                getActivity().finish();
                break;
            case R.id.action_delete:
                deleteImagesFromBD();
                break;
            case R.id.action_hide:
                hideImages();
                break;
        }

        return true;
    }

    private void importImagesToBD() {
        DataStorage.getDataStorage(getActivity()).insertImages(mADPGridImage.getSelectedImages());
    }

    private void deleteImagesFromBD() {
        DataStorage.getDataStorage(getActivity()).deleteImages(mADPGridImage.getSelectedImages());
    }

    private void hideImages() {
        ArrayList<Image> hiddenImages = new ArrayList<Image>();
        for (Image image: mADPGridImage.getSelectedImages()) {
            image.setHidden(true);
        }
        DataStorage.getDataStorage(getActivity()).updateImages(hiddenImages);
    }


    @Override
    public void onDestroyActionMode(ActionMode actionMode) {
        if (mActionMode != null) {
            mActionMode = null;
            mADPGridImage.unCheckAll();
        }

    }


    @Override
    public void onResume() {
        super.onResume();
        // Comprobar si ha habido cambios en la BD
        ArrayList<Image> images = DataStorage.getDataStorage(getActivity()).getImages();
        updateImages(images);
    }


    private void updateImages(ArrayList<Image> images) {
        mADPGridImage.updateImages(images);
        mADPGridImage.notifyDataSetChanged();
    }
}


