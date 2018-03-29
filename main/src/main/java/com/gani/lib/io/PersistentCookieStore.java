package com.gani.lib.io;

import android.content.Context;
import android.util.Log;

import com.gani.lib.logging.GLog;
import com.gani.lib.ui.Ui;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PersistentCookieStore implements CookieStore {
  public static final String COOKIE_FILE = "cookies";

  private CookieStore delegate;
  
  public PersistentCookieStore(CookieStore delegate) {
    this.delegate = delegate;
    
    deserializeCookieMap();
  }
  
  @Override
  public void add(URI uri, HttpCookie cookie) {
    delegate.add(uri, cookie);
    serializeCookieMap();
  }

  @Override
  public List<HttpCookie> get(URI uri) {
    return delegate.get(uri);
  }

  @Override
  public List<HttpCookie> getCookies() {
    return delegate.getCookies();
  }

  @Override
  public List<URI> getURIs() {
    return delegate.getURIs();
  }

  @Override
  public boolean remove(URI uri, HttpCookie cookie) {
    boolean removed = delegate.remove(uri, cookie);
    serializeCookieMap();
    return removed;
  }

  @Override
  public boolean removeAll() {
    boolean hadCookies = delegate.removeAll();
    serializeCookieMap();
    return hadCookies;
  }
  
  @SuppressWarnings("unchecked")
  private void deserializeCookieMap() {
    try {
      GLog.t(getClass(), "DESERIALIZE COOKIE1");
      FileInputStream fileIn = Ui.context().openFileInput(COOKIE_FILE);

      GLog.t(getClass(), "DESERIALIZE COOKIE2");
      ObjectInputStream objectIn = new ObjectInputStream(fileIn);
      GLog.t(getClass(), "DESERIALIZE COOKIE3");
      importFromDelegate((Map<URI, List<SerializableCookie>>) objectIn.readObject());
      objectIn.close();
    }
    catch (FileNotFoundException e) {
      // This is very possible and not problem at all.
    }
    catch (IOException e) {
      GLog.e(getClass(), "Failed deserializing cookies", e);
    }
    catch (ClassNotFoundException e) {
      GLog.e(getClass(), "Failed deserializing cookies", e);
    }
  }
  
  private void importFromDelegate(Map<URI, List<SerializableCookie>> map) {
    GLog.t(getClass(), "IMPORT COOKIE1");

    for (Map.Entry<URI, List<SerializableCookie>> entry : map.entrySet()) {
      GLog.t(getClass(), "IMPORT COOKIE2");
      for (SerializableCookie cookie : entry.getValue()) {
        GLog.t(getClass(), "IMPORT COOKIE3");
        delegate.add(entry.getKey(), cookie.toHttpCookie());
      }
    }
  }
  
  private Map<URI, List<SerializableCookie>> exportFromDelegate() {
    Map<URI, List<SerializableCookie>> cookieMap = new HashMap<URI, List<SerializableCookie>>();
    for (URI uri : delegate.getURIs()) {
      cookieMap.put(uri, SerializableCookie.from(delegate.get(uri)));
    }
    return cookieMap;
  }

  private void serializeCookieMap() {
//    if (true) {
//      throw new RuntimeException("SERIALIZE COOKIE");
//    }
    try {
      GLog.t(getClass(), "SERIALIZE COOKIE1");
      FileOutputStream fileOut = Ui.context().openFileOutput(COOKIE_FILE, Context.MODE_PRIVATE);
      GLog.t(getClass(), "SERIALIZE COOKIE2");
      ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
      GLog.t(getClass(), "SERIALIZE COOKIE3");
      objectOut.writeObject(exportFromDelegate());
      objectOut.close();
    }
    catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
