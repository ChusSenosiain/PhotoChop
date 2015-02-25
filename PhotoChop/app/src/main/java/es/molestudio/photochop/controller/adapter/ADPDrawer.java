package es.molestudio.photochop.controller.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import es.molestudio.photochop.R;
import es.molestudio.photochop.model.ObjectDrawerItem;

/**
 * Created by Chus on 24/02/15.
 */
public class ADPDrawer extends ArrayAdapter<ObjectDrawerItem> {

    private int mLayout;
    private LayoutInflater mInflater;
    private ObjectDrawerItem mDrawerItems[] = null;

    public ADPDrawer(Context context, ObjectDrawerItem[] drawerItems) {
        super(context, R.layout.drawer_list_item, drawerItems);
        mDrawerItems = drawerItems;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mLayout = R.layout.drawer_list_item;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItem = convertView;

        listItem = mInflater.inflate(mLayout, parent, false);

        ImageView imageViewIcon = (ImageView) listItem.findViewById(R.id.iv_drawer_item_image);
        TextView textViewName = (TextView) listItem.findViewById(R.id.tv_drawter_item_text);

        ObjectDrawerItem drawerItem = mDrawerItems[position];

        imageViewIcon.setImageResource(drawerItem.getIcon());
        textViewName.setText(drawerItem.getName());

        return listItem;
    }

}
