package com.example.mishlavim.dialogs;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.DialogFragment;


public class DeleteUser extends DialogFragment {

    public interface deleteUserListener {
        public void onDeletePositiveClick(DialogFragment dialog);
        public void onDeleteNegativeClick(DialogFragment dialog);
    }

    // Use this instance of the interface to deliver action events
    deleteUserListener listener;

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            listener = (deleteUserListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException("Error");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage("Are you sure you want to delete this user?")

                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Log.d("positive","positive delete button had pressed.");
                        listener.onDeletePositiveClick(DeleteUser.this);
                    }
                })

                .setNegativeButton("no", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        Log.d("negative","negative delete button had pressed.");
                        listener.onDeleteNegativeClick(DeleteUser.this);
                    }
                });

        // Create the AlertDialog object and return it
        return builder.create();
    }

}
