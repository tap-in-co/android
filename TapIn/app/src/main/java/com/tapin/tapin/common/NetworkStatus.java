package com.tapin.tapin.common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetworkStatus {

    ConnectivityManager connectivityManager;
    Context context;
    boolean connected = false;

    public NetworkStatus(Context context) {
        this.context = context;
    }

    public static String getResponce(String strUr, String method) {

        try {
            URL url = new URL(strUr);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(15000);
            connection.setRequestMethod(method);
            connection.setDoInput(true);
            connection.connect();

            InputStream inputStream = connection.getInputStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            StringBuffer buffer = new StringBuffer();
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }

            Log.v("Server Answer:", buffer.toString());

            return buffer.toString();

        } catch (Exception e) {
            Log.v("Server Exception:", e.toString());
        }

        return "";

    }

    public boolean isOnline() {

        try {
            connectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager
                    .getActiveNetworkInfo();

            if (networkInfo != null) {
                connected = true;
            }
            return connected;
        } catch (Exception e) {
            Log.v("Connectivity Exception:", e.toString());
        }
        return connected;

    }

    public boolean isOnline(Context context) {

        try {
            connectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager
                    .getActiveNetworkInfo();

            if (networkInfo != null) {
                if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI)
                    if (networkInfo.isConnectedOrConnecting())
                        connected = true;
                if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE)
                    if (networkInfo.isConnectedOrConnecting())
                        connected = true;
            }
            return connected;
        } catch (Exception e) {
            Log.v("Connectivity Exception:", e.toString());
        }
        return connected;

    }

}
