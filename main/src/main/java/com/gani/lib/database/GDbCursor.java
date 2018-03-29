package com.gani.lib.database;

import android.database.Cursor;

import com.gani.lib.GApp;
import com.gani.lib.logging.GLog;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

// We should start using this instead of Cursor
public class GDbCursor implements GDbData {
  private Cursor cursor;
  
  public GDbCursor(Cursor cursor) {
    this.cursor = cursor;
  }

  protected <T, C extends GDbCursor> T executeFirstRow(AutoCleanupCommand<T, C> command) {
    try {
      moveToFirst();
      return command.execute((C) this);
    } finally {
      close();
    }
  }

  protected <T, C extends GDbCursor> T executeFirstRowIfExist(AutoCleanupCommand<T, C> command) {
    try {
      if (moveToFirst()) {
        return command.execute((C) this);
      }
      return null;
    } finally {
      close();
    }
  }

  public interface AutoCleanupCommand<T, C extends GDbCursor> {
    T execute(C cursor);
  }

  // For debugging
  public void logColumnNames() {
    String[] columnNames = cursor.getColumnNames();
    for (String name : columnNames) {
      GLog.t(getClass(), "Column " + name + " => " + cursor.getColumnIndex(name));
    }
  }
  
  public boolean moveToFirst() {
    return cursor != null && cursor.moveToFirst();
  }

  public boolean moveToPosition(int i) {
    return cursor != null && cursor.moveToPosition(i);
  }

  public boolean moveToPrevious() {
    return cursor != null && cursor.moveToPrevious();
  }

  public boolean moveToNext() {
    return cursor != null && cursor.moveToNext();
  }

  public String getString(int index) {
    return cursor.getString(index);
  }

  public boolean getBoolean(int index) {
    return cursor.getInt(index) == GDbRow.TRUE;
  }
  
  public long getLong(int index) {
    return cursor.getLong(index);
  }
  
  public int getInt(int index) {
    return cursor.getInt(index);
  }
  
  public Integer getNullableInt(int index) {
    return cursor.isNull(index) ? null : cursor.getInt(index);
  }
  
  public void close() {
    cursor.close();
  }
  
  public int getIndex(String name) {
    return cursor.getColumnIndex(name);
  }

  public String getString(String name) {
    return cursor.getString(getIndex(name));
  }

//  public VanityName getVanityName(String name) {
//    return VanityName.fromString(getString(name));
//  }

  public boolean getBoolean(String name) {
    return getBoolean(getIndex(name));
  }

  public Boolean getNullableBoolean(int index) {
    return cursor.isNull(index) ? null : getBoolean(index);
  }

  public Boolean getNullableBoolean(String name) {
    return getNullableBoolean(getIndex(name));
  }

  public int getInt(String name) {
    return getInt(getIndex(name));
  }

  public Integer getNullableInt(String name) {
    return getNullableInt(getIndex(name));
  }

  @Override
  public long getLong(String name) {
    return getLong(getIndex(name));
  }

  public Long getNullableLong(int index) {
    return cursor.isNull(index) ? null : cursor.getLong(index);
  }

  public Long getNullableLong(String name) {
    return getNullableLong(getIndex(name));
  }

//  public Uri getInternalUrl(String pathKey) {
//    return Uri.parse(Build.INSTANCE.getWebPrefix() + getString(pathKey));
//  }

  // See https://code.google.com/p/guava-libraries/wiki/ReflectionExplained
  public <T> T getObject(String name, TypeToken<T> typeToken) {
    return getObject(getIndex(name), typeToken);
  }

  public <T> T getObject(int index, TypeToken<T> typeToken) {
    // Beware that incompatible objects might not throw an exception, e.g. a field that exists on POJO but not on JSON.
    // In this case, the field will be null, which can cause problems.
    // See http://stackoverflow.com/questions/3163193/strict-json-parsing-with-googles-gson
    try {
      return GApp.gson().fromJson(getString(index), typeToken.getType());
    }
    catch (JsonParseException e) {
      GLog.w(getClass(), "Failed parsing stored object: " , e);
      return null;
    }
    catch (IllegalArgumentException e) {
      GLog.w(getClass(), "Incompatible stored object: " , e);
      return null;
    }
  }
}
