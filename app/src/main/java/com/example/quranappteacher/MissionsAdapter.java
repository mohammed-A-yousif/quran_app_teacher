package com.example.quranappteacher;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MissionsAdapter extends RecyclerView.Adapter<MissionsAdapter.ViewHolder> implements Filterable {
    private List<Contact> listItems;
    private List<Contact> listItemsFiltered;


    public MissionsAdapter(List<Contact> listItems, Context context) {
        this.listItems = listItems;
        listItemsFiltered = new ArrayList<>(listItems);

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_mission, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Contact listItem = listItems.get(position);
        holder.textViewName.setText(listItem.getName());
        holder.textViewPhone.setText(listItem.getPhone());
        holder.textViewDate.setText(listItem.getDate());
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    @Override
    public Filter getFilter() {
        return contactFilter;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewName;
        public TextView textViewPhone;
        public TextView textViewDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewName = itemView.findViewById(R.id.mission_row_name_textView);
            textViewPhone = itemView.findViewById(R.id.mission_row_phone_textView);
            textViewDate = itemView.findViewById(R.id._mission_row_date_textView);
        }
    }
    private Filter contactFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Contact> filteredList =new ArrayList<>();

            if (constraint==null|| constraint.length()==0){
                filteredList.addAll(listItemsFiltered);
            }else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Contact item : listItemsFiltered){
                    if (item.getName().toLowerCase().contains(filterPattern) || item.getPhone().contains(filterPattern)){
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results =new FilterResults();
            results.values=filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            listItems.clear();
            listItems.addAll((List)results.values);
            notifyDataSetChanged();

        }
    };

    public interface MissionsAdapterListener {
        void onContactSelected(Contact contact);
    }

}
