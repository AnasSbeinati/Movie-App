package com.example.anoos.movieapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by BoDy on 20/08/2016.
 */
public class SaveSharedPreference
{
    static final String PREF_USER_TYPE= "type";


    static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public static void setTYPE(Context ctx, String type)
    {


        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_USER_TYPE, type);

        editor.commit();
    }


    public static String gettype(Context ctx)
    {
        return getSharedPreferences(ctx).getString(PREF_USER_TYPE, "");
    }

}
