package com.example.mishlavim.model.Adapter;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Space;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import com.example.mishlavim.R;
import com.example.mishlavim.dialogs.DeleteUser;
import com.example.mishlavim.guideActivities.GuideMainActivity;
import com.example.mishlavim.login.LoginActivity;
import com.example.mishlavim.model.Firebase.AuthenticationMethods;
import com.example.mishlavim.model.Firebase.FirebaseStrings;
import com.example.mishlavim.model.Firebase.FirestoreMethods;
import com.example.mishlavim.model.Global;
import com.example.mishlavim.model.Guide;
import com.example.mishlavim.model.Volunteer;
import com.example.mishlavim.volunteerActivities.VolunteerMainActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import androidx.annotation.NonNull;
public class MyListAdapter extends ArrayAdapter<String> implements Filterable {
    private int layout;
    private List<String> mObjects;
    public MyListAdapter(Context context, int resource, List<String> objects) {
        super(context, resource, objects);
        mObjects = objects;
        layout = resource;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder mainViewholder = null;
        if(convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(layout, parent, false);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.thumbnail = (ImageView) convertView.findViewById(R.id.list_item_thumbnail);
            viewHolder.title = (TextView) convertView.findViewById(R.id.list_item_text);
            viewHolder.imagebutton = (ImageButton) convertView.findViewById(R.id.list_item_btn);
            convertView.setTag(viewHolder);
        }
        mainViewholder = (ViewHolder) convertView.getTag();
        mainViewholder.imagebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //init clicked id
//                clickedRowName = (String) v.getTag();
//                //showing the popup menu
//                PopupMenu popup = new PopupMenu(getBaseContext(), v);
//                MenuInflater inflater = popup.getMenuInflater();
//                inflater.inflate(R.menu.volunteer_options_menu, popup.getMenu());
//                popup.show();
                Log.d("TAG", "onClick: ");
            }
        });
//            mainViewholder.imagebutton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Toast.makeText(getContext(), "Button was clicked for list item " + position, Toast.LENGTH_SHORT).show();
//                }
//            });
        mainViewholder.title.setText(getItem(position));

        return convertView;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return super.getFilter();
    }
}