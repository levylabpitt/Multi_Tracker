package com.synapse.network;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.SystemClock;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;

import com.synapse.ProgressHUD;


/**
 * Created by Jitendra on 22,March,2019
 */

public class Dialogs {

    private static ProgressDialog progressDialog;

    // public static ViewDialog viewDialog;
    public static ProgressHUD viewDialog;

    /**
     * Method to show progress dialog
     *
     * @param context
     * @param message
     */
    public static void showProgressDialog(Context context, String message) {

        try {
            if (viewDialog == null) {
                try {
                    viewDialog = ProgressHUD.show(context, "", false);
                } catch (Exception e) {
                    e.getMessage();
                }
            }

            viewDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }



   /* public static AlertDialog showProgressDialog(AppCompatActivity mContext, String message) {


        AlertDialog.Builder builder;
        AlertDialog alertDialog;

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.custom_loading_layout,
                null);
      *//*  WebView webview = (WebView) layout.findViewById(R.id.webview);
        webview.loadUrl("file:///android_asset/pro.gif");*//*
        builder = new AlertDialog.Builder(mContext);
        builder.setView(layout);
        alertDialog = builder.create();
        alertDialog.show();

        return alertDialog;
    }*/


    /**
     * Method to hide progress dialog
     *
     * @param activity
     */
    public static void hideProgressDialog(Context activity) {
        try {
            if (activity == null)
                return;

            ((AppCompatActivity) activity).runOnUiThread(() -> {
                try {
                    if (viewDialog.isShowing()) {
                        viewDialog.dismiss();
                    }
                } catch (Exception e) {
                    e.getMessage();
                }
            });
        } catch (Exception e) {

        }
    }


    /**
     * Method to display a alert dialog with ok
     *
     * @param mContext
     * @param message
     */
    public static void showAlert(Context mContext, String message) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        final AlertDialog dialog = builder.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
//        builder.setTitle(mContext.getString(R.string.app_name));
        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();

    }

    /**
     * Method to show try again dialog
     *
     * @param mContext
     * @param message
     */
    public static void showTryAgainDialog(final Context mContext, String message, final OnRetryCallback mOnRetryCallback) {
        final long[] mLastClickTime = {0};
        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        final AlertDialog dialog = builder.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        builder.setMessage(message);
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // mis-clicking prevention, using threshold of 1000 ms
                if (SystemClock.elapsedRealtime() - mLastClickTime[0] < 1000) {
                    return;
                }
                mLastClickTime[0] = SystemClock.elapsedRealtime();
                dialog.dismiss();
                mOnRetryCallback.OnRetry(true);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                mOnRetryCallback.OnRetry(false);
            }
        });

        builder.setCancelable(false);
        builder.show();
    }


    /*

     *//**
     * Method to show dilog with Ok and cancel with Dismiss callback listener
     *
     * @param mActivity
     * @param message
     * @param mOnDialogDismiss
     *//*
    public static void showOkCancelWithListener(Activity mActivity, String message, final OnOkCancelListener mOnDialogDismiss) {
        final long[] mLastClickTime = {0};
        final AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        final AlertDialog dialog = builder.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        builder.setMessage(message);
        builder.setPositiveButton(mActivity.getString(R.string.TAG_OK), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // mis-clicking prevention, using threshold of 1000 ms
                if (SystemClock.elapsedRealtime() - mLastClickTime[0] < 1000) {
                    return;
                }
                mLastClickTime[0] = SystemClock.elapsedRealtime();
                dialogInterface.dismiss();
                mOnDialogDismiss.onOkDismissed(true);
            }
        });

        builder.setNegativeButton(mActivity.getString(R.string.TAG_CANCEL), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                mOnDialogDismiss.onCancelDismissed(false);
            }
        });

        builder.setCancelable(false);
        builder.show();

    }
*/
}

