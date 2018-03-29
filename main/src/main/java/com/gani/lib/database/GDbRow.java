package com.gani.lib.database;

import android.content.ContentValues;

import com.gani.lib.GApp;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

public class GDbRow implements GDbData {
  public static final int TRUE = 1;
  public static final int FALSE = 0;

  private ContentValues values;
  
  public GDbRow() {
    this.values = new ContentValues();
  }

  // TODO: Experiment with replacing put() with this.
  public GDbRow putNonNull(String column, String value) {
    if (value != null) {
      values.put(column, value);
    }
    return this;
  }

  public GDbRow put(String column, String value) {
    values.put(column, value);
    return this;
  }

  public GDbRow put(String column, Enum value) {
    if (value != null) {
      values.put(column, value.name());
    }
    return this;
  }

  public GDbRow put(String column, Long value) {
    values.put(column, value);
    return this;
  }

  public GDbRow put(String column, Integer value) {
    values.put(column, value);
    return this;
  }

  public GDbRow put(String column, DbOrder value) {
    values.put(column, value.nextVal());
    return this;
  }

  public GDbRow put(String column, Boolean value) {
    values.put(column, value ? TRUE : FALSE);
    return this;
  }

//  public GDbRow put(String column, Image value) {
//    values.put(column, App.gson().toJson(value));
//    return this;
//  }
//
//  public GDbRow put(String column, Images value) {
//    values.put(column, App.gson().toJson(value));
//    return this;
//  }
//
//  public GDbRow put(String column, Author value) {
//    values.put(column, App.gson().toJson(value));
//    return this;
//  }
//
//  public GDbRow put(String column, Authors value) {
//    values.put(column, App.gson().toJson(value));
//    return this;
//  }
//
//  public GDbRow put(String column, VoGuild value) {
//    values.put(column, App.gson().toJson(value));
//    return this;
//  }

  public GDbRow put(String column, Object value) {
    values.put(column, GApp.gson().toJson(value));
    return this;
  }

  public GDbRow putNull(String column) {
    values.putNull(column);
    return this;
  }

//  public Set<Map.Entry<String, Object>> entries() {
//    return values.valueSet();
//  }


//  public Long getLong(String key) {
//    return values.getAsLong(key);
//  }

  public ContentValues toContentValues() {
    return values;
  }

  public <T> T getObject(String column, TypeToken<T> typeToken) {
    // Beware that incompatible objects might not throw an exception, e.g. a field that exists on POJO but not on JSON.
    // In this case, the field will be null, which can cause problems.
    // See http://stackoverflow.com/questions/3163193/strict-json-parsing-with-googles-gson
    try {
      return GApp.gson().fromJson(getString(column), typeToken.getType());
    }
    catch (JsonParseException e) {
      return null;
    }
  }

  @Override
  public boolean getBoolean(String column) {
    return values.getAsInteger(column) == TRUE;
  }

  @Override
  public Boolean getNullableBoolean(String column) {
    if (values.get(column) == null) {
      return null;
    }
    return values.getAsInteger(column) == TRUE;
  }

  @Override
  public long getLong(String column) {
    return values.getAsLong(column);
  }

  @Override
  public Long getNullableLong(String key) {
    return values.getAsLong(key);
  }

  @Override
  public int getInt(String column) {
    return values.getAsInteger(column);
  }

  @Override
  public String getString(String column) {
    return values.getAsString(column);
  }

//  @Override
//  public Image getImage(String column) {
//    return getObject(column, new TypeToken<Image>() {
//    });
//  }
//
//  @Override
//  public Images getImages(String column) {
//    return getObject(column, new TypeToken<Images>() {
//    });
//  }
//
//  @Override
//  public Author getAuthor(String column) {
//    return getObject(column, new TypeToken<Author>() {
//    });
//  }
//
//  @Override
//  public Authors getAuthors(String column) {
//    return getObject(column, new TypeToken<Authors>() {
//    });
//  }
//
//  @Override
//  public VoGuild getGuild(String column) {
//    return getObject(column, new TypeToken<VoGuild>() {
//    });
//  }
//
//  @Override
//  public VoGuilds getGuilds(String column) {
//    return getObject(column, new TypeToken<VoGuilds>() {
//    });
//  }
}
