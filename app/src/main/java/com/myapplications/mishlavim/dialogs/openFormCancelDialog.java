package com.myapplications.mishlavim.dialogs;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.DialogFragment;


public class openFormCancelDialog extends DialogFragment {

    public interface openFormCancelListener {
        public void onOpenFormCancelNeutralClick(DialogFragment dialog);
    }

    // Use this instance of the interface to deliver action events
    openFormCancelListener listener;

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            listener = (openFormCancelListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException("Error");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage("שאלון זה פתוח אצל מספר מתנדבים ולכן לא ניתן לערוך אותו.")

                .setNeutralButton("הבנתי", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Log.d("neutral","neutral button had pressed.");
                        listener.onOpenFormCancelNeutralClick(openFormCancelDialog.this);
                    }
                });

        // Create the AlertDialog object and return it
        return builder.create();
    }

}
