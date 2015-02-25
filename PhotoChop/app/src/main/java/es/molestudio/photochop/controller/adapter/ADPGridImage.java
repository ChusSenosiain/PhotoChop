package es.molestudio.photochop.controller.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;

import es.molestudio.photochop.R;
import es.molestudio.photochop.controller.util.Log;
import es.molestudio.photochop.model.Constants;
import es.molestudio.photochop.model.Image;

/**
 * Created by Chus on 24/02/15.
 */
public class ADPGridImage extends BaseAdapter {

    private ArrayList<Image> mImages = new ArrayList<Image>();
    private Context mContext;
    private LayoutInflater mInflater;


    public ADPGridImage(Context context) {
        mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void updateImages(ArrayList<Image> images) {

        mImages = images;
        if (mImages == null){
            mImages = new ArrayList<Image>();
        }

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

        Image image = mImages.get(position);

        if (view == null) {
            view = mInflater.inflate(R.layout.gridview_image_item, null);

            ViewHolder holder = new ViewHolder();
            holder.ivImagePhoto = (ImageView) view.findViewById(R.id.iv_image);

            view.setTag(holder);
        }

        ViewHolder holder = (ViewHolder) view.getTag();

        ImageLoader.getInstance().displayImage(image.getImageUri().toString(), holder.ivImagePhoto, Constants.UIL_DISPLAY_IMAGE_OPTIONS);

        return view;
    }


    private static class ViewHolder {
        public ImageView ivImagePhoto;
    }
}
