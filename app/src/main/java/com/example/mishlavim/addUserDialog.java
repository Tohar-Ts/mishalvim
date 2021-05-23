package com.example.mishlavim;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.DialogFragment;
import com.example.mishlavim.model.GlobalUserDetails;

import java.io.Console;


public class addUserDialog extends DialogFragment {

    public interface addUserDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog);
        public void onDialogNegativeClick(DialogFragment dialog);
        public void onDialogNeutralClick(DialogFragment dialog);
    }

    // Use this instance of the interface to deliver action events
    addUserDialogListener listener;

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            listener = (addUserDialogListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException("Error");
        }
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage("משתמש נוצר בהצלחה!")

                .setPositiveButton("הוסף משתמש חדש", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Log.d("positive","positive button had pressed.");
                        listener.onDialogPositiveClick(addUserDialog.this);
                    }
                })
                .setNeutralButton("חזרה לרשימה", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Log.d("neutral","neutral button had pressed.");
                listener.onDialogNeutralClick(addUserDialog.this);

            }
        })
                .setNegativeButton("מחיקת משתמש", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        Log.d("negative","negative button had pressed.");
                        listener.onDialogNegativeClick(addUserDialog.this);
                    }
                });

        // Create the AlertDialog object and return it
        return builder.create();
    }

}