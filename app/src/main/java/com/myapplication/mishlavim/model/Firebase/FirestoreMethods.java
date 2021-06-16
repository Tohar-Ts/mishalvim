package com.myapplication.mishlavim.model.Firebase;

import android.util.Log;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

/**
 * Firestore Methods class manages connecting to firestore and provides functions for:
 * update document in the firestore, get, delete etc.
 *
 * How to use:
 * First, declare the on success and on failed function in the class according to the function arguments.
 * For example:
 * if the function accept "Function<DocumentSnapshot, Void> successFunc" argument,
 * then your success function should accept a DocumentSnapshot object and return a Void object.
 *
 * Then, call the wanted method.
 *  For example:
 * FirestoreMethods.getDocument(FirebaseStrings.formsTemplatesStr(), id,
 *                                         this::mySuccessFunc, this::showError);
 * Notice:
 * "Void" with a capital V is different from "void".
 * If the function needs to accept/return a void object, declare it this way:
 * private Void onUpdateFailure(Void unused){
 *     return null;
 * }
 */
public class FirestoreMethods {
    //TODO - add comments

    public static void createNewDocument(String collection, String newId, Object newDataObj,
                                         Function<Void, Void> successFunc,  Function<Void, Void> failedFunc){

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection(collection)
                .document(newId)
                .set(newDataObj)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        successFunc.apply(null);
                        Log.d("FirestoreMethods:", "Creating document ended successfully");
                    } else {
                        Log.d("FirestoreMethods:", "Creating document failed" + task.getException());
                        failedFunc.apply(null);
                    }
                });
    }
    public static void createNewDocumentRandomId(String collection, Object newDataObj,
                                                 Function<DocumentReference, Void> successFunc, Function<Void, Void> failedFunc){

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(collection)
                .add(newDataObj)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        successFunc.apply(task.getResult());
                        Log.d("FirestoreMethods:", "Creating document ended successfully");
                    } else {
                        Log.d("FirestoreMethods:", "Creating document failed" + task.getException());
                        failedFunc.apply(null);
                    }
                });
    }


    public static void getDocument(String collection, String documentId, Function<DocumentSnapshot,
                                    Void> successFunc,  Function<Void, Void> failedFunc){

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection(collection)
                .document(documentId)
                .get()
                .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            assert document != null;
                            successFunc.apply(document);
                            Log.d("FirestoreMethods:", "Get document ended successfully");
                        }
                        else {
                            Log.d("FirestoreMethods:", "Get document failed" + task.getException());
                            failedFunc.apply(null);
                        }
                });
    }


    public static void updateDocumentField(String collection, String documentId, String fieldToUpdate, Object data,
                                      Function<Void, Void> successFunc,  Function<Void, Void> failedFunc) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection(collection)
                .document(documentId)
                .update(fieldToUpdate, data)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        successFunc.apply(null);
                        Log.d("FirestoreMethods:", "Update document ended successfully");
                    }
                    else {
                        Log.d("FirestoreMethods:", "Update document failed" + task.getException());
                        failedFunc.apply(null);
                    }
                });
    }

    public static void updateMapKey(String collection, String documentId, String mapName, String key, String value,
                                           Function<Void, Void> successFunc,  Function<Void, Void> failedFunc) {
        //init
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String mapKeyRef = mapName + "." + key;
        //updating the guide list
        db.collection(collection)
                .document(documentId)
                .update(mapKeyRef, value)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        successFunc.apply(null);
                        Log.d("FirestoreMethods:", "Update map key ended successfully");
                    }
                    else {
                        Log.d("FirestoreMethods:", "Update map key failed" + task.getException());
                        failedFunc.apply(null);
                    }
                });
    }

    public static void deleteMapKey(String collection, String documentId, String mapName, String key,
                                    Function<Void, Void> successFunc,  Function<Void, Void> failedFunc) {
        //init
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String mapKeyRef = mapName + "." + key;

        //updating the guide list
        db.collection(collection)
                .document(documentId)
                .update(mapKeyRef, FieldValue.delete())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        successFunc.apply(null);
                        Log.d("FirestoreMethods:", "Delete map key ended successfully");
                    }
                    else {
                        Log.d("FirestoreMethods:", "Delete map key failed" + task.getException());
                        failedFunc.apply(null);
                    }
                });
    }

    public static void deleteDocument(String collection, String documentId,
                                      Function<Void, Void> successFunc,  Function<Void, Void> failedFunc){
        Log.d("FirestoreMethods:", "Starting to delete this doc:" + documentId);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection(collection)
                .document(documentId)
                .delete()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("FirestoreMethods:", "Delete document ended successfully.");
                        successFunc.apply(null);
                    }
                    else {
                        Log.d("FirestoreMethods:", "Delete document failed" + task.getException());
                        failedFunc.apply(null);
                    }
                });
    }


    public static void getDocumentByUserName(String userName,
                                      Function<DocumentSnapshot, Void> successFunc,  Function<Void, Void> failedFunc){

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference usersRef = db.collection(FirebaseStrings.usersStr());

        //getting the given user document
        usersRef.whereEqualTo(FirebaseStrings.nameStr(), userName)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (Objects.requireNonNull(task.getResult()).isEmpty())
                            Log.d("FirestoreMethods:", "getting document by user name returned empty.");
                        else {
                            //succeed at getting the user doc by his name
                            AtomicReference<DocumentSnapshot> userId = new AtomicReference<>();
                            for (QueryDocumentSnapshot document : task.getResult())
                                userId.set(document); // one user only for each user name!

                            successFunc.apply(userId.get());
                            Log.d("FirestoreMethods:", "getting document by user name ended successfully");
                        }

                    } else {
                        Log.d("FirestoreMethods:", "getting document by user name failed" + task.getException());
                        failedFunc.apply(null);
                    }
                });
    }

    public static void getDocumentsByUserType( String type,
                                              Function<QuerySnapshot, Void> successFunc, Function<Void, Void> failedFunc){

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference usersRef = db.collection(FirebaseStrings.usersStr());

        //getting a user type doc from the firebase
        usersRef.whereEqualTo(FirebaseStrings.typeStr(), type)
                .get()
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {

                        if (Objects.requireNonNull(task.getResult()).isEmpty())
                            Log.d("FirestoreMethods:", "getting document by user type returned empty.");
                        else {
                            //succeed at getting all documents of the given type
                            successFunc.apply(task.getResult()); //a list of documents
                            Log.d("FirestoreMethods:", "getting document by user type ended successfully");
                        }

                    } else {
                        Log.d("FirestoreMethods:", "getting document by user type failed");
                        failedFunc.apply(null);
                    }
                });
    }

    public static void getCollection(String collection, Function<QuerySnapshot, Void> successFunc,  Function<Void, Void> failedFunc){

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection(collection)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot document = task.getResult();
                        assert document != null;
                        successFunc.apply(document);
                        Log.d("FirestoreMethods:", "Get document ended successfully");
                    }
                    else {
                        Log.d("FirestoreMethods:", "Get document failed" + task.getException());
                        failedFunc.apply(null);
                    }
                });
    }
    //TODO - remove user
}
