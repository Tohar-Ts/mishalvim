package com.example.mishlavim.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.mishlavim.R;

import java.util.ArrayList;

public class CustomeArrayAdupter extends ArrayAdapter<CustomList> {


    public CustomeArrayAdupter(@NonNull Context context, int resource, @NonNull ArrayList<CustomList> customList) {
        super(context, resource, customList);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.custom_list, parent, false);
        }
        CustomList currentItem = getItem(position);
        ImageView imageView = listItemView.findViewById(R.id.imageView3);
        imageView.setImageResource(currentItem.getmImgResId());
        TextView textView1 = listItemView.findViewById(R.id.textView5);
        textView1.setText(currentItem.getmMovieName());
        EditText et = listItemView.findViewById(R.id.editTextTextPersonName2);
        et.setText(currentItem.getmMovieRating());
        return listItemView;

    }
}
