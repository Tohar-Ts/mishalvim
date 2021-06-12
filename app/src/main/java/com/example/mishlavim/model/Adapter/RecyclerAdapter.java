package com.example.mishlavim.model.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mishlavim.R;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> implements Filterable {


    List<String> showList;
    List<String> showListAll;
    PopupMenu.OnMenuItemClickListener fragment;
    View.OnClickListener fragmentNoMenu;
    String clickedText;
    Integer popMenuSrc;
    Boolean noMenu;

    public RecyclerAdapter(List<String> showList, PopupMenu.OnMenuItemClickListener fragment, Integer popMenuSrc, Boolean noMenu,View.OnClickListener fragmentNoMenu) {
        this.showList = showList;
        this.fragment = fragment;
        this.popMenuSrc = popMenuSrc;
        this.showListAll = new ArrayList<>(showList);
        this.noMenu = noMenu;
        this.fragmentNoMenu = fragmentNoMenu;
    }

    @NonNull
    @NotNull
    @Override
    //called when scrolling
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        //create the view
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view;
        if(noMenu)
            view = layoutInflater.inflate(R.layout.row_item_no_menu, parent, false);
        else
            view = layoutInflater.inflate(R.layout.row_item, parent, false);
        return new ViewHolder(view);
    }


    //called to map the data
    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerAdapter.ViewHolder holder, int position) {
        holder.textView.setText(showList.get(position));
    }

    @Override
    public int getItemCount() {
        return showList.size();
    }

    public String getClickedText() {
        return clickedText;
    }

    @NonNull
    @Override
    public Filter getFilter() {
            return filter;
    }
    //this runs on a background thread
    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<String> filteredList = new ArrayList<>();
            if (constraint.toString().isEmpty()){
                filteredList.addAll(showListAll);
            }
            else{
                for (String name :showListAll){
                    if (name.toLowerCase().contains(constraint.toString().toLowerCase())){
                        filteredList.add(name);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;

            return filterResults;
        }
        //run on a UI thread
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
        showList.clear();
        showList.addAll((Collection<? extends String>) results.values);
        notifyDataSetChanged();
        }
    };
    //one row rules
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
       RelativeLayout row_layout;
        ImageView imageView;
        TextView textView;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            if(noMenu) {
                textView = itemView.findViewById(R.id.row_name);
                row_layout = itemView.findViewById(R.id.row_layout);
                row_layout.setOnClickListener(fragmentNoMenu);
            }
            else {
                textView = itemView.findViewById(R.id.row_name);
                imageView = itemView.findViewById(R.id.row_more_img);
                imageView.setOnClickListener(this);
                row_layout = itemView.findViewById(R.id.row_layout);
                row_layout.setOnClickListener(this);
            }
        }


        @Override
        public void onClick(View v) {
            clickedText = showList.get(getAdapterPosition());
            PopupMenu popup = new PopupMenu(v.getContext(), v);
            popup.setOnMenuItemClickListener(fragment);
            popup.inflate(popMenuSrc);
            popup.show();
        }
    }
}
