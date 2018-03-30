package com.gani.lib.ui.alert;

import android.os.Handler;
import android.view.Gravity;
import android.widget.Toast;

import com.gani.lib.ui.Ui;

public class ToastUtils {
  public static void showNormal(final String str) {
    showNormal(str, Toast.LENGTH_SHORT);
  }

  // Can be run from a background thread, e.g. HttpCallback.Rest.onRestSuccess()
  public static void showNormal(final String str, final int duration) {
    Handler mainHandler = new Handler(Ui.context().getMainLooper());
    Runnable myRunnable = new Runnable() {
      @Override
      public void run() {
        Toast.makeText(Ui.context(), str, duration).show();
      }
    };
    mainHandler.post(myRunnable);
  }

  public static void showNormal(int strId) {
    showNormal(Ui.str(strId));
  }

  public static void showToastInCenter(int resId) {
    Toast toast = Toast.makeText(Ui.context(), resId, Toast.LENGTH_SHORT);
    toast.setGravity(Gravity.TOP, 0, Ui.resources().getDisplayMetrics().heightPixels / 2);
    toast.show();
  }
}
