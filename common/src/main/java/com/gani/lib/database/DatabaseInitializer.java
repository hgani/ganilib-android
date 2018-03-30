package com.gani.lib.database;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.gani.lib.logging.GLog;
import com.gani.lib.ui.Ui;

public abstract class DatabaseInitializer extends SQLiteOpenHelper {
  private static final String DATABASE_NAME = "default.db";

  // Singleton to ensure that only 1 connection is shared, thereby avoiding SQLiteDatabaseLockedException.
  // See http://kagii.com/post/6828016869/android-sqlite-locking
  private static DatabaseInitializer singleInstance;

  public static SQLiteDatabase getReadableDb() {
    return singleInstance.getReadableDatabase();
  }

  public static SQLiteDatabase getWritableDb() {
    return singleInstance.getWritableDatabase();
  }

  static void init(DatabaseInitializer initializer) {
    singleInstance = initializer;
  }

  protected DatabaseInitializer(int databaseVersion) {
    super(Ui.context(), DATABASE_NAME, null, databaseVersion);
  }
  
//  private int readCredentialVersion(SQLiteDatabase database) {
//    Cursor cursor = null;
//    try {
//      cursor = database.query(GDataProvider.Credential.TABLE, new String[] { GDataProvider.Credential.COLUMN_VERSION }, null, null, null, null, null);
//      if (cursor.moveToFirst()) {
//        return cursor.getInt(0);
//      }
//      return 0;  // Logged out, so it doesn't matter if we recreate the table anyway.
//    }
//    catch (SQLiteException e) {  // Only for the first upgrade, because "version" didn't use to exist.
//      return 0;
//    }
//    finally {
//      OldResourceCloser.close(cursor);
//    }
//  }

  // TODO: Review why we chose to use integers for foreign keys and times. Shouldn't they be longs (whatever the equivalent is in SQLite)?
//  @Override
//  public void onCreate(SQLiteDatabase database) {
//
//  }
//
//  private static void createIndex(SQLiteDatabase database, String tableName, String columnName) {
//    database.execSQL("create index " + tableName + "_" + columnName + "_idx on " + tableName + "(" + columnName + ");");
//  }
//
//  protected static void dropTableIfExists(SQLiteDatabase database, String tableName) {
//    GLog.t(DatabaseInitializer.class, "Dropping table: " + tableName);
//    database.execSQL("drop table if exists " + tableName);
//  }

  @Override
  public final void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
    GLog.t(DatabaseInitializer.class, "Dropping all tables " );
//    for (DataUris dataUris : GDataProvider.REGISTERED_TABLE_URIS) {
    for (GDbTable table : GDataProvider.registeredTables()) {
      if (shouldDeleteOnUpgrade(table.getDataUris())) {
        table.dropTableIfExists(database);
//        dropTableIfExists(database, dataUris.getTableName());
      }
    }

    onCreate(database);
  }

  protected boolean shouldDeleteOnUpgrade(DataUris tableUris) {
    return true;
  }
  
  // Only introduced in API level 11 and above. See http://stackoverflow.com/questions/11365706/ondowngrade-in-sqliteopenhelper-or-another-way-to-not-crash-when-downgrading
  @Override
  public final void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    onUpgrade(db, oldVersion, newVersion);
  }

  public static class DbInitializationFailure extends Exception {
    DbInitializationFailure(Exception e) {
      super(e);
    }
  }

  public void createTables(SQLiteDatabase database, GDbTable... tables) {
    for (GDbTable table : tables) {
      table.createTable(database);
    }
  }

  public void recreateTables(SQLiteDatabase database, GDbTable... tables) {
    for (GDbTable table : tables) {
      table.recreateTable(database);
    }
  }
}
