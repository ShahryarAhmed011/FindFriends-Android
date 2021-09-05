package com.neurotech.findfriends;


import android.app.Dialog;
import android.content.Context;
import android.view.Window;

public class DeletingContactsDialog {

    private Dialog mDialog;

    public DeletingContactsDialog(Context context) {
        mDialog = new Dialog(context);
        if (mDialog.getWindow() != null) {
            mDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }
        mDialog.setContentView(R.layout.delete_contacts_dialog);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setCancelable(false);
    }


    public void show() {
        mDialog.show();
    }


    public void dismiss() {
        mDialog.dismiss();
    }
}