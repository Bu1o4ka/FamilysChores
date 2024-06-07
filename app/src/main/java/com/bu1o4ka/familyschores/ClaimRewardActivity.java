package com.bu1o4ka.familyschores;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.DialogFragment;
import androidx.annotation.NonNull;


public class ClaimRewardActivity extends DialogFragment {

    DialogInterface.OnClickListener claim;
    public ClaimRewardActivity(DialogInterface.OnClickListener claim) {
        this.claim = claim;
    }

    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        return builder
                .setTitle("Задание выполнено")
                .setIcon(R.drawable.ic_logo)
                .setMessage("Покажи это родителям и получи свою награду!")
                .setPositiveButton("Получил", claim)
                .create();
    }
}
