package com.gani.lib.analytics;

import android.content.Context;
import android.os.Bundle;

import java.util.HashSet;
import java.util.Set;

/**
 * Our createCurrent tracking convention:
 * <ul>
 *   <li>Count screen views of content consumption screens</li>
 *   <li>Track names of important screens</li>
 *   <li>Track more data (clicks, parameters) around conversion, acquisition, retention</li>
 * </ul>
 */
public class Tracker {
  
  private boolean didReorient = false;
  private boolean didCountScreenView = false;
  private Set<String> trackedEventIds = new HashSet<String>();
  
  public Tracker(Bundle savedInstanceState) {
    didReorient = (savedInstanceState != null);
  }

  public Tracker start(Context context) {
//    FlurryAgent.onStartSession(context, Build.INSTANCE.flurryApiKey());
    return this;
  }
  
  public void stop(Context context) {
//    FlurryAgent.onEndSession(context);
  }
  
  public Tracker track(TrackingSpec trackingSpec) {
    if (trackingSpec.shouldIncrementScreenViewCount()) {
      incrementScreenViewCount();
    }
    
    if (trackingSpec.event() != null) {
      track(trackingSpec.event());
    }
    
    return this;
  }
  
  public Tracker track(Event event) {
//    FlurryAgent.logEvent(event.getId(), event.getParams());
    return this;
  }
  
  public Tracker trackIgnoringRepeats(Event event) {
    String eventId = event.getId();
    if (!didReorient) {
//      FlurryAgent.logEvent(eventId, event.getParams());
      trackedEventIds.add(eventId);
    }
    return this;
  }
  
  private Tracker incrementScreenViewCount() {
    if (!didReorient && !didCountScreenView) { // Ignore skewing repeats
//      FlurryAgent.onPageView();
      didCountScreenView = true;
    }
    return this;
  }
}
