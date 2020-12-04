package com.example.quranappteacher;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class SharedPrefManager {

    private static final String SHARED_PREF_NAME = "Teacher";
    private static final String KEY_NAME = "keyname";
    private static final String KEY_PHONENUMBER = "keyphone";
    private static final String KEY_ID = "keyid";
    private static final String KEY_USERTYPE = "keyusertype";


    private static com.example.quranappteacher.SharedPrefManager mInstance;
    private static Context mCtx;

    private SharedPrefManager(Context context) {
        mCtx = context;
    }

    public static synchronized com.example.quranappteacher.SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new com.example.quranappteacher.SharedPrefManager(context);
        }
        return mInstance;
    }

    public void adminLogin(Admin admin) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_ID, admin.getId());
        editor.putString(KEY_NAME, admin.getName());
        editor.putString(KEY_PHONENUMBER, admin.getPhoneNumber());
        editor.putInt(KEY_USERTYPE, admin.getUserType());
        editor.apply();
    }


    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_PHONENUMBER, null) != null;
    }

    public Admin getAdmin() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new Admin(
                sharedPreferences.getInt(KEY_ID, -1),
                sharedPreferences.getInt(KEY_USERTYPE, -1),
                sharedPreferences.getString(KEY_NAME, null),
                sharedPreferences.getString(KEY_PHONENUMBER, null)
        );
    }

    public void logout() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        mCtx.startActivity(new Intent(mCtx, Login.class));
    }
}