package com.example.mishlavim.adminActivities;
import androidx.appcompat.app.AppCompatActivity;

public class AdminMainActivity extends AppCompatActivity {
//
//    SwitchCompat simpleSwitch;
//    RecyclerView guidesList;
//    RecyclerView formsList;
//    Button addNewUser;
//    Button addNewForm;
//
//    //our two buttons for the admin to cycle between forms and guides
////    Button firstFragmentBtn, secondFragmentBtn;
//Toolbar toolbar;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main_admin);
//
//        // initiate a Switch
//        simpleSwitch = (SwitchCompat) findViewById(R.id.AdminSwitch);
//
//        simpleSwitch.setOnCheckedChangeListener((buttonView, isChecked) ->{
//            switchOnClick();
//        });
//        //initiate the Recyclers
//        guidesList = findViewById(R.id.guidesList);
//        formsList =  findViewById(R.id.formsList);
//
//        addNewUser = findViewById(R.id.buttonAddNewUser);
//        addNewForm = findViewById(R.id.buttonFillNewForm);
//
//        // TODO: 5/24/2021 make sure we start with forms not guides on the screen.
//
//// check current state of a Switch (true or false).
////        Boolean switchState = simpleSwitch.isChecked();
//
////        guidesBTM = findViewById(R.id.guidesBTM);
////        formsBTM = findViewById(R.id.formsBTM);
//
//        //set listener on the guides button
////        guidesBTM.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////
////                replaceFragment(new AdminGuidesListActivity());
////
////            }
////        });
//        //set listener on the forms button
////        formsBTM.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////
////                replaceFragment(new AdminFormListActivity());
////
////            }
////        });
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setTitle(null);
//        BottomNavigationView navBarButtons=(BottomNavigationView) findViewById(R.id.bottom_navigation);
//        //set the current placement of the cursor on "home"
//        navBarButtons.setSelectedItemId(R.id.go_home);
//
//        //activate a on click listener for the other buttons:
//        navBarButtons.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
//
//                switch(item.getItemId()){
//                    case R.id.go_home:
//                        return true;
//                    case R.id.add_user:
//                        startActivity(new Intent(getApplicationContext(), GuideAddVolunteerActivity.class));
//                        overridePendingTransition(0, 0);
//                        return true;
//                    case R.id.forms:
//                        startActivity(new Intent(getApplicationContext(), FinishedFormActivity.class));
//                        overridePendingTransition(0, 0);
//                        return true;
//                }
//                return false;
//            }
//        });
//
//    }
//
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_admin,menu);
//        return true;
//    }
//    public void switchOnClick(){
//        if(simpleSwitch.isChecked()){
//            guidesList.setVisibility(View.VISIBLE);
//            formsList.setVisibility(View.GONE);
//            addNewUser.setVisibility(View.VISIBLE);
//            addNewForm.setVisibility(View.GONE);
//        }
//        else{
//            guidesList.setVisibility(View.GONE);
//            formsList.setVisibility(View.VISIBLE);
//            addNewUser.setVisibility(View.GONE);
//            addNewForm.setVisibility(View.VISIBLE);
//
//        }
//    }
//
//
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//
//
//            int id = item.getItemId();
//            switch (id){
//                case R.id.setting:
//                    Toast.makeText(AdminMainActivity.this, item.getTitle().toString(), Toast.LENGTH_SHORT).show();
//                    break;
//                case R.id.exit:
//                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
//                    overridePendingTransition(0, 0);
//                    Toast.makeText(AdminMainActivity.this, item.getTitle().toString(), Toast.LENGTH_SHORT).show();
//                    break;
//
//            }
//            return super.onOptionsItemSelected(item);
//
//        }
}


