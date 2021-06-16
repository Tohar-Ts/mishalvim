package com.myapplication.mishlavim.dialogs;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.DialogFragment;


public class alertOpenFormDialog extends DialogFragment {

    public interface alertOpenFormListener {
        public void onAlertOpenFormNeutralClick(DialogFragment dialog);

    }

    // Use this instance of the interface to deliver action events
    alertOpenFormListener listener;

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            listener = (alertOpenFormListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException("Error");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage("לא ניתן לשנות הגדרות עריכה לשאלון שלא הושלם בעבר")

                .setNeutralButton("הבנתי", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Log.d("neutral","neutral  button had pressed.");
                        listener.onAlertOpenFormNeutralClick(alertOpenFormDialog.this);
                    }
                });

        // Create the AlertDialog object and return it
        return builder.create();
    }

}
