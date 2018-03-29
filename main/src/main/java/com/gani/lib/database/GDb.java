package com.gani.lib.database;

import android.database.sqlite.SQLiteException;

public abstract class GDb {
  private static GDb instance;

  public static void register(GDb i) throws DatabaseInitializer.DbInitializationFailure {
    instance = i;

    DatabaseInitializer.init(instance.createInitializer());
    try {
      DatabaseInitializer.getReadableDb();
    }
    catch (SQLiteException e) {
      throw new DatabaseInitializer.DbInitializationFailure(e);
    }
  }

//  private static void initDatabase() {
//    DatabaseHelper.init();
//    try {
//      DatabaseHelper.getReadableDb();
//    }
//    catch (SQLiteException e) {
//      CvLog.e(App.class, "This is likely because we forget to drop all tables", e);
//      NotificationDrawer.putGenericMessage(NotificationDrawer.DATABASE_UPGRADE_ERROR,
//          R.string.label_app_error_database_upgrade_title,
//          R.string.label_app_error_database_upgrade_message,
//          Alert.intent(R.string.label_app_error_database_upgrade_message));
//    }
//  }

  public static GDb instance() {
    return instance;
  }

//  protected abstract String authorityName();
  protected abstract DatabaseInitializer createInitializer();
}
