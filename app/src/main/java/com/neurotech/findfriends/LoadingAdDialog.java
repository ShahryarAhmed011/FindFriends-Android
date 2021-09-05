package com.neurotech.findfriends;


import android.app.Dialog;
import android.content.Context;
import android.view.Window;

public class LoadingAdDialog{

    private Dialog mDialog;

    public LoadingAdDialog(Context context) {
        mDialog = new Dialog(context);
        if (mDialog.getWindow() != null) {
            mDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }
        mDialog.setContentView(R.layout.loading_ad_dialog);
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