package com.gani.lib.database;

import android.provider.BaseColumns;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ProjectionUtils {
  static Map<String, String> projectionMapFromClass(Class<? extends BaseColumns> tableClass) {
    Map<String, String> projectionMap = new HashMap<String, String>();
    try {
      projectionMap.put(BaseColumns._ID, BaseColumns._ID);
      for (Field f : tableClass.getDeclaredFields()) {
        String name = f.getName();
        if (name.startsWith("COLUMN_")) {
          String value = (String) f.get(null);
          projectionMap.put(value, value);
        }
      }
    }
    catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    }
    return projectionMap;
  }
  
  // TODO: Shouldn't be needed, can simply pass null to CursorLoader
  public static String[] projectionsFromClass(Class<? extends BaseColumns> tableClass) {
    Set<String> projectionSet = projectionMapFromClass(tableClass).keySet();
    return projectionSet.toArray(new String[projectionSet.size()]);
  }
}