package com.gani.lib.analytics;

public class TrackingSpec {
  
  public static final TrackingSpec DO_NOTHING = new TrackingSpec(false, null);

  private boolean shouldIncrementScreenViewCount = false;
  private Event event = null;
  
  public TrackingSpec(boolean shouldIncrementScreenViewCount, Event event) {
    this.shouldIncrementScreenViewCount = shouldIncrementScreenViewCount;
    this.event = event;
  }
  
  public boolean shouldIncrementScreenViewCount() {
    return shouldIncrementScreenViewCount;
  }
  
  public Event event() {
    return event;
  }
  
}
