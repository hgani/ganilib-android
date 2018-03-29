package com.gani.lib.database;

import com.google.gson.reflect.TypeToken;

import static com.gani.lib.database.GDataProvider.KeyValue.COLUMN_KEY;
import static com.gani.lib.database.GDataProvider.KeyValue.COLUMN_VALUE;
import static com.gani.lib.database.GDataProvider.KeyValue.TABLE_HELPER;

public class GDbValue {
  public static void set(String key, Object value) {
    DbKeyValue instance = new DbKeyValue(key, value);

    if (get(key, new TypeToken<Object>() {}) == null) {
      TABLE_HELPER.insertRows(instance);
    }
    else {
      TABLE_HELPER.updateRow(instance, new String[] { COLUMN_KEY }, new String[] { key });
    }
  }

  // NOTE: This method has to receive a TypeToken instead of creating it itself in order for correct generic type to get passed -- in other words, TypeToken has to be created by the caller passing the explicit class type.
  public static <T> T get(String key, final TypeToken<T> token) {
    GDbCursor cursor = TABLE_HELPER.query(new String[] { COLUMN_KEY }, new String[] { key });

    return cursor.executeFirstRowIfExist(new GDbCursor.AutoCleanupCommand<T, GDbCursor>() {
      @Override
      public T execute(GDbCursor cursor) {
        return cursor.getObject(COLUMN_VALUE, token);
      }
    });
  }

  public static void remove(String key) {
    TABLE_HELPER.deleteRows(new String[] { COLUMN_KEY }, new String[] { key });
  }

  public static void clear(){
    TABLE_HELPER.deleteRows(new String[] { }, new String[] { });
  }

  public static String getString(String key) {
    return get(key, new TypeToken<String>() {});
//    GDbCursor cursor = TABLE_HELPER.query(new String[] { COLUMN_KEY }, new String[] { key });
//
//    return cursor.executeFirstRowIfExist(new GDbCursor.AutoCleanupCommand<String, GDbCursor>() {
//      @Override
//      public String execute(GDbCursor cursor) {
//        return cursor.getString(COLUMN_VALUE);
//      }
//    });
  }
}
