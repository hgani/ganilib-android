package com.gani.lib.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.gani.lib.GApp;
import com.gani.lib.ui.Ui;

public class NotificationDrawer {
  public static final int C2DM_ID_SETUP_FAILURE = 1;
  public static final int C2DM_ID_FAILURE = 2;
  public static final int C2DM_ID_SUCCESS = 3;
  public static final int ERROR_GENERIC= 4;
  public static final int ERROR_REPORT = 5;
  public static final int DATABASE_UPGRADE_ERROR= 6;
  public static final int HANDSHAKING_ERROR= 7;

//  public static void putGenericErrorMessage(String message) {
//    putGenericMessage(ERROR_GENERIC, null, App.str(R.string.title_error_notification), message,
//        Alert.intent(message), true);
//  }

  public static void putGenericMessage(int id, int titleId, int messageId, Intent intent) {
    putGenericMessage(id, titleId, messageId, intent, true);
  }

  public static void putGenericMessage(int id, int titleId, int messageId, Intent intent, boolean autoCancel) {
    putGenericMessage(id, null, Ui.str(titleId), Ui.str(messageId), intent, autoCancel);
  }

  public static void putGenericMessage(int id, String title, String message, Intent intent) {
    putGenericMessage(id, null, title, message, intent, true);
  }

//  public static void putGeneric(int id, String tag, String title, String message, Intent intent) {
//    putGenericMessage(id, tag, title, message, ScreenPostList.intentToLaunchSubscreen(intent), true);
//  }
  
  public static void cancel(String tag) {
    manager().cancel(tag, C2DM_ID_SUCCESS);
  }
  
  private static NotificationManager manager() {
    return (NotificationManager) Ui.context().getSystemService(Context.NOTIFICATION_SERVICE);
  }

  private static void putGenericMessage(int id, String tag, String title, String message, Intent intent, boolean autoCancel) {
    manager().notify(tag, id, createNotification(title, message, intent, autoCancel));
  }

  private static Notification createNotification(String title, String message, Intent intent, boolean autoCancel) {
    Context context = Ui.context();
    Notification.Builder builder = new Notification.Builder(context)
      .setSmallIcon(GApp.instance().notificationIconRes())
      .setWhen(System.currentTimeMillis())
      .setTicker(title)
      .setContentTitle(title)
      .setContentText(message)
      // Pass a unique request number or else this intent will replace the previous one.
      .setContentIntent(PendingIntent.getActivity(context, (int) (Math.random() * 100), intent, 0));
    if (autoCancel) {
      builder.setAutoCancel(true);
    }
    return builder.getNotification();
  }
}
