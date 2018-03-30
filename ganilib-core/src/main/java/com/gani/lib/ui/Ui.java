package com.gani.lib.ui;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;

import com.gani.lib.logging.GLog;

public class Ui {
  private static Context appContext;
  private static Resources resources;
  private static Handler uiHandler;

  public static void init(Context c, Handler h) {
    appContext = c;
    resources = c.getResources();
    uiHandler = h;
  }

  public static Context context() {
    return appContext;
  }

  public static Resources resources() {
    return resources;
  }

  public static Drawable drawable(int resId) {
    return ((Build.VERSION.SDK_INT >= 21) ? appContext.getDrawable(resId) : resources.getDrawable(resId));
  }

  public static String str(int resId, Object... formatArgs) {
    return appContext.getString(resId, formatArgs);
  }

  public static String quantityStr(int resId, int quantity, Object... formatArgs) {
    return appContext.getResources().getQuantityString(resId, quantity, formatArgs);
  }

  public static int dimen(int resId) {
    return (int) resources.getDimension(resId);
  }

  public static int color(int resId) {
    return resources.getColor(resId);
  }

  private static String expandColorIfNecessary(String code) {
    if (code.length() == 3) {
      String result = "";
      for (char c : code.toCharArray()) {
        result += ("" + c + c);
      }
      return result;
    }
    return code;
  }

  public static int color(String code) throws IllegalArgumentException {

    if (code != null) {
      if (code.startsWith("#")) {
        code = "#" + expandColorIfNecessary(code.substring(1));
      }
      try {
        return Color.parseColor(code);
      }
      catch (IllegalArgumentException e) {
        throw e;
      }
      catch (StringIndexOutOfBoundsException e) {
        throw new IllegalArgumentException(e);
      }
    }
    throw new IllegalArgumentException();
  }

  public static String expandColorIfNecassary(String code){
    if (code.length() == 3) {
      String result = "";
      for (char c : code.toCharArray()) {
        result += ("" + c + c);
      }
      return result;
    }
    return code;
  }

  public static int integer(int resId) {
    return resources.getInteger(resId);
  }

  // Use this to:
  // Ensure that async task is created and executed on UI thread and
  // Ensure that listeners are executed on UI thread
  public static void run(Runnable command) {
    uiHandler.post(command);
  }

  public static void delay(Runnable command, int delayInMillis) {
    uiHandler.postDelayed(command, delayInMillis);
  }

  public static Typeface ttf(String ttf) {
    return Typeface.createFromAsset(appContext.getAssets(), ttf);
  }
}
