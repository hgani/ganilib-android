package com.gani.lib.http;

import com.gani.lib.logging.GLog;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class GImmutableParams implements Serializable {
  public static final GImmutableParams EMPTY = new GImmutableParams();

  // NOTE: Value should only be either String or String[]
  private Map<String, Object> paramMap;

  protected GImmutableParams() {
    this(new HashMap<String, Object>());
  }

  protected GImmutableParams(Map<String, Object> initialData) {
    this.paramMap = new HashMap<>();
    paramMap.putAll(initialData);
  }

  protected Map<String, Object> getMap() {
    return new HashMap<>(paramMap);
  }

  public Object get(String name) {
    return paramMap.get(name);
  }

  public int size() {
    return paramMap.size();
  }

  public Set<Map.Entry<String, Object>> entrySet() {
    return paramMap.entrySet();
  }

  protected GParams toMutable() {
    return new GParams.Default(paramMap);
  }

  public String asQueryString() {
    StringBuilder buffer = new StringBuilder();

    for (Map.Entry<String, Object> entry : paramMap.entrySet()) {
      if (entry.getValue() == null) {
        GLog.e(UrlUtils.class, "Null param value for key " + entry.getKey());
      }
      else {
        if (entry.getValue() instanceof String) {
          buffer.append("&").append(entry.getKey()).append("=").append(UrlUtils.encodeUrl((String) entry.getValue()));
        }
        else {
//          // Note that when the array is empty, this is skipped entirely or else it will pass an array with blank string to the server
//          for (String value : (String[]) entry.getValue()) {
//            buffer.append("&").append(entry.getKey()).append("=").append(encodeUrl(value));
//          }
          String[] values = (String[]) entry.getValue();
          if (values.length > 0) {
            for (String value : values) {
              buffer.append("&").append(entry.getKey()).append("=").append(UrlUtils.encodeUrl(value));
            }
          }
          else {
            // This solves 2 issues:
            // - Allow server implementation to be more predictable as the param key (e.g. params[handshake][key]) always exists.
            // - Even more important is that if this is the only param key (e.g. params[handshake][key]), not passing this will make the whole param (params[handshake]) to be missing.
            //
            // The only drawback is that in Rails, this will be received as `[""]` (an array with one empty string), but this can be consistently solved as it also applies to web form.
            buffer.append("&").append(entry.getKey()).append("=");
          }
        }
      }
    }

    return (buffer.length() == 0)? "" : buffer.substring(1);
  }

  @Override
  public String toString() {
    return asQueryString();
  }

  public static GImmutableParams fromNullable(GImmutableParams params) {
    if (params == null) {
      return GImmutableParams.EMPTY;
    }
    return params;
  }

  //
//  public static GImmutableParams fromUri(Uri uri) {
//    GParams params = GParams.create();
//    for (String key : uri.getQueryParameterNames()) {
//      String value = uri.getQueryParameter(key);
//      params.put(key, value);
//    }
//    return params.toImmutable();
//  }
}
