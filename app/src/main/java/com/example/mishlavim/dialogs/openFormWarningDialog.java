package com.example.mishlavim.dialogs;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.DialogFragment;


public class openFormWarningDialog extends DialogFragment {

    public interface openFormWarningListener {
        public void onOpenFormWarningPositiveClick(DialogFragment dialog);
        public void onOpenFormWarningNegativeClick(DialogFragment dialog);
    }

    // Use this instance of the interface to deliver action events
    openFormWarningListener listener;

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            listener = (openFormWarningListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException("Error");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage("אזהרה! ייתכן וטופס זה כבר מולא על ידי מתנדבים. עריכתו תגרום לשיבושים בתשובותיהם. להמשיך?")

                .setPositiveButton("כן", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Log.d("positive","positive  button had pressed.");
                        listener.onOpenFormWarningPositiveClick(openFormWarningDialog.this);
                    }
                })

                .setNegativeButton("לא", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        Log.d("negative","negative button had pressed.");
                        listener.onOpenFormWarningNegativeClick(openFormWarningDialog.this);
                    }
                });

        // Create the AlertDialog object and return it
        return builder.create();
    }

}
