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
import es.molestudio.photochop.model.Category;
import es.molestudio.photochop.model.Selectable;

/**
 * Created by Chus on 02/03/15.
 */
public class ADPCategory extends ArrayAdapter {

    private ArrayList<Category> items;
    private ArrayList<Category> itemsAll;
    private ArrayList<Category> suggestions;
    private int viewResourceId;

    public ADPCategory(Context context, int viewResourceId, ArrayList<Category> items) {
        super(context, viewResourceId, items);
        this.items = items;
        this.itemsAll = (ArrayList<Category>) items.clone();
        this.suggestions = new ArrayList<Category>();
        this.viewResourceId = viewResourceId;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(viewResourceId, null);
        }
        Category category = items.get(position);
        if (category != null) {
            TextView customerNameLabel = (TextView) v.findViewById(R.id.tv_category_label);
            if (customerNameLabel != null) {
//              Log.i(MY_DEBUG_CategoryAG, "getView Category Name:"+customer.getName());
                customerNameLabel.setText(category.getName());
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
            String str = ((Category)(resultValue)).getName();
            return str;
        }
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if(constraint != null) {
                suggestions.clear();
                for (Category category : itemsAll) {
                    if(category.getName().toLowerCase().startsWith(constraint.toString().toLowerCase())){
                        suggestions.add(category);
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
            ArrayList<Category> filteredList = (ArrayList<Category>) results.values;
            if(results != null && results.count > 0) {
                clear();
                for (Category c : filteredList) {
                    add(c);
                }
                notifyDataSetChanged();
            }
        }
    };



}
