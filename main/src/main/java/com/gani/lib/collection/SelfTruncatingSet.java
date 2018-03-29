package com.gani.lib.collection;

import com.gani.lib.json.GJson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.AbstractSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;

public class SelfTruncatingSet<E> extends AbstractSet<E> implements Serializable {
  private static final long serialVersionUID = 1L;
  private static final Object VALUE = new Object();

  private SelfTruncatingMap<E, Object> backingMap;

  public SelfTruncatingSet(int maxCapacity) {
    this.backingMap = new SelfTruncatingMap<E, Object>(maxCapacity);
  }

  private int getMaxCapacity() {
    return backingMap.getMaxCapacity();
  }


  @Override
  public boolean add(E object) {
    return backingMap.put(object, VALUE) == VALUE;
  }

  @Override
  public void clear() {
    backingMap.clear();
  }

  @Override
  @SuppressWarnings("unchecked")
  public Object clone() {
    // If one day we need to support this, see HashSet implementation.
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean contains(Object object) {
      return backingMap.containsKey(object);
  }

  @Override
  public boolean isEmpty() {
      return backingMap.isEmpty();
  }

  @Override
  public Iterator<E> iterator() {
      return backingMap.keySet().iterator();
  }

  @Override
  public boolean remove(Object object) {
      return backingMap.remove(object) != null;
  }

  @Override
  public int size() {
      return backingMap.size();
  }

  private void writeObject(ObjectOutputStream stream) throws IOException {
    // If one day we need to support this, see HashSet implementation.
    throw new UnsupportedOperationException();
  }

  private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
    // If one day we need to support this, see HashSet implementation.
    throw new UnsupportedOperationException();
  }

  HashMap<E, HashSet<E>> createBackingMap(int capacity, float loadFactor) {
    return new HashMap<E, HashSet<E>>(capacity, loadFactor);
  }



  public static class GsonSerializer
      implements JsonSerializer<SelfTruncatingSet<?>>, JsonDeserializer<SelfTruncatingSet<?>> {
    @Override
    public JsonElement serialize(SelfTruncatingSet<?> src, Type typeOfSrc, JsonSerializationContext context) {
      JsonObject object = new JsonObject();
      object.addProperty("max_capacity", src.getMaxCapacity());
      object.addProperty("delegate", GJson.defaultGson().toJson(src));
      return object;
    }

    @Override
    public SelfTruncatingSet<?> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
      JsonObject jsonObject = json.getAsJsonObject();
      int maxCapacity = jsonObject.get("max_capacity").getAsInt();
      String delegate = jsonObject.get("delegate").getAsString();

      SelfTruncatingSet<Object> set = new SelfTruncatingSet<>(maxCapacity);
      // Use typeOfT so that the generic type is still maintained, e.g. LinkedHashSet<Long>
      LinkedHashSet<?> entries = GJson.defaultGson().fromJson(delegate, typeOfT);
      for (Object entry : entries) {
        set.add(entry);
      }
      return set;
    }
  }
}
