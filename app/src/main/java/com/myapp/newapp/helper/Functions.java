package com.myapp.newapp.helper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.myapp.newapp.R;
import com.myapp.newapp.api.model.Category;
import com.myapp.newapp.api.model.Publisher;
import com.myapp.newapp.ui.LoginActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ishan on 31-05-2017.
 */

public class Functions {
    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private static DialogOptionsSelectedListener dialogOptionsSelectedListener;

    public static Typeface getTypeFace(Context context, String location) {
        return Typeface.createFromAsset(context.getResources().getAssets(), location);
    }

    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void hideKeyPad(Context context, View view) {
        InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(view.getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public static boolean emailValidation(String email) {
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

    public static void LogE(String key, String value) {
//        if (BuildConfig.DEBUG) {
        Log.e(key, value);
//        }
    }

    public static String getDeviceId(Context context) {
        return Settings.Secure.getString(context.getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        return manufacturer + " " + model;
    }

    public static String jsonString(Object obj) {
        return "" + MyApplication.getGson().toJson(obj);
    }


    public static void logout(Context context) {
        PrefUtils.setLoggedIn(context, false);
        PrefUtils.setEntered(context, false);

        Intent intent = new Intent(context, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
        ((Activity) context).finish();

    }

    public static void showAlertDialogWithTwoOpt(Context mContext, String message, final DialogOptionsSelectedListener dialogOptionsSelectedListener, String yesOption) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton(yesOption, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (dialogOptionsSelectedListener != null)
                            dialogOptionsSelectedListener.onSelect(true);
                        dialog.dismiss();
                    }
                });
        AlertDialog alert = builder.create();

        alert.show();
    }

    public static String getFormatedCategory(Context context, String category) {
        String[] splitCatId = category.split(",");
        String output = "";
        for (int i = 0; i < splitCatId.length; i++) {
            List<Category> catList = PrefUtils.getCategories(context);
            for (int j = 0; j < catList.size(); j++) {
                if (splitCatId[i].trim().equals(catList.get(j).getId() + "")) {
                    Log.e(splitCatId[i], catList.get(j).getId() + "");
                    output = output + "#" + catList.get(j).getName();
                    if (i != splitCatId.length - 1) {
                        output = output + ", ";
                    }
                }
            }
        }
        return output;
    }

    public static String getPublisher(Context context, String publisherId) {
        List<Publisher> list = PrefUtils.getPublisher(context);
        String publisherName = "";
        for (int i = 0; i < list.size(); i++) {
            String pubId = list.get(i).getId().trim();
            if (list.get(i).getId().trim().equals(publisherId.trim())) {
                publisherName = list.get(i).getName();
            }
        }
        return publisherName;
    }

    public interface DialogOptionsSelectedListener {
        void onSelect(boolean isYes);
    }

    public static void setDialogOptionsSelectedListener(DialogOptionsSelectedListener _dialogOptionsSelectedListener) {
        dialogOptionsSelectedListener = _dialogOptionsSelectedListener;
    }

    public static String getCheckText(String input) {
        if (input != null && input.trim().length() > 0) {
            return input;
        } else {
            return "-";
        }
    }

    public static String getCheckTextNull(String input) {
        if (input != null && input.trim().length() > 0) {
            return input;
        } else {
            return "";
        }
    }

    public static String getDateToTimemilli(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long timeInMilliseconds = 0;
        try {
            Date mDate = sdf.parse(date);
            timeInMilliseconds = mDate.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeInMilliseconds + "";
    }

    public static String getDateFormated(String date) {
        Log.e("date", date);
        String output = null;
        if (IsTodayDate(date)) {

            long currentTime = new Date().getTime();
            long inputTime = Long.parseLong(getDateToTimemilli(date));

            Log.e("diff", (currentTime - inputTime) + "");
            Log.e("min", TimeUnit.MILLISECONDS.toMinutes(currentTime - inputTime) + "");
            Log.e("sec", TimeUnit.MILLISECONDS.toSeconds(currentTime - inputTime) + "");

            if (TimeUnit.MILLISECONDS.toSeconds(currentTime - inputTime) < 60) {
                output = TimeUnit.MILLISECONDS.toSeconds(currentTime - inputTime) + " sec ago";
            } else if (TimeUnit.MILLISECONDS.toMinutes(currentTime - inputTime) < 60) {
                output = TimeUnit.MILLISECONDS.toMinutes(currentTime - inputTime) + " min ago";
            } else {
                int hour = (int) (TimeUnit.MILLISECONDS.toMinutes(currentTime - inputTime) / 60);
                output = hour + " hour ago";
            }

        } else {
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date1 = null;
            try {
                date1 = sdf1.parse(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            SimpleDateFormat sdf2 = new SimpleDateFormat("dd MMM yyyy");
            if (date1 != null) {
                output = sdf2.format(date1);
            } else {
                output = date;
            }
        }
        return output;
    }

    public static boolean IsTodayDate(String date) {
        Date todayDate = new Date(), inputDate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd MMM yyyy");
        try {
            todayDate = sdf1.parse(sdf1.format(new Date()));
            inputDate = sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            Log.e("error", e.toString());
        }
        Log.e("todayDate", todayDate.toString());
        Log.e("cDate", inputDate.toString());
        if (todayDate.after(inputDate)) {
            return false;
        } else {
            return true;
        }
    }

    public static Typeface getRegularFont(Context _context) {
        Typeface tf = Typeface.createFromAsset(_context.getAssets(), "font/regular.ttf");
        return tf;
    }

    public static Typeface getBoldFont(Context _context) {
        Typeface tf = Typeface.createFromAsset(_context.getAssets(), "font/bold.ttf");
        return tf;
    }

    public static void fireIntent(Context context, Intent intent, boolean isNewActivity) {
        Activity activity = (Activity) context;
        context.startActivity(intent);
        if (!isNewActivity) {
            activity.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        } else {
            activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
    }
}
