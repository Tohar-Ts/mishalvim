package com.example.mishlavim.adminActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.mishlavim.R;
import com.example.mishlavim.model.Adapter.RecyclerAdapter;
import com.example.mishlavim.model.Admin;
import com.example.mishlavim.model.Global;

import java.util.ArrayList;
import java.util.List;

public class AdminChooseVoluGuideActivity extends AppCompatActivity implements
        View.OnClickListener {
    private SearchView searchView;
    private RecyclerView guidesView;
    private RecyclerAdapter recyclerAdapter;
    private List<String> guidesNames;
    private Admin admin;
    private Boolean passAll;
    private String clickedGuideId, clickedGuideName;
    private String clickedVoluId, clickedVoluName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_chhose_volu_guide);
        //getting the guide list
        Global global = Global.getGlobalInstance();
        admin = global.getAdminInstance();

        guidesNames = new ArrayList<>(admin.getGuideList().keySet());

        //init xml views
        guidesView = findViewById(R.id.guides_recycler_view);

        //init the recycle view adapter which is the list of the guides that are displayed to the admin
        recyclerAdapter = new RecyclerAdapter(guidesNames, null, R.menu.guide_options_menu, true, this);
        guidesView.setAdapter(recyclerAdapter);

        searchView = findViewById(R.id.search_barA);
        // change close icon color
        ImageView iconClose = searchView.findViewById(R.id.search_close_btn);
        iconClose.setColorFilter(getResources().getColor(R.color.button_blue));
        //change search icon color
        ImageView iconSearch = searchView.findViewById(R.id.search_button);
        iconSearch.setColorFilter(getResources().getColor(R.color.button_blue));
        //this function allows the searchview to detect the text upon input and perform the search
        //with the selected filter in the recycleview adapter:
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                recyclerAdapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {

    }
}