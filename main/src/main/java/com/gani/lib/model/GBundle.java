package com.gani.lib.model;

import android.content.Intent;
import android.os.Bundle;

import com.gani.lib.http.GImmutableParams;

import java.io.Serializable;

public class GBundle {
  private Bundle backend;

  protected GBundle(GBundle backend) {
    this(backend.backend);
  }

  public GBundle(Bundle backend) {
    this.backend = backend;
  }

  public final Serializable getSerializable(String key) {
    Serializable object = backend.getSerializable(key);
    // In lower API levels (e.g. Kitkat), T[] is returned as Object[] so we use a wrapper to retain the original array type.
    // See http://stackoverflow.com/questions/30154807/java-lang-classcastexception-java-lang-object-cannot-be-cast-to-pfe-essat-obj
    if (object instanceof ArrayWrapper) {
      return ((ArrayWrapper) object).array;
    }
    return object;
  }

  public final Intent getIntent(String key) {
    return (Intent) backend.getParcelable(key);
  }

  public final String getString(String key) {
    return backend.getString(key);
  }

  public final long getLong(String key) {
    return backend.getLong(key);
  }

  public final int getInt(String key) {
    return backend.getInt(key);
  }

  public final boolean getBoolean(String key) {
    if (!containsKey(key)) {
      throw new IllegalArgumentException("Bundle key not specified: " + key);
    }
    return backend.getBoolean(key);
  }

  public final boolean getBoolean(String key, boolean defaultValue) {
    return backend.getBoolean(key, defaultValue);
  }

  public final Long getNullableLong(String key) {
    if (containsKey(key)) {
      return getLong(key);
    }
    return null;
  }

  private final boolean containsKey(String key) {
    return backend.containsKey(key);
  }

  public final <T> Class<T> getClass(String key) {
    return (Class<T>) getSerializable(key);
  }

  public GImmutableParams getParams(String key) {
    return (GImmutableParams) backend.getSerializable(key);
  }



  public static class ArrayWrapper<T> implements Serializable {
    private T[] array;

    public ArrayWrapper(T[] array) {
      this.array = array;
    }
  }
}
