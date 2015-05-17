package jp.co.spajam.androidapp;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by fuji on 2015/05/17.
 */
public class Util {

    public static String getSpPetAccountName(Context context) {
        SharedPreferences sp = context.getSharedPreferences(Const.SP_KEY, Context.MODE_PRIVATE);
        return sp.getString(Const.SP_KEY_PET_ACCOUNT_NAME, "");

    }

    public static void setSpPetAccountName(Context context, String name) {
        SharedPreferences sp = context.getSharedPreferences(Const.SP_KEY, Context.MODE_PRIVATE);
        sp.edit().putString(Const.SP_KEY_PET_ACCOUNT_NAME, name).apply();

    }
}
