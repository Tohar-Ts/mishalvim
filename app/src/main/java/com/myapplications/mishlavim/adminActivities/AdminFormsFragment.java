package com.myapplications.mishlavim.adminActivities;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.myapplications.mishlavim.R;
import com.myapplications.mishlavim.model.Adapter.RecyclerAdapter;
import com.myapplications.mishlavim.model.Firebase.FirebaseStrings;
import com.myapplications.mishlavim.model.Firebase.FirestoreMethods;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class AdminFormsFragment extends Fragment implements View.OnClickListener {
    SearchView searchView;
    RecyclerView templateView;
    RecyclerAdapter recyclerAdapter;
    HashMap<String,String> templates;
    List<String> templatesNames;
    String clickedRowText;
    String clickedRowUid;

    public AdminFormsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin_forms, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //init xml views
        templateView = view.findViewById(R.id.templates_recycler_view);
        clickedRowText = ""; //default
        clickedRowUid = ""; //default

        //here we designate the search view and the search function used for searching
        searchView = view.findViewById(R.id.search_barB);
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

        //getting all the templates
        FirestoreMethods.getCollection(FirebaseStrings.formsTemplatesStr(), this::onGetTemplateSuccess, this::showError);
    }

    //this function displays an error if the data isn't available in the firebase database
    private Void showError(Void unused) {
        Toast.makeText(getActivity(), "?????????? ???????????? ??????????", Toast.LENGTH_SHORT).show();
        return null;
    }

    //this function sends a message once the correct template is located in the template collection
    //from the firebase. if the template isnt found an error message is sent.
    private Void onGetTemplateSuccess(QuerySnapshot result){
        //init a list of FormTemplates object from the collection

        String formNameField = FirebaseStrings.formNameStr();
        templates = new HashMap<>();
        for (DocumentSnapshot snapshot : result) {
            if (result == null) {
                showError(null);
                return null;
            }
            templates.put((String) snapshot.get(formNameField), snapshot.getId());
        }

        //init a list of Form Templates names
        templatesNames = new ArrayList<>(templates.keySet());

        //init the recycle view
        recyclerAdapter = new RecyclerAdapter(templatesNames, null, R.menu.templates_options_menu, true, this);
        templateView.setAdapter(recyclerAdapter);

        return null;
    }


    @Override
    public void onClick(View v) {
        //getting clicked text
        clickedRowText = recyclerAdapter.getClickedText();
        clickedRowUid = templates.get(clickedRowText);

        Log.d("onMenuItemClick: ", "view_template:" + recyclerAdapter.getClickedText() );
        Intent intent = new Intent(getActivity(),
                AdminWatchTemplate.class);
        intent.putExtra("CLICKED_FORM_VALUE", clickedRowUid);
        intent.putExtra("CLICKED_FORM_KEY", clickedRowText);
        getActivity().startActivity(intent);
    }
}