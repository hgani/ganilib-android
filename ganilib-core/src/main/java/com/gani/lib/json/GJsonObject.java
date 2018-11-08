package com.gani.lib.json;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.gani.lib.logging.GLog;
import com.gani.lib.ui.Ui;
import com.google.gson.internal.bind.util.ISO8601Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public abstract class GJsonObject<JO extends GJsonObject, JA extends GJsonArray> {
  protected JSONObject backend;

  protected GJsonObject() {
    this(new JSONObject());
  }

  protected GJsonObject(GJsonObject object) {
    this(object.backend);
  }

  protected GJsonObject(JSONObject backend) {
    this.backend = backend;
  }

  protected GJsonObject(String jsonString) throws JSONException {
    this(new JSONObject(jsonString));
  }

  // Return GJsonArray instead of List<GJsonObject> so we can provide additional info such as
  // overriding toString().
  @NonNull
  public JA getArray(String name) throws JSONException {
    return createArray(getRawArray(name));
  }

  protected abstract JA createArray(JSONArray array);

  @NonNull
  private JSONArray getRawArray(String name) throws JSONException  {
    return backend.getJSONArray(name);
  }

  @NonNull
  public String[] getStringArray(String name) throws JSONException {
    JSONArray arr = backend.getJSONArray(name);
    String[] elements = new String[arr.length()];
    for (int i = 0; i < elements.length; ++i) {
      elements[i] = arr.getString(i);
    }
    return elements;
  }

  @Nullable
  public String[] getNullableStringArray(String name) {
    try {
//      JSONArray arr = backend.getJSONArray(name);
//      String[] elements = new String[arr.length()];
//      for (int i = 0; i < elements.length; ++i) {
//        elements[i] = arr.getString(i);
//      }
//      return elements;
      return getStringArray(name);
    }
    catch(JSONException e){
      return null;
    }
  }

  @NonNull
  public int[] getIntArray(String name) throws JSONException {
    JSONArray arr = backend.getJSONArray(name);
    int[] elements = new int[arr.length()];
    for (int i = 0; i < elements.length; ++i) {
      elements[i] = arr.getInt(i);
    }
    return elements;
  }

  @Nullable
  public int[] getNullableIntArray(String name) {
    try {
      return getIntArray(name);
    }
    catch(JSONException e){
      return null;
    }
  }

  @NonNull
  public JO getObject(String name) throws JSONException {
    return createObject(getRawObject(name));
  }

  protected abstract JO createObject(JSONObject object);

  @NonNull
  private final JSONObject getRawObject(String name) throws JSONException  {
    return backend.getJSONObject(name);
  }

  protected final boolean isNull(String name) {
    return backend.isNull(name);
  }

  @Nullable
  public JO getNullableObject(String name) {
    try {
      return isNull(name) ? null : getObject(name);
    }
    catch (JSONException e) {
      return null;
    }
  }

  public Iterable<String> keys() {
    return new Iterable<String>() {
      @Override
      public Iterator<String> iterator() {
        return backend.keys();
      }
    };
  }

  public boolean isEmpty() {
    if(backend.names() == null){
      return true;
    }
    return backend.names().length() <= 0;
  }

  @NonNull
  public List<Long> getLongs(String name) throws JSONException {
    JSONArray array = backend.getJSONArray(name);
    List<Long> elements = new ArrayList<Long>(array.length());
    for (int i = 0; i < array.length(); ++i) {
      elements.add(array.getLong(i));
    }
    return elements;
  }

  @NonNull
  public String getString(String name) throws JSONException {
    return backend.getString(name);
  }

  @NonNull
  public String getString(String name, String defaultValue) {
    try {
      return backend.getString(name);
    }
    catch (JSONException e) {
      return defaultValue;
    }
  }

  @Nullable
  public String getNullableString(String name) {
    try {
      // isNull() is needed to check if the property is explicitly specified as null.
      // If the property is not specified (i.e. undefined), we'll get JSONException.
      return backend.isNull(name) ? null : getString(name);
    }
    catch (JSONException e) {
      return null;
    }
  }

  @NonNull
  public long getLong(String name) throws JSONException {
    return backend.getLong(name);
  }

  @NonNull
  public int getInt(String name) throws JSONException {
    return backend.getInt(name);
  }

  @NonNull
  public boolean getBoolean(String name) throws JSONException {
    return backend.getBoolean(name);
  }

  public boolean getBoolean(String name, boolean defaultValue){
    try {
      if(backend.isNull(name)){
        return defaultValue;
      }
      else{
        return getBoolean(name);
      }
    }
    catch (JSONException e) {
      return defaultValue;
    }
  }

  @NonNull
  public double getDouble(String name) throws JSONException {
    return backend.getDouble(name);
  }

  @Nullable
  public Integer getNullableInt(String name) {
    try {
      return backend.isNull(name) ? null : getInt(name);
    }
    catch (JSONException e) {
      return null;
    }
  }

  @NonNull
  public int getInt(String name, int defaultValue) {
    try {
      return backend.isNull(name) ? defaultValue : getInt(name);
    }
    catch (JSONException e) {
      return defaultValue;
    }
  }

  @Nullable
  public Long getNullableLong(String name) {
    try {
      return backend.isNull(name) ? null : getLong(name);
    }
    catch (JSONException e) {
      return null;
    }
  }

  @Nullable
  public Double getNullableDouble(String name) {
    try {
      return backend.isNull(name) ? null : getDouble(name);
    }
    catch (JSONException e) {
      return null;
    }
  }

  @Nullable
  public Boolean getNullableBoolean(String name) {
    try {
      return backend.isNull(name) ? null : getBoolean(name);
    }
    catch (JSONException e) {
      return null;
    }
  }

  @NonNull
  public double getDouble(String name, double defaultValue) {
    try {
      return backend.isNull(name) ? defaultValue : getDouble(name);
    }
    catch (JSONException e) {
      return defaultValue;
    }
  }

  @Nullable
  public Integer getNullableColor(String name) {
    String code = getNullableString(name);
    if (code != null) {
      try {
        return Ui.color(code);
      }
      catch (IllegalArgumentException e) {
        // Do nothing
      }
    }
    return null;
  }

  @Nullable
  public JA getNullableArray(String name) {
    try {
      return backend.isNull(name) ? null : getArray(name);
    }
    catch (JSONException e) {
      return null;
    }
  }

  @NonNull
  public JA getArray(String name, JA defaultValue) {
    try {
      return backend.isNull(name) ? defaultValue : getArray(name);
    }
    catch (JSONException e) {
      return defaultValue;
    }
  }

  @NonNull
  public Date getDate(String name) throws JSONException {
    try {
      return ISO8601Utils.parse(getString(name), new ParsePosition(0));
    }
    catch (ParseException e) {
      throw new JSONException(e.getLocalizedMessage());
    }
  }

  @Nullable
  public Date getNullableDate(String name) {
    try {
      return backend.isNull(name) ? null : getDate(name);
    }
    catch (JSONException e) {
      return null;
    }
  }

  private JO self() {
    return (JO) this;
  }

//  public JO put(String name, String value) {
//    try {
//      backend.put(name, value);
//    }
//    catch (JSONException e) {
//      GLog.e(getClass(), "Failed adding value to JSON", e);
//    }
//    return self();
//  }

  public JO put(String name, Object value) {
    try {
      if (value instanceof GJsonObject) {
        value = ((GJsonObject) value).backend;
      }
      backend.put(name, value);
    }
    catch (JSONException e) {
      GLog.e(getClass(), "Failed adding value to JSON", e);
    }
    return self();
  }

  @Override
  @NonNull
  public String toString() {
    return backend.toString();
  }



  public static class Default extends GJsonObject<GJsonObject.Default, GJsonArray.Default> {
    public Default(String str) throws JSONException {
      super(str);
    }

    public Default(JSONObject object) {
      super(object);
    }

    @Override
    protected GJsonArray.Default createArray(JSONArray array) {
      return new GJsonArray.Default(array);
    }

    @Override
    protected GJsonObject.Default createObject(JSONObject object) {
      return new GJsonObject.Default(object);
    }
  }
}

