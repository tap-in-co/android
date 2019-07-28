package com.tapin.tapin.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    private static final String PASSWORD_PATTERN = "((?=.*[a-z])(?=.*\\d)(?=.*[A-Z]).{6,40})";
    public static SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static boolean isValidEmailAddress(String emailAddress) {
        String expression = "[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})";
        CharSequence inputStr = emailAddress;
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        return matcher.matches();
    }

    public static boolean isValidMobile(String phone) {
        boolean check = false;
        if (!Pattern.matches("[a-zA-Z]+", phone)) {
            // if(phone.length() != 10) {
            check = phone.length() >= 6 && phone.length() <= 13;
        } else {
            check = false;
        }
        return check;
    }

    public final static boolean isValidOrganizationEmail(String target) {

        String regex = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*.edu";
        return Pattern.matches(regex, target);

    }

    public static boolean validatePassword(final String password) {
        Pattern pattern;
        Matcher matcher;
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        Debug.e("isMatch of" + password, matcher.matches() + "");

        return matcher.matches();


    }

    public static boolean isInternetConnected(Context mContext) {

        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null;
    }

    public static void hideSoftKeyword(Activity activity) {

        if (activity.getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }

    }

    public static void showSoftKeyword(Activity activity, View view) {

        if (activity.getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }

    }

    public static String compressImage(String filePath) {

        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();

        // by setting this field as true, the actual bitmap pixels are not
        // loaded in the memory. Just the bounds are loaded. If
        // you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

        // max Height and width values of the compressed image is taken as
        // 816x612

        float maxHeight = 816.0f;
        float maxWidth = 612.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

        // width and height values are set maintaining the aspect ratio of the
        // image

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;

            }
        }

        // setting inSampleSize value allows to load a scaled down version of
        // the original image

        options.inSampleSize = calculateInSampleSize(options, actualWidth,
                actualHeight);

        // inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false;

        // this options allow android to claim the bitmap memory if it runs low
        // on memory
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
            // load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight,
                    Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2,
                middleY - bmp.getHeight() / 2, new Paint(
                        Paint.FILTER_BITMAP_FLAG));

        // check the rotation of the image and display it properly
        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Debug.e("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Debug.e("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Debug.e("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Debug.e("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream out = null;
        String filename = getFilename();
        try {
            out = new FileOutputStream(filename);

            // write the compressed bitmap at the destination specified by
            // filename.
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 70, out);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return filename;

    }

    public static String getFilename() {
        File file = new File(Environment.getExternalStorageDirectory()
                .getPath(), "MyFolder/Images");
        if (!file.exists()) {
            file.mkdirs();
        }
        String uriSting = (file.getAbsolutePath() + "/"
                + System.currentTimeMillis() + ".jpg");
        return uriSting;

    }

    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height
                    / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }

    public static Typeface getTypeface1(Context activity) {
        Typeface font = Typeface.createFromAsset(activity.getAssets(),
                "proxima-nova-light.otf");

        return font;
    }

    public static Typeface getTypeface2(Context activity) {
        Typeface font = Typeface.createFromAsset(activity.getAssets(),
                "ProximaNova-Light.otf");

        return font;
    }

    public static Typeface getTypeface3(Context activity) {
        Typeface font = Typeface.createFromAsset(activity.getAssets(),
                "ProximaNova-Regular.otf");

        return font;
    }

    public static String getFormattedDate(String dateStr) {
        SimpleDateFormat form = new SimpleDateFormat(Constant.SERVER_DATE_FORMAT);
        Date date = null;
        try {
            date = form.parse(dateStr);
        } catch (Exception e) {

            e.printStackTrace();
        }
        SimpleDateFormat postFormater = new SimpleDateFormat("MM/dd/yy");
        String newDateStr = postFormater.format(date);
        return newDateStr;
    }

    public static String getFormattedDate(Date dateStr) {
        SimpleDateFormat form = new SimpleDateFormat(Constant.SERVER_DATE_FORMAT);

        SimpleDateFormat postFormater = new SimpleDateFormat("MM/dd/yy");
        String newDateStr = postFormater.format(dateStr);
        return newDateStr;
    }

    public static String convertLocalToGMT(String str) {

        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date myDate = null;
        try {
            myDate = inputFormat.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        outputFormat.setTimeZone(TimeZone.getTimeZone("GMT"));

        String result = outputFormat.format(myDate);
        Debug.e("convertLocalToGMT", "" + result);

        return result;
    }

    public static String getPointsDate(String str) {

        if (isEmpty(str))
            return "";

        String dateStr = str;
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        inputFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date date = null;
        try {
            date = inputFormat.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        inputFormat.setTimeZone(TimeZone.getDefault());


        SimpleDateFormat outputFormat = new SimpleDateFormat("MMM dd HH:mm a");
        String result = outputFormat.format(date);
        //Debug.e("convertGMTToLocal", "" + result);


        return result;
    }

    public static String getTimeDifference(String datestr) {

        String result = "";
        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        inputFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date startDate = null;

        Date endDate = getDateInUTC();
        try {
            startDate = inputFormat.parse(datestr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Debug.e("getTime diff is", inputFormat.format(endDate) + "-" + inputFormat.format(startDate));
        long duration = endDate.getTime() - startDate.getTime();

        long diffInSeconds = TimeUnit.MILLISECONDS.toSeconds(duration);
        long diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(duration);
        long diffInHours = TimeUnit.MILLISECONDS.toHours(duration);
        long diffInDays = TimeUnit.MILLISECONDS.toDays(duration);

        if (diffInMinutes < 1) {
            result = String.format("%s %s", diffInSeconds, (diffInSeconds <= 1) ? " second ago" : " seconds ago");
        } else if (diffInHours < 1) {
            result = String.format("%s %s", diffInMinutes, (diffInMinutes <= 1) ? " minute ago" : " minutes ago");

        } else if (diffInDays < 1) {
            result = String.format("%s %s", diffInHours, (diffInHours <= 1) ? " hour ago" : " hours ago");
        } else {
            result = String.format("%s %s", diffInDays, (diffInDays <= 1) ? "day ago" : " days ago");
        }

        return result;
    }

    public static boolean isEmpty(String str) {

        if (!TextUtils.isEmpty(str))
            return TextUtils.isEmpty(str.trim());
        return TextUtils.isEmpty(str);

    }

    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    public static String getFullname(String fullname) {
        String str = fullname;
        String[] splitStr = fullname.split("\\s+");
        if (splitStr.length > 1) {
            str = splitStr[0];
            str += " " + (isNotEmpty(splitStr[1]) ? splitStr[1].substring(0, 1) + "." : "");
        } else {

        }

        return str;
    }

    public static void shareApp(Activity mActivity) {

        String tagLine = "Crowd-source your to do list";
        String WebSiteUrlPath = "http://www.snaptaskapp.com/";

        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        share.putExtra(Intent.EXTRA_SUBJECT, tagLine);
        share.putExtra(Intent.EXTRA_TEXT, WebSiteUrlPath);
        mActivity.startActivity(Intent.createChooser(share, "Share link!"));


    }

    public static Date getDateInUTC() {
//        Calendar cal_Two = Calendar.getInstance(TimeZone.getTimeZone("UTC"));

        final SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        final String utcTime = sdf.format(new Date());

        try {
            return sdf.parse(utcTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar cal_Two = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        return cal_Two.getTime();
    }

    public static String getDateInFormat() {

        outputFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        final String utcTime = outputFormat.format(new Date());


        return utcTime;
    }

    // A method to find height of the status bar
    public static int getStatusBarHeight(Activity activity) {
        int result = 0;
        int resourceId = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = activity.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static String getOpenTime(String open_time, String close_time) {

        if (isEmpty(open_time) || isEmpty(close_time))
            return "";

        try {
            SimpleDateFormat dateFormatter = new SimpleDateFormat("HH:mm:ss");
            Date date = dateFormatter.parse(open_time);
            Date date1 = dateFormatter.parse(close_time);

            SimpleDateFormat timeFormatter = new SimpleDateFormat("h:mm a");
            String displayValue1 = timeFormatter.format(date);
            String displayValue2 = timeFormatter.format(date1);

            return displayValue1 + "-" + displayValue2;

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";

    }

    public static String getColor(String color) {

        if (isEmpty(color)) {
            return "#000000";
        }
        color = color.replace("rgb", "");
        color = color.replace("(", "");
        color = color.replace(")", "");
        String[] rgb_list = color.split("\\s*,\\s*");
        //Log.e("Colors==", color + "==" + rgb_list);
        String hex = String.format("#%02x%02x%02x", Integer.parseInt(rgb_list[0]), Integer.parseInt(rgb_list[1]), Integer.parseInt(rgb_list[2]));
        return (hex);
    }

    public static void disableEditText(EditText editText) {
        editText.setFocusable(false);
//        editText.setEnabled(false);
        editText.setCursorVisible(false);
        editText.setKeyListener(null);
//        editText.setBackgroundColor(Color.TRANSPARENT);
    }

    public static String getDeviceID(Activity activity) {
        String android_id = Settings.Secure.getString(activity.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        return android_id;
    }

    public static String convertTime(String inputPattern, String outputPattern, String time) {

        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;

    }


    public static boolean isTimeBetweenTwoTime(String initialTime, String finalTime, String currentTime) throws ParseException {

        String reg = "^([0-1][0-9]|2[0-3]):([0-5][0-9]):([0-5][0-9])$";

        if (initialTime.matches(reg) && finalTime.matches(reg) && currentTime.matches(reg)) {
            boolean valid = false;
            //Start Time
            java.util.Date inTime = new SimpleDateFormat("HH:mm:ss").parse(initialTime);
            Calendar calendar1 = Calendar.getInstance();
            calendar1.setTime(inTime);

            //Current Time
            java.util.Date checkTime = new SimpleDateFormat("HH:mm:ss").parse(currentTime);
            Calendar calendar3 = Calendar.getInstance();
            calendar3.setTime(checkTime);

            //End Time
            java.util.Date finTime = new SimpleDateFormat("HH:mm:ss").parse(finalTime);
            Calendar calendar2 = Calendar.getInstance();
            calendar2.setTime(finTime);

            if (finalTime.compareTo(initialTime) < 0) {
                calendar2.add(Calendar.DATE, 1);
                calendar3.add(Calendar.DATE, 1);
            }

            java.util.Date actualTime = calendar3.getTime();
            if ((actualTime.after(calendar1.getTime()) || actualTime.compareTo(calendar1.getTime()) == 0)
                    && actualTime.before(calendar2.getTime())) {
                valid = true;
            }
            return valid;
        } else {
            throw new IllegalArgumentException("Not a valid time, expecting HH:MM:SS format");
        }

    }
}