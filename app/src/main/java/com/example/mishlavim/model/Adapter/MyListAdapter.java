package com.example.mishlavim.model.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import androidx.fragment.app.DialogFragment;

import com.example.mishlavim.R;
import com.example.mishlavim.dialogs.DeleteUserDialog;
import com.example.mishlavim.guideActivities.GuideFormsPermissionActivity;
import com.example.mishlavim.guideActivities.GuideVoluSettingActivity;
import com.example.mishlavim.guideActivities.GuideMainActivity;
import com.example.mishlavim.model.Firebase.FirebaseStrings;
import com.example.mishlavim.model.Firebase.FirestoreMethods;

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
        mainViewHolder.imagebutton.setOnClickListener(new myClickListener(getItemId(position)));
//        mainViewHolder.imagebutton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //init clicked id
//
//                clickedRowName = (String) v.getTag();
//                //showing the popup menu
//                PopupMenu popup = new PopupMenu(context, v);
//
//                MenuInflater inflater = popup.getMenuInflater();
//                inflater.inflate(R.menu.volunteer_options_menu, popup.getMenu());
//                popup.show();
//                Log.d("TAG", "onClick: ");
//            }
//        });


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


    public class myClickListener implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {

        long id;

        public myClickListener(long id) {
            this.id = id;
        }
        @Override
        public void onClick(View v) {
            PopupMenu popup = new PopupMenu(context, v);
            popup.setOnMenuItemClickListener(this);
            MenuInflater inflater = popup.getMenuInflater();
            inflater.inflate(R.menu.volunteer_options_menu, popup.getMenu());
            popup.show();
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
//            //delete volunteer
//            if (item.getItemId() == R.id.remove_volunteer) {
//                DialogFragment newFragment = new DeleteUserDialog();
//                newFragment.show(getSupportFragmentManager(), "deleteUser");
//                return true;
//            }
//            else if (item.getItemId() == R.id.view_volunteer) {
//                FirestoreMethods.getDocument(FirebaseStrings.usersStr(),  guide.getMyVolunteers().get(clickedRowName), this::getUserDocSuccess, this::getUserDocFailed);
//                Log.d("clicked:", clickedRowName + " view" );
//                return true;
//            }
//
//            else if (item.getItemId() == R.id.edit_volunteer) {
//                Intent intent = new Intent(getApplicationContext(), GuideVoluSettingActivity.class);
//                intent.putExtra("CLICKED_VOLU_KEY", clickedRowName);
//                intent.putExtra("CLICKED_VOLU_ID", guide.getMyVolunteers().get(clickedRowName));
//                startActivity(intent);
//                overridePendingTransition(0, 0);
//                return true;
//            }
//            else if (item.getItemId() == R.id.open_form_to_volunteer) {
//                Intent intent = new Intent(getApplicationContext(), GuideFormsPermissionActivity.class);
//                intent.putExtra("CLICKED_VOLU_KEY", clickedRowName);
//                intent.putExtra("CLICKED_VOLU_ID", guide.getMyVolunteers().get(clickedRowName));
//                startActivity(intent);
//                overridePendingTransition(0, 0);
//                return true;
//            }
//            else if (item.getItemId() == R.id.view_volunteer) {
//                FirestoreMethods.getDocument(FirebaseStrings.usersStr(), guide.getMyVolunteers().get(clickedRowName), this::getUserDocSuccess, this::getUserDocFailed);
//                Log.d("clicked:", clickedRowName + " view" );
//                return true;
//            }
            return false;
        }
    }
}
