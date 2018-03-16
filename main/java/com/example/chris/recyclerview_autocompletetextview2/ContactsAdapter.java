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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chris on 08/03/2018.
 */

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.MyViewHolder>
        implements Filterable {
    private Context context;
    private List<Contact> contactList;
    private List<Contact> listWithoutDuplicates;
    private List<Contact> contactListFiltered;
    private ContactsAdapterListener listener;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, phone, cat_id;
      //  public ImageView thumbnail;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            cat_id =  (TextView) view.findViewById(R.id.cat_id);
            //phone = (TextView) view.findViewById(R.id.phone);
           // thumbnail = view.findViewById(R.id.thumbnail);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    listener.onContactSelected(contactListFiltered.get(getAdapterPosition()));
                }
            });
        }
    }

/*    @Override
    public int getItemViewType(int position) {
        //for each row in recyclerview, get the getType_row
        //it will either be the PopulistoListView or the fetched contacts
        return Integer.parseInt(theContactsList.get(position).getType_row());
    }*/


    public ContactsAdapter(Context context, List<Contact> contactList, ContactsAdapterListener listener) {
        this.context = context;
        this.listener = listener;
        this.contactList = listWithoutDuplicates;
        this.contactListFiltered = contactList;
       // this.listWithoutDuplicates = listWithoutDuplicates;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_row_item_fetch, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

     //   if(contactListFiltered.size() < contactList.size()) {
            final Contact contact = contactListFiltered.get(position);
            holder.name.setText(contact.getName());
            holder.cat_id.setText(contact.getCat_Id());

       // }
//        holder.phone.setText(contact.getPhone());

    /*    Glide.with(context)
                .load(contact.getImage())
                .apply(RequestOptions.circleCropTransform())
                .into(holder.thumbnail);*/
    }

    @Override
    public int getItemCount() {
        return contactListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    contactListFiltered = listWithoutDuplicates;
                } else {
                    List<Contact> filteredList = new ArrayList<>();
                    for (Contact row : listWithoutDuplicates) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        //if (row.getName().toLowerCase().contains(charString.toLowerCase()) || row.getPhone().contains(charSequence)) {
                        if (row.getName().toLowerCase().contains(charString.toLowerCase()) ) {

                            filteredList.add(row);
                        }
                    }

                    contactListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = contactListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                contactListFiltered = (ArrayList<Contact>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface ContactsAdapterListener {
        void onContactSelected(Contact contact);
    }
}
