package com.gani.lib.http;

import android.content.Context;

import com.gani.lib.logging.GLog;

import org.json.JSONException;

// TODO: Merge these methods into GHttpError and need make sure GHttpError.markForJson() gets called.
public abstract class GHttpAlert<HR extends GHttpResponse, RR extends GRestResponse> {
  // To be overridden
  public void reportCodeError(HR r) throws JSONException {
    // Do nothing by default
  }

  // To be overridden
  public void reportJsonError(RR r, JSONException e) {
    // Do nothing by default
  }

  // To be overridden
  public String messageForJsonError(String url, JSONException ex) {
    return "Failed communicating with server";
  }

//  // To be overridden
//  public void alertJsonError(Context c, RR r, JSONException e) {
//    GLog.e(getClass(), "TODO: Override alertJsonError()", e);
//  }

  // TODO: rename to alertAppSpecificError
  // To be overridden
  public void alertCommonError(Context c, HR r) throws JSONException {
    GLog.e(getClass(), "TODO: Override alertCommonError()");
    // Example: Alert.display(c, r.getError().getMessage());
  }

  // To be overridden
  public void alertFormParsingError(Context c, HR r) {
    GLog.e(getClass(), "TODO: Override alertFormParsingError()");
    // Example: Alert.display(c, r.getError().getMessage());
  }
}
