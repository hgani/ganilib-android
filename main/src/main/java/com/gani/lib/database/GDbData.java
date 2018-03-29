package com.gani.lib.database;

import com.google.gson.reflect.TypeToken;

public interface GDbData {
  int getInt(String key);
  long getLong(String key);
  boolean getBoolean(String key);
  Long getNullableLong(String key);
  Boolean getNullableBoolean(String key);

  String getString(String key);
  <T> T getObject(String name, TypeToken<T> typeToken);
}
