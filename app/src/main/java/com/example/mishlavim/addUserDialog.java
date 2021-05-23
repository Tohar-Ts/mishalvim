package com.example.mishlavim;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mishlavim.guideActivities.GuideAddVolunteerActivity;
import com.example.mishlavim.guideActivities.GuideMainActivity;
import com.example.mishlavim.model.GlobalUserDetails;
import com.example.mishlavim.model.Guide;
import com.example.mishlavim.model.UserTypes;

public class addUserDialog extends DialogFragment implements NoticeDialogFragment.NoticeDialogListener{
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        GlobalUserDetails globalInstance = GlobalUserDetails.getGlobalInstance();
        String type = globalInstance.getType();

        builder.setMessage("משתמש נוצר בהצלחה!")

                .setPositiveButton("הוסף משתמש חדש", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                })
                .setNeutralButton("חזרה לרשימה", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (type.equals(UserTypes.getGUIDE())){
                    startActivity(new Intent(GuideMainActivity.this, GuideMainActivity.class));
                }
                else {

                }


            }
        })
                .setNegativeButton("מחיקת משתמש", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        // Create the AlertDialog object and return it

        return builder.create();
    }
}