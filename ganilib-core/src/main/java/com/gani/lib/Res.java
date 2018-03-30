package com.gani.lib;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.provider.Settings;

import com.gani.lib.prefs.Prefs;
import com.gani.lib.ui.Ui;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Res {
  // This won't interfere with prefs from other projects. See https://stackoverflow.com/questions/36777401/does-sharedpreferences-name-need-to-be-unique
  private static final String PREF_NAME = "__ganilib_prefs";

  public static Context context() {
    return Ui.context();
  }

  public static String deviceId() {
    return Settings.Secure.getString(context().getContentResolver(), Settings.Secure.ANDROID_ID);
  }

  public static Prefs prefs(String name) {
    return new Prefs(context().getSharedPreferences(name, Context.MODE_PRIVATE));
  }

  public static Prefs libPrefs() {
    return prefs(PREF_NAME);
  }

  public static Prefs defaultPrefs() {
    return new Prefs(PreferenceManager.getDefaultSharedPreferences(context()));
  }

  private static AssetManager assets() {
    return context().getAssets();
  }

  public static String assetText(String fileName) throws IOException {
//    AssetManager am =
    InputStream is = assets().open(fileName);

    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
    StringBuilder out = new StringBuilder();
    String line;
    while ((line = reader.readLine()) != null) {
      out.append(line);
    }
    reader.close();

    return out.toString();
  }

  public static JSONObject assetJsonObject(String path) throws JSONException {
    try{
      String jsonAsString = assetText(path);
      return new JSONObject(jsonAsString);
    }
    catch(IOException e){
      throw new JSONException(e.getMessage());
    }
  }

  public static Drawable assetDrawable(String fileName) throws IOException {
    InputStream ims = assets().open(fileName);
    return Drawable.createFromStream(ims, null);
//    try {
//      // set image to ImageView
//      mImage.setImageDrawable(d);
//    }
//    catch(IOException ex) {
//      return;
//  }
  }
}
