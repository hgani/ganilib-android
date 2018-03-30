package com.gani.lib.http;

import android.net.Uri;

import com.gani.lib.json.GJsonObject;
import com.gani.lib.logging.GLog;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public abstract class GParams<PB extends GParams, IP extends GImmutableParams> implements Serializable {
  // NOTE: Value should only be either String or String[]
  private Map<String, Object> paramMap;

  protected GParams() {
    this(new HashMap<String, Object>());
  }

  protected GParams(Map<String, Object> initialData) {
    this.paramMap = new HashMap<>();
    paramMap.putAll(initialData);
  }

  protected Map<String, Object> getMap() {
    return paramMap;
  }

  public PB put(String name, String value) {
    paramMap.put(name, value);
    return (PB) this;
  }

  private String nullSafeString(Object value) {
    return (value == null) ? null : value.toString();
  }

  public PB put(String name, Long value) {
    return put(name, nullSafeString(value));
  }

  public PB put(String name, Boolean value) {
    return put(name, nullSafeString(value));
  }

  public PB put(String name, Integer value) {
    return put(name, nullSafeString(value));
  }

  public PB put(String name, GJsonObject value) {
    return put(name, nullSafeString(value));
  }

  public PB put(String name, String[] values) {
    paramMap.put(name, values);
    return (PB) this;
  }

  public Object get(String name) {
    return paramMap.get(name);
  }

  public GParams copy() {
    return new Default(paramMap);
  }
  
  public int size() {
    return paramMap.size();
  }

  public Set<Map.Entry<String, Object>> entrySet() {
    return paramMap.entrySet();
  }
  
  public static GParams create() {
    return new Default();
  }

  // Think about this class as a builder and GImmutableParams is the actual (built) object.
  protected abstract IP createImmutable(Map<String, Object> paramMap);

  public final IP toImmutable() {
    return createImmutable(paramMap);
  }

  public static GParams fromNullable(GImmutableParams params) {
    if (params == null) {
      return GParams.create();
    }
    return params.toMutable();
  }

  public IP importFrom(Uri uri) {
    for (String key : uri.getQueryParameterNames()) {
      String value = uri.getQueryParameter(key);
      put(key, value);
    }
    return toImmutable();
  }

  @Override
  public String toString() {
    return toImmutable().toString();
  }

  static class Default extends GParams {
    Default() {
      super();
    }

    Default(Map<String, Object> initialData) {
      super(initialData);
    }

    @Override
    protected GImmutableParams createImmutable(Map paramMap) {
      return new GImmutableParams(paramMap);
    }
  }
}
