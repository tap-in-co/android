package com.tapin.tapin.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;

public class AlertMessages {
    Activity context;
    LayoutInflater inflater;

    public AlertMessages(Activity context) {
        this.context = context;
        inflater = context.getLayoutInflater();
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
        builder.setMessage("No Data found on server.").setPositiveButton("Ok",
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

    public void showCustomMessage(String title, String message, DialogInterface.OnClickListener dialogInterface) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message).setCancelable(false)
                .setPositiveButton("Ok", dialogInterface)
                .setNegativeButton("Cancel", null);

        AlertDialog alert = builder.create();
        alert.show();

    }




}
