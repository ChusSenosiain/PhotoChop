package es.molestudio.photochop.controller.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import es.molestudio.photochop.R;
import es.molestudio.photochop.controller.util.ImageLoader;
import es.molestudio.photochop.controller.util.Log;
import es.molestudio.photochop.model.Image;

/**
 * Created by Chus on 24/02/15.
 */
public class ADPGridImage extends BaseAdapter  {

    private ArrayList<Image> mImages = new ArrayList<Image>();
    private HashMap<Integer, Boolean> mSelectedItems = new HashMap<Integer, Boolean>();
    private Context mContext;
    private LayoutInflater mInflater;
    private boolean mIsInSelectionMode = false;
    private ItemSelectorListener mListener;
    private ImageLoader mImageLoader;
    private View mView;


    public interface ItemSelectorListener {
        public void onItemSelected(int selectedItemCount);
    }


    public ADPGridImage(Context context, View view, ItemSelectorListener listener) {
        mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mListener = listener;
        mImageLoader = new ImageLoader(mContext);
        mView = view;

    }


    public void updateImages(ArrayList<Image> images) {

        mImages = images;
        if (mImages == null){
            mImages = new ArrayList<Image>();
        }
    }

    public void addImages(ArrayList<Image> images) {
        for (Image image: images) {
            addImage(image);
        }
    }

    private void setSelecionMode() {

        boolean isInSelectionMode = false;
        int countSelected = 0;

        if (mSelectedItems == null) {
           mSelectedItems = new HashMap<Integer, Boolean>();
        }

        for (boolean checkedItem: mSelectedItems.values()) {
            if (checkedItem) {
                isInSelectionMode = true;
                countSelected ++;
            }
        }

        if (mListener != null) {
            mListener.onItemSelected(countSelected);
        }

        if (isInSelectionMode != mIsInSelectionMode) {
            mIsInSelectionMode = isInSelectionMode;
            changeVisibleViews();
        }
    }


    private void changeVisibleViews(){

        GridView gridView = (GridView) mView;
        if (gridView != null) {

            for (int i = 0; i <= gridView.getLastVisiblePosition() - gridView.getFirstVisiblePosition(); i ++) {
                View view = gridView.getChildAt(i);
                CheckBox chk = (CheckBox) view.findViewById(R.id.chk_selection);
                chk.setVisibility(mIsInSelectionMode ? View.VISIBLE : View.INVISIBLE);
            }
        }
    }


    public void unCheckAll() {
        mSelectedItems = null;
        setSelecionMode();
    }

    public boolean isItemSelected(int position) {

        boolean isItemSelected = false;

        try {
            isItemSelected = mSelectedItems.get(position);
        } catch (Exception e) { // Null pointer exception

        } finally {
            return isItemSelected;
        }
    }


    public ArrayList<Image> getSelectedImages() {

        ArrayList<Image> imagesSelected = new ArrayList<Image>();

        for (Map.Entry<Integer, Boolean> entry : mSelectedItems.entrySet()) {
            Integer key = entry.getKey();
            Boolean value = entry.getValue();
            if (value) {
                imagesSelected.add((Image) getItem(key));
            }
        }

        return imagesSelected;
    }



    public void addImage(Image image) {
        mImages.add(image);
    }

    @Override
    public int getCount() {
        return mImages.size();
    }

    @Override
    public Object getItem(int position) {
        return mImages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        final Image image = mImages.get(position);

        CheckBox chk;

        if (view == null) {
            view = mInflater.inflate(R.layout.gridview_image_item, null);

            ViewHolder holder = new ViewHolder();
            holder.ivImagePhoto = (ImageView) view.findViewById(R.id.iv_image);
            view.setTag(holder);
        }


        final int pos = position;
        chk = (CheckBox) view.findViewById(R.id.chk_selection);
        chk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mSelectedItems.put(pos, isChecked);
                setSelecionMode();
            }
        });


        chk.setChecked(isItemSelected(pos));
        chk.setVisibility(mIsInSelectionMode ? View.VISIBLE : View.INVISIBLE);

        final ViewHolder holder = (ViewHolder) view.getTag();
        holder.ivImagePhoto.setImageDrawable(null);

        if (image.isHidden()) {
            holder.ivImagePhoto.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_lock_white_24dp));
            holder.ivImagePhoto.setScaleType(ImageView.ScaleType.CENTER);
        } else {
            mImageLoader.displayThumbnailImage(holder.ivImagePhoto, image.getImageInternalId());
            holder.ivImagePhoto.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }

        return view;
    }

    private static class ViewHolder {
        public ImageView ivImagePhoto;
    }

}
