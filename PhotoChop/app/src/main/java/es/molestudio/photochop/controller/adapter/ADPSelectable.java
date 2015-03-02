package es.molestudio.photochop.controller.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import java.util.ArrayList;

import es.molestudio.photochop.R;
import es.molestudio.photochop.model.Selectable;

/**
 * Created by Chus on 02/03/15.
 */
public class ADPSelectable<T> extends ArrayAdapter<T> {

    private ArrayList<T> items;
    private ArrayList<T> itemsAll;
    private ArrayList<T> suggestions;
    private int viewResourceId;

    public ADPSelectable(Context context, int viewResourceId, ArrayList<T> items) {
        super(context, viewResourceId, items);
        this.items = items;
        this.itemsAll = (ArrayList<T>) items.clone();
        this.suggestions = new ArrayList<T>();
        this.viewResourceId = viewResourceId;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(viewResourceId, null);
        }
        T item = items.get(position);
        if (item != null) {
            TextView customerNameLabel = (TextView) v.findViewById(R.id.tv_category_label);
            if (customerNameLabel != null) {
//              Log.i(MY_DEBUG_TAG, "getView Category Name:"+customer.getName());
                customerNameLabel.setText(((Selectable)item).getName());
            }
        }
        return v;
    }

    @Override
    public Filter getFilter() {
        return nameFilter;
    }

    Filter nameFilter = new Filter() {
        @Override
        public String convertResultToString(Object resultValue) {
            String str = ((Selectable)(resultValue)).getName();
            return str;
        }
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if(constraint != null) {
                suggestions.clear();
                for (T item : itemsAll) {
                    if(((Selectable) item).getName().toLowerCase().startsWith(constraint.toString().toLowerCase())){
                        suggestions.add(item);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            ArrayList<T> filteredList = (ArrayList<T>) results.values;
            if(results != null && results.count > 0) {
                clear();
                for (T c : filteredList) {
                    add(c);
                }
                notifyDataSetChanged();
            }
        }
    };



}
