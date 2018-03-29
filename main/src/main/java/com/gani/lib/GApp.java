package com.gani.lib;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Handler;
import android.provider.Settings;

import com.gani.lib.collection.SelfTruncatingSet;
import com.gani.lib.http.GHttp;
import com.gani.lib.ui.Ui;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import static android.R.attr.versionName;

public abstract class GApp {
  private static GApp instance;

  public static void register(GApp i) {
    instance = i;
  }

  public static GApp instance() {
    return instance;
  }

//  private static String versionName;
//  private static String codeName;
//  private static String deviceId;
//  private static int apiVersion;
//
//  public static void init(Context c, Handler h, String cn, int av) {
//    Ui.init(c, h);
//
//    versionName = calculateApplicationVersionName(c);
//    codeName = cn;
//    deviceId = Settings.Secure.getString(c.getContentResolver(), Settings.Secure.ANDROID_ID);
//    apiVersion = av;
//  }

  private String versionName;
  private String codeName;
  private String deviceId;
  private int apiVersion;

  // NOTE: Make sure the handler is created in UI thread.
  public GApp(Context c, Handler h, String cn, int av) {
    Ui.init(c, h);

    versionName = calculateApplicationVersionName(c);
    codeName = cn;
    deviceId = Settings.Secure.getString(c.getContentResolver(), Settings.Secure.ANDROID_ID);
    apiVersion = av;
  }

  private static String calculateApplicationVersionName(Context context) {
    String versionName;
    try {
      PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
      versionName = info.versionName;
      if (versionName == null) {  // We only get a proper version name if we deploy using ant.
        versionName = "test_mode"; // Version in the metrics DB is limited to 10 chars only. Make sure this isn't longer than that.
      }
    }
    catch (PackageManager.NameNotFoundException e) {
      versionName = "unknown";
    }
    return versionName;
  }

  public static String getVersionName() {
    return instance.versionName;
  }

  public static String getCodeName() {
    return instance.codeName;
  }

  public static String getDeviceId() {
    return instance.deviceId;
  }

  public static int getApiVersion() {
    return instance.apiVersion;
  }

  private static Gson GSON = new GsonBuilder().registerTypeAdapter(SelfTruncatingSet.class, new SelfTruncatingSet.GsonSerializer()).create();
  private static Gson DEFAULT_GSON = new Gson();

  public static Gson gson() {
    return GSON;
  }

  public static Gson defaultGson() {
    return DEFAULT_GSON;
  }

  public int notificationIconRes() {
    return R.drawable.icon_notification_logo;
  }
}
