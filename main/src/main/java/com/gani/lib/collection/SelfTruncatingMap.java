package com.gani.lib.collection;

import java.util.LinkedHashMap;

public class SelfTruncatingMap<K, V> extends LinkedHashMap<K, V> {
  private static final long serialVersionUID = 1L;
  private int maxCapacity;
  
  public SelfTruncatingMap(int maxCapacity) {
    super(maxCapacity);
    this.maxCapacity = maxCapacity;
  }

  int getMaxCapacity() {
    return maxCapacity;
  }

  @Override
  protected boolean removeEldestEntry(Entry<K, V> eldest) {
    return size() > maxCapacity;
  }
}
