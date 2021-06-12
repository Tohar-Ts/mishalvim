package com.example.mishlavim.adminActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mishlavim.R;
import com.example.mishlavim.model.Adapter.RecyclerAdapter;
import com.example.mishlavim.model.Admin;
import com.example.mishlavim.model.Global;
import com.example.mishlavim.model.Guide;

import java.util.ArrayList;
import java.util.List;

public class AdminChooseVoluGuideActivity extends AppCompatActivity implements
        View.OnClickListener {
    private TextView chooseGuideVoluName;
    private SearchView searchView;
    private RecyclerView guidesView;
    private RecyclerAdapter recyclerAdapter;
    private List<String> guidesNames;
    private Admin admin;
    private Guide guide;
    private Boolean passAll;
    private String clickedVoluId, clickedVoluName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_choose_volu_guide);
        //getting the guide list
        Global global = Global.getGlobalInstance();
        admin = global.getAdminInstance();
        guide = global.getGuideInstance();

        guidesNames = new ArrayList<>(admin.getGuideList().keySet());
        guidesNames.remove(guide.getName());

        //init data
        clickedVoluName = getIntent().getStringExtra("CLICKED_VOLU_NAME");
        clickedVoluId = getIntent().getStringExtra("CLICKED_VOLU_ID");
        passAll = getIntent().getBooleanExtra("PASS_ALL", false);

        //init xml views
        guidesView = findViewById(R.id.choose_guide_recycler_view);
        chooseGuideVoluName = findViewById(R.id.chooseGuideVoluName);

        //init prompt text
        if(passAll)
            chooseGuideVoluName.setText( "את כל המתנדבים של" +"\n"+"מדריך " + guide.getName());
        else
            chooseGuideVoluName.setText( "את חניך " + clickedVoluName);

        //init the recycle view adapter which is the list of the guides that are displayed to the admin
        recyclerAdapter = new RecyclerAdapter(guidesNames, null, R.menu.guide_options_menu, true, this);
        guidesView.setAdapter(recyclerAdapter);

        searchView = findViewById(R.id.search_barG);
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