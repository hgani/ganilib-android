package com.gani.lib.io;

import com.gani.lib.logging.GLog;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.HttpCookie;
import java.util.ArrayList;
import java.util.List;

class SerializableCookie implements Serializable {

  private static final long serialVersionUID = 1L;

  private transient HttpCookie httpCookie;
  
  SerializableCookie(HttpCookie httpCookie) {
    this.httpCookie = httpCookie;
  }

  HttpCookie toHttpCookie() {
    return httpCookie;
  }

  boolean hasExpired() {
    return httpCookie.hasExpired();
  }
  
  @Override
  public int hashCode() {
    return httpCookie.hashCode();
  }

  @Override
  public boolean equals(Object o) {
    if (o instanceof SerializableCookie) {
      SerializableCookie that = (SerializableCookie) o;
      return this.httpCookie.equals(that.httpCookie);
    }
    
    return false;
  }

  @Override
  public String toString() {
    return httpCookie.toString();
  }

  private synchronized void writeObject(ObjectOutputStream s) throws IOException {
    GLog.t(getClass(), "WRITE COOKIE: " + httpCookie);

    s.defaultWriteObject();
    s.writeObject(httpCookie.getName());
    s.writeObject(httpCookie.getValue());

    s.writeObject(httpCookie.getComment());
    s.writeObject(httpCookie.getCommentURL());
    s.writeBoolean(httpCookie.getDiscard());
    s.writeObject(httpCookie.getDomain());
    s.writeLong(httpCookie.getMaxAge());
    s.writeObject(httpCookie.getPath());
    s.writeObject(httpCookie.getPortlist());
    s.writeBoolean(httpCookie.getSecure());
    s.writeInt(httpCookie.getVersion());
  }
  
  private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
    s.defaultReadObject();

    httpCookie = new HttpCookie((String) s.readObject(), (String) s.readObject());

    GLog.t(getClass(), "READ COOKIE: " + httpCookie);

    httpCookie.setComment((String) s.readObject());
    httpCookie.setCommentURL((String) s.readObject());
    httpCookie.setDiscard(s.readBoolean());
    httpCookie.setDomain((String) s.readObject());
    httpCookie.setMaxAge(s.readLong());
    httpCookie.setPath((String) s.readObject());
    httpCookie.setPortlist((String) s.readObject());
    httpCookie.setSecure(s.readBoolean());
    httpCookie.setVersion(s.readInt());
  }
  
  public static List<SerializableCookie> from(List<HttpCookie> cookies) {
    List<SerializableCookie> result = new ArrayList<SerializableCookie>();
    for (HttpCookie cookie : cookies) {
      result.add(new SerializableCookie(cookie));
    }
    return result;
  }
}
