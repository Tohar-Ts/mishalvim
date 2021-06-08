package com.example.mishlavim.model.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.mishlavim.R;

import java.util.List;

public class MyListAdapter extends ArrayAdapter<String> implements Filterable {
    private int layout;
    private List<String> mObjects;
    private Context context;
    private String clickedRowName;
    public MyListAdapter(Context context, int resource, List<String> objects) {
        super(context, resource, objects);
        this.context = context;
        mObjects = objects;
        layout = resource;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder mainViewHolder = null;
        if(convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(layout, parent, false);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.thumbnail = (ImageView) convertView.findViewById(R.id.list_item_thumbnail);
            viewHolder.title = (TextView) convertView.findViewById(R.id.list_item_text);
            viewHolder.imagebutton = (ImageButton) convertView.findViewById(R.id.list_item_btn);
            convertView.setTag(viewHolder);
        }
        mainViewHolder = (ViewHolder) convertView.getTag();
        mainViewHolder.imagebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //init clicked id

                clickedRowName = (String) v.getTag();
                //showing the popup menu
                PopupMenu popup = new PopupMenu(context, v);

                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.volunteer_options_menu, popup.getMenu());
                popup.show();
                Log.d("TAG", "onClick: ");
            }
        });


//            mainViewHolder.imagebutton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Toast.makeText(getContext(), "Button was clicked for list item " + position, Toast.LENGTH_SHORT).show();
//                }
//            });
        mainViewHolder.title.setText(getItem(position));

        return convertView;
    }
//    public void setContext (Context con){
//        this.context = con;
//    }
    @NonNull
    @Override
    public Filter getFilter() {
        return super.getFilter();
    }
}