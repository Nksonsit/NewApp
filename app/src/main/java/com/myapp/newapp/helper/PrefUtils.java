package com.myapp.newapp.helper;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.myapp.newapp.api.model.Category;
import com.myapp.newapp.api.model.Publisher;
import com.myapp.newapp.api.model.User;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ishan on 24-02-2017.
 */
public class PrefUtils {

    public static String LOGGED_IN = "logged_in";
    public static String ENTERED = "entered";
    public static String FCM_TOKEN = "fcm_token";
    public static String USER_ID = "UserId";
    public static String USER_PROFILE_KEY = "user_profile";
    public static String CATEGORY = "category";
    public static String PUBLISHER = "publisher";
    public static String CATEGORY_SELECTED = "category_selected";
    public static String CODE = "code";

    public static void setLoggedIn(Context ctx, boolean value) {
        Prefs.with(ctx).save(LOGGED_IN, value);
    }

    public static boolean isUserLoggedIn(Context ctx) {
        return Prefs.with(ctx).getBoolean(LOGGED_IN, false);
    }

    public static void setEntered(Context ctx, boolean value) {
        Prefs.with(ctx).save(ENTERED, value);
    }

    public static boolean isUserEntered(Context ctx) {
        return Prefs.with(ctx).getBoolean(ENTERED, false);
    }

    public static void setCategorySelected(Context ctx, boolean value) {
        Prefs.with(ctx).save(CATEGORY_SELECTED, value);
    }

    public static boolean isCategorySelected(Context ctx) {
        return Prefs.with(ctx).getBoolean(CATEGORY_SELECTED, false);
    }


    public static void setFCMToken(Context ctx, String value) {
        Prefs.with(ctx).save(FCM_TOKEN, value);
    }

    public static String getFCMToken(Context ctx) {
        return Prefs.with(ctx).getString(FCM_TOKEN, " ");
    }

    public static void setUserFullProfileDetails(Context context, User userProfile) {

        String toJson = new Gson().toJson(userProfile);
        Prefs.with(context).save(USER_PROFILE_KEY, toJson);
    }

    public static User getUserFullProfileDetails(Context context) {
        Gson gson = new Gson();

        User userProfileDetails = null;

        String getCityString = Prefs.with(context).getString(USER_PROFILE_KEY, "");

        try {
            userProfileDetails = gson.fromJson(getCityString, User.class);

        } catch (Exception e) {

        }
        return userProfileDetails;
    }

    public static void setCategory(Context context,List<Category> categories) {
        if (categories.isEmpty() || categories == null) {
            categories = new ArrayList<>();
        }
        String json = MyApplication.getGson().toJson(categories);
        Prefs.with(context).save(CATEGORY, json);
    }

    public static List<Category> getCategories(Context context) {
        List<Category> categories = new ArrayList<>();
        String json = Prefs.with(context).getString(CATEGORY, "");
        if (TextUtils.isEmpty(json)) {
            return categories;
        }
        Type type = new TypeToken<ArrayList<Category>>() {
        }.getType();
        categories = MyApplication.getGson().fromJson(json, type);
        return categories;
    }

    public static void setPublisher(Context context, List<Publisher> data) {
        if (data.isEmpty() || data == null) {
            data = new ArrayList<>();
        }
        String json = MyApplication.getGson().toJson(data);
        Prefs.with(context).save(PUBLISHER, json);
    }

    public static List<Publisher> getPublisher(Context context) {
        List<Publisher> publishers = new ArrayList<>();
        String json = Prefs.with(context).getString(PUBLISHER, "");
        if (TextUtils.isEmpty(json)) {
            return publishers;
        }
        Type type = new TypeToken<ArrayList<Publisher>>() {
        }.getType();
        publishers = MyApplication.getGson().fromJson(json, type);
        return publishers;
    }

    public static void setCode(Context context, String id) {
        Prefs.with(context).save(CODE,id);
    }
    public static String getCode(Context context) {
        return Prefs.with(context).getString(CODE,null);
    }
}
