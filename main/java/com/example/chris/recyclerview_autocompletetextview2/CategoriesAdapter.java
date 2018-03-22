package com.example.chris.recyclerview_autocompletetextview2;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chris on 08/03/2018.
 */

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.MyViewHolder>
        implements Filterable {
    private Context context;
    private List<Category> categoryList;
    //categoryListFiltered are the names in the filtered list
    private List<Category> categoryListFiltered;
    private CategoriesAdapterListener listener;
    //private List<Category> categoryListFiltered = new ArrayList<>();


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, phone;
        public ImageView thumbnail;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            //  phone = (TextView) view.findViewById(R.id.phone);
            // thumbnail = view.findViewById(R.id.thumbnail);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    listener.onContactSelected(categoryListFiltered.get(getAdapterPosition()));
                }
            });
        }
    }


    public CategoriesAdapter(Context context, List<Category> categoryList, CategoriesAdapterListener listener) {
        this.context = context;
        this.listener = listener;
        this.categoryList = categoryList;
        this.categoryListFiltered = categoryList;
        //categoryListFiltered = new ArrayList<>();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_row_item_fetch, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        final Category category = categoryListFiltered.get(position);
        holder.name.setText(category.getName());
        // holder.phone.setText(category.getPhone());

    }

    @Override
    public int getItemCount() {

        //app crashes when this line is not commented
//        System.out.println("getItemCount is :" + categoryListFiltered.size());

       return categoryListFiltered.size();
    }





    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    categoryListFiltered = categoryList;
                } else {
                    List<Category> filteredList = new ArrayList<>();

                    for (Category row : categoryList) {

                        //split the category into separate words
                        String[] arr = row.getName().split(" ");

                        //for every split word
                        for (String ss : arr) {

                            //if (row.getName().toLowerCase().contains(charString.toLowerCase()) || row.getPhone().contains(charSequence)) {

                            //if any of the words start with the search term
                            if (ss.toLowerCase().startsWith(charString.toLowerCase())) {

                                filteredList.add(row);
                                System.out.println("bowbow2:" + filteredList.size());

                            }
                        }
                    }

                    categoryListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();

                filterResults.values = categoryListFiltered;
                System.out.println("here it is, categoryListFiltered" + categoryListFiltered.size());
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                categoryListFiltered = (ArrayList<Category>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }





    public interface CategoriesAdapterListener {
        void onContactSelected(Category category);
    }
}

