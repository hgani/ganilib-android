package com.gani.lib.http;

import com.gani.lib.json.GJsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class GRestResponse implements Serializable {
  private static final long serialVersionUID = 1L;

  private String jsonString;
  private transient JSONObject jsonResult;
  private GHttpResponse httpResponse;

  protected GRestResponse(String jsonString, GHttpResponse httpResponse) {
    this.jsonString = jsonString;
    this.httpResponse = httpResponse;
  }

  public GHttpResponse getHttpResponse() {
    return httpResponse;
  }

  public String getUrl() {
    return httpResponse.getUrl();
  }
  
  protected JSONObject getJsonResult() throws JSONException {
    if (this.jsonResult == null) {
      if (jsonString != null) {
        this.jsonResult = new JSONObject(jsonString);
      }
      else {
        throw new JSONException("Null body");  // Unify NPE with JSONException
      }
    }
    return this.jsonResult;
  }

  public GJsonObject getResult() throws JSONException {
    return new GJsonObject.Default(getJsonResult());
  }

//  public boolean isAllOk() throws JSONException {
//    return true;
//  }
  
  public String getJsonString() {
    return jsonString;
  }

  @Override
  public String toString() {
    return jsonString;
  }
}