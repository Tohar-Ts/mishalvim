package com.example.mishlavim.model.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mishlavim.R;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {


    List<String> showList;
    PopupMenu.OnMenuItemClickListener fragment;
    String clickedText;
    Integer popMenuSrc;

    public RecyclerAdapter(List<String> showList, PopupMenu.OnMenuItemClickListener fragment, Integer popMenuSrc) {
        this.showList = showList;
        this.fragment = fragment;
        this.popMenuSrc = popMenuSrc;
    }

    @NonNull
    @NotNull
    @Override
    //called when scrolling
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        //create the view
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.row_item, parent, false);
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

    //one row rules
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imageView;
        TextView textView;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.row_more_img);
            textView = itemView.findViewById(R.id.row_name);
            imageView.setOnClickListener(this);
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
