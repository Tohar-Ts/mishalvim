package com.example.mishlavim.guideActivities;

//
//public class GuideAddVolunteerActivity extends AppCompatActivity implements View.OnClickListener {
//    private EditText emailEditText;
//    private EditText userNameEditText;
//    private EditText passwordEditText;
//    private ProgressBar loadingProgressBar;
//    private FirebaseAuth mAuth;
//    private FirebaseFirestore db;
//    private Validation validation;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_guide_add_volunteer);
//
//        emailEditText = findViewById(R.id.newEmail);
//        userNameEditText = findViewById(R.id.newUserName);
//        passwordEditText = findViewById(R.id.newPassword);
//        EditText verifyPasswordEditText = findViewById(R.id.verifyPassword);
//        Button addButton = findViewById(R.id.addNewUser);
//        loadingProgressBar = findViewById(R.id.registerLoading);
//
//        mAuth = FirebaseAuth.getInstance();
//        db = FirebaseFirestore.getInstance();
//
//        validation = new Validation(emailEditText,userNameEditText, passwordEditText, verifyPasswordEditText
//                , loadingProgressBar, getResources());
//
//        addButton.setOnClickListener(this);
//    }
//
//    @Override
//    public void onClick(View v) {
//        registerUser();
//    }
//
//    private void registerUser() {
//
//        if (validation.validateInput())
//            return;
//        String email = emailEditText.getText().toString().trim();
//        String password = passwordEditText.getText().toString().trim();
//        String userName = userNameEditText.getText().toString().trim();
//
//        loadingProgressBar.setVisibility(View.VISIBLE);
//        registerToFirebase(userName, email, password);
//    }
//
//    private void showRegisterFailed() {
//        loadingProgressBar.setVisibility(View.GONE);
//        Toast.makeText(getApplicationContext(), R.string.register_failed, Toast.LENGTH_SHORT).show();
//    }
//
//    private void registerToFirebase(String userName, String email, String password){
//        mAuth.createUserWithEmailAndPassword(email, password)
//                .addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) {
//                        FirebaseUser fbUser = mAuth.getCurrentUser();
//                        Volunteer volunteer = new Volunteer(userName, UserTypes.getVOLUNTEER(), email, myGuide);
//                    } else
//                        showRegisterFailed();
//                });
//    }
//
//    private void createNewUser(FirebaseUser fbUser, String userName, String email) {
//        User user;
//
//        if (newUserType.equals(UserTypes.getADMIN()))
//            user = new Admin(userName, newUserType, email);
//
//        else if (newUserType.equals(UserTypes.getGUIDE()))
//            user = new Guide(userName, newUserType, email, new HashMap<>());
//
//        else { //volunteer
//            String myGuide = guideName.getText().toString().trim();
//            user = new Volunteer(userName, newUserType, email, myGuide);
//            Guide.addVolunteerByGuideName(fbUser.getUid(), (Volunteer)user);
//        }
//
//        addUserToDb(fbUser, user);
//    }
//
//    private void addUserToDb(FirebaseUser fbUser, User user) {
//        String usersCollection = UserTypes.getUSER_COLLECTION();
//        String userId = fbUser.getUid();
//
//        db.collection(usersCollection)
//                .document(userId)
//                .set(user)
//                .addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) {
//                        Toast.makeText(com.example.mishlavim.adminActivities.AdminAddNewUserActivity.this, newUserType + " was added successfully", Toast.LENGTH_SHORT).show();
//                        loadingProgressBar.setVisibility(View.GONE);
//                    } else {
//                        showRegisterFailed();
//                    }
//                });
//    }
//}