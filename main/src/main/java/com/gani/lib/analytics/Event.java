package com.gani.lib.analytics;

import java.util.HashMap;
import java.util.Map;

public class Event {
  
  private String id;
  private Map<String, String> params = new HashMap<String, String>();
  
  public Event(String id) {
    this.id = id;
  }
  
  public String getId() {
    return id;
  }

  public Map<String, String> getParams() {
    return params;
  }
  
  public Event withParam(String name, String value) {
    params.put(name, value);
    return this;
  }
  
  public Event withParam(String name, Long value) {
    params.put(name, value.toString());
    return this;
  }
  
//  public Event withShareChannelParams(Collection<String> actualChannelParams) {
//    Collection<String> allAvailableChannelParams = Arrays.asList(ParamValues.SHARE_FACEBOOK, ParamValues.SHARE_TWITTER);
//    for (String channelParam : allAvailableChannelParams) {
//      withParam(channelParam, "" + actualChannelParams.contains(channelParam));
//    }
//    return this;
//  }
}
