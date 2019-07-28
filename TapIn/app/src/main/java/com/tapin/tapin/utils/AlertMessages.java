package com.tapin.tapin.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.tapin.tapin.R;

public class AlertMessages {

    static AlertDialogCallback alertDialogCallback;
    Context context;

    public AlertMessages(Context context) {
        this.context = context;
    }

    public void showNetworkAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Please check your internet connection.")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert = builder.create();
        alert.setTitle("Connection Problem");
        alert.show();

    }

    public void showserverdataerror() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("No CorporateDomain found on server.").setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert = builder.create();

        alert.show();

    }

    public void Registered() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Regestration sucessfull").setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert = builder.create();

        alert.show();

    }

    public void login() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Log in sucessfull").setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert = builder.create();

        alert.show();

    }

    public void showErrorInConnection() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Please check Your Internet Connection")
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();

    }

    public void showFriendAddError(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message).setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();

    }

    public void showCustomMessage(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Message");
        builder.setMessage(message).setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();

                    }
                });

        AlertDialog alert = builder.create();
        alert.show();

    }

    public void showCustomMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        if (title.length() > 0) {
            builder.setTitle(title);
        }

        builder.setMessage(message).setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();

                    }
                });

        AlertDialog alert = builder.create();
        alert.show();

    }

    public void showCustomMessage(String title, String message, DialogInterface.OnClickListener dialogInterface) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.datepicker);
        builder.setTitle(title);
        builder.setMessage(message).setCancelable(false)
                .setPositiveButton("Ok", dialogInterface)
                .setNegativeButton("Cancel", null);

        AlertDialog alert = builder.create();
        alert.show();

    }

    public void alert(Context context, String title, String message, final String positiveBtn, final String negativeBtn, final String neutralBtn, final AlertDialogCallback alertDialogCallback) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

        if (title != null && title.length() > 0) {
            alertDialog.setTitle(title);
        }
        alertDialog.setMessage(message);

        if (positiveBtn != null) {
            alertDialog.setPositiveButton(positiveBtn, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                    dialog.dismiss();
                    alertDialogCallback.clickedButtonText(positiveBtn);

                }
            });
        }

        if (negativeBtn != null) {
            alertDialog.setNegativeButton(negativeBtn, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                    dialog.dismiss();
                    alertDialogCallback.clickedButtonText(negativeBtn);

                }
            });
        }

        if (neutralBtn != null) {
            alertDialog.setNeutralButton(neutralBtn, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                    dialog.dismiss();
                    alertDialogCallback.clickedButtonText(neutralBtn);

                }
            });
        }

        alertDialog.show();
    }

    public interface AlertDialogCallback {

        void clickedButtonText(String s);

    }


}
