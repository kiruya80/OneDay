package com.ulling.oneday.util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

/**
 * 다이얼로그 유틸
 */
public class DialogUtils {
    private static final DialogUtils ourInstance = new DialogUtils();

    public static DialogUtils getInstance() {
        return ourInstance;
    }


    private DialogUtils() {
    }

    public ProgressDialog showProgressDialog(Activity activity, String msg) {
        ProgressDialog asyncDialog = new ProgressDialog(activity);
        asyncDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        asyncDialog.setMessage(msg);
        asyncDialog.setCancelable(false);

        return asyncDialog;
    }


    public void showDefaultDialog(Activity activity,
                                  String title,
                                  String msg,
                                  boolean isOneBtn,
                                  String msgPositive,
                                  String msgNegative,
                                  final DialogListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setPositiveButton(msgPositive,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (listener != null)
                            listener.onClickListener(true);
                    }
                });
        if (!isOneBtn)
            builder.setNegativeButton(msgNegative,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            if (listener != null)
                                listener.onClickListener(false);
                        }
                    });
        builder.show();
    }

    public interface DialogListener {
        void onClickListener(Boolean positive);
    }

}
