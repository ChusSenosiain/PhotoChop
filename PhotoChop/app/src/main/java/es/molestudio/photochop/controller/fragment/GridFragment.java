package es.molestudio.photochop.controller.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.parse.ParseUser;

import java.util.ArrayList;

import es.molestudio.photochop.R;
import es.molestudio.photochop.View.AppTextView;
import es.molestudio.photochop.controller.DataBaseManagerWrap;
import es.molestudio.photochop.controller.ImageManager;
import es.molestudio.photochop.controller.ImageManagerImpl;
import es.molestudio.photochop.controller.Images;
import es.molestudio.photochop.controller.activity.ImageActivity;
import es.molestudio.photochop.controller.activity.SwipeGalleryActivity;
import es.molestudio.photochop.controller.adapter.ADPGridImage;
import es.molestudio.photochop.controller.util.GetImagesFromGalleryTask;
import es.molestudio.photochop.model.Constants;
import es.molestudio.photochop.model.Image;

/**
 * Created by Chus on 24/02/15.
 */
public class GridFragment extends Fragment implements GetImagesFromGalleryTask.ImageReaderListener,
        AdapterView.OnItemLongClickListener,
        AdapterView.OnItemClickListener,
        ADPGridImage.ItemSelectorListener,
        ActionMode.Callback{


    public static final String ARG_SELECT_FROM_GALLERY = "es.molestudio.photochop.controller.fragment.GridFragment.ARG_SELECT_FROM_GALLERY";
    public static final String NEW_IMAGES_ON_BD = "es.molestudio.photochop.controller.activity.GridFragment.NEW_IMAGES_ON_BD";


    private GridView mPhotoGrid;
    private ADPGridImage mADPGridImage;
    private Boolean mSelectImagesFromGallery = false;
    private ActionMode mActionMode;
    private ActionBar mActionBar;
    private int mSelectedItemsCount;
    private boolean mShowHiddenImages = false;
    private int mCounter;
    private int mCounterSelected;
    private ProgressBar mProgressBar;
    private AppTextView mTvNoImages;


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
            mSelectImagesFromGallery = getArguments().getBoolean(ARG_SELECT_FROM_GALLERY, false);
        }

        setHasOptionsMenu(true);
        setRetainInstance(true);
   }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View root = inflater.inflate(R.layout.fragment_grid, container, false);

        // Preferences: show or hide secret images
        SharedPreferences settings = getActivity().getSharedPreferences(Constants.TAG_PREFERENCES, 0);
        mShowHiddenImages = settings.getBoolean(Constants.TAG_SHOW_HIDDEN, false);


        mActionBar = ((ActionBarActivity) getActivity()).getSupportActionBar();
        mTvNoImages = (AppTextView) root.findViewById(R.id.tv_no_images);


        mPhotoGrid = (GridView) root.findViewById(R.id.image_grid);
        mProgressBar = (ProgressBar) root.findViewById(R.id.progressBar);

        mADPGridImage = new ADPGridImage(getActivity(), mPhotoGrid, this);
        mPhotoGrid.setAdapter(mADPGridImage);

        mPhotoGrid.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE);
        mPhotoGrid.setOnItemLongClickListener(this);
        mPhotoGrid.setOnItemClickListener(this);

        mTvNoImages.setVisibility(View.GONE);

        if (mSelectImagesFromGallery) {
            // Images From Gallery
            GetImagesFromGalleryTask getImagesFromGalleryTask = new GetImagesFromGalleryTask(getActivity(), this);
            getImagesFromGalleryTask.execute();
        } else {
            // Images From BD
            showHiddenImages();
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

        if (!mSelectImagesFromGallery) {
            Intent imageGallery = new Intent(getActivity(), SwipeGalleryActivity.class);
            imageGallery.putExtra(SwipeGalleryActivity.EXTRA_IMAGE_POSITION, position);
            startActivityForResult(imageGallery, SwipeGalleryActivity.RQ_CHANGE_IMAGES);
        } else {
            Intent imageDetails = new Intent(getActivity(), ImageActivity.class);
            imageDetails.putExtra(ImageActivity.EXTRA_IMAGE, image);
            startActivity(imageDetails);
        }
    }

    // GetImagesFromGalleryTask.ImageReaderListener implementation
    @Override
    public void onFinishReadFromGallery(ArrayList<Image> images) {
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
                    if (mSelectImagesFromGallery) {
                        inflater.inflate(R.menu.menu_images_selection, menu);
                    } else {
                        inflater.inflate(R.menu.menu_images_main, menu);
                    }
                } else if (selectedItemCount > 1 && mSelectedItemsCount == 1 && mSelectImagesFromGallery) {
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
                break;
            case R.id.action_delete:
                deleteImagesFromBD(mADPGridImage.getSelectedImages());
                break;
            case R.id.action_hide:
                mCounterSelected = mADPGridImage.getSelectedImages().size();
                setHiddenforSelectedImages(true);
                break;
            case R.id.action_show:
                mCounterSelected = mADPGridImage.getSelectedImages().size();
                setHiddenforSelectedImages(false);
                break;
        }

        mActionMode.finish();

        return true;
    }

    // Create images from gallery to BD
    private void importImagesToBD() {

        mProgressBar.setVisibility(View.VISIBLE);
        ArrayList<Image> newImages = DataBaseManagerWrap.getDataBaseManager(getActivity()).insertImages(mADPGridImage.getSelectedImages());

        if (newImages.size() > 0) {
            Intent returnIntent = new Intent();
            returnIntent.putExtra(NEW_IMAGES_ON_BD, true);
            getActivity().setResult(Activity.RESULT_OK, returnIntent);
        }
        mProgressBar.setVisibility(View.GONE);

        getActivity().finish();

    }

    // Delete images from DB
    private void deleteImagesFromBD(final ArrayList<Image> images) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(getActivity().getString(R.string.delete_image_question))
                .setTitle(getActivity().getString(R.string.delete_image));

        builder.setPositiveButton(getActivity().getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mProgressBar.setVisibility(View.VISIBLE);
                DataBaseManagerWrap.getDataBaseManager(getActivity()).deleteImages(images);
                updateImagesFromBD(false);
                mProgressBar.setVisibility(View.GONE);
            }
        });

        builder.setNegativeButton(getActivity().getString(R.string.no), null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // Hide or show the selected images
    private void setHiddenforSelectedImages(boolean hide) {

        if (ParseUser.getCurrentUser() == null) {
            Toast.makeText(getActivity(), getString(R.string.txt_error_user_not_logged), Toast.LENGTH_LONG).show();
            return;
        }

        mProgressBar.setVisibility(View.VISIBLE);
        ImageManager imageManager = new ImageManagerImpl(getActivity());

        mCounter = 0;

        for (Image image: mADPGridImage.getSelectedImages()) {
            if (hide) {
                imageManager.hideImage(image, new ImageManager.ImageManagerListener() {
                    @Override
                    public void onFinish(Image image, Exception err) {
                        mCounter++;
                        if (mCounter == mCounterSelected) {
                            showHiddenImages();
                            mProgressBar.setVisibility(View.GONE);
                        }
                    }
                });
            } else {

                imageManager.showImage(image, new ImageManager.ImageManagerListener() {
                    @Override
                    public void onFinish(Image image, Exception err) {
                        mCounter++;
                        if (mCounter == mCounterSelected) {
                            showHiddenImages();
                            mProgressBar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        }

    }


    @Override
    public void onDestroyActionMode(ActionMode actionMode) {
        if (mActionMode != null) {
            mActionMode = null;
            mADPGridImage.unCheckAll();
        }

    }

    // Load the images on Images singleton
    public void updateImagesFromBD(Boolean scrollToLastPosition) {
        updateImages(Images.reloadImagesFromBD(getActivity()));
        if (scrollToLastPosition) {
            mPhotoGrid.smoothScrollToPosition(mADPGridImage.getCount());
        }
    }

    // Update the gridview with the images
    private void updateImages(ArrayList<Image> images) {

        mADPGridImage.updateImages(images);
        mADPGridImage.notifyDataSetChanged();

        if (mADPGridImage.getCount() == 0 && !mSelectImagesFromGallery) {
            mTvNoImages.setVisibility(View.VISIBLE);
        } else {
            mTvNoImages.setVisibility(View.GONE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Select images from gallery
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SwipeGalleryActivity.RQ_CHANGE_IMAGES) {
                updateImagesFromBD(false);
            }
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_show_hidden:
                showOrHideImagesMenuClick();
                break;

        }

        return super.onOptionsItemSelected(item);
    }


    private void showOrHideImagesMenuClick() {

        mShowHiddenImages = (!mShowHiddenImages);
        getActivity().invalidateOptionsMenu();

        SharedPreferences settings = getActivity().getSharedPreferences(Constants.TAG_PREFERENCES, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(Constants.TAG_SHOW_HIDDEN, mShowHiddenImages);
        editor.commit();

        showHiddenImages();

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if (!mSelectImagesFromGallery) {
            inflater.inflate(R.menu.menu_grid_fragment, menu);
        }
    }


    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (!mSelectImagesFromGallery) {
            // If true: las imagenes se ven
            if (mShowHiddenImages) {
                menu.getItem(0).setIcon(getResources().getDrawable(R.drawable.ic_visibility_off_white_24dp));
            } else {
                menu.getItem(0).setIcon(getResources().getDrawable(R.drawable.ic_visibility_white_24dp));
            }
        }
    }

    private void showHiddenImages() {

        // Mostrar todas las imágenes
        if (mShowHiddenImages) {
            updateImagesFromBD(false);
        }
        // No mostrar las ocultas
        else {
            ArrayList<Image> visibleImages = new ArrayList<Image>();
            for (Image image: Images.getInstance(getActivity())) {
                if (!image.isHidden()) {
                    visibleImages.add(image);
                }
            }
            Images.reloadImages(visibleImages);
            updateImages(visibleImages);
        }

    }


}


