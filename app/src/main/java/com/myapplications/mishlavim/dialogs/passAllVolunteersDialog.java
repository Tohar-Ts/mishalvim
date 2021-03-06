package com.myapplications.mishlavim.dialogs;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.DialogFragment;


public class passAllVolunteersDialog extends DialogFragment {

    public interface passAllVolunteersListener {
        public void passAllVolunteersPositiveClick(DialogFragment dialog);
        public void passAllVolunteersNegativeClick(DialogFragment dialog);
    }

    // Use this instance of the interface to deliver action events
    passAllVolunteersListener listener;

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            listener = (passAllVolunteersListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException("Error");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage("האם את/ה בטוח/ה שברצונך להעביר את כל המתנדבים?")

                .setPositiveButton("כן", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Log.d("neutral","neutral button had pressed.");
                        listener.passAllVolunteersPositiveClick(passAllVolunteersDialog.this);
                    }
                })
                .setNegativeButton("לא", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Log.d("neutral","neutral button had pressed.");
                        listener.passAllVolunteersNegativeClick(passAllVolunteersDialog.this);
                    }
                });


        // Create the AlertDialog object and return it
        return builder.create();
    }

}
