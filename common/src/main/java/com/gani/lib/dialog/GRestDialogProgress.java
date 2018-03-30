package com.gani.lib.dialog;

import android.content.Intent;
import android.os.Bundle;

import com.gani.lib.http.GImmutableParams;
import com.gani.lib.http.GRestCallback;
import com.gani.lib.http.GRestResponse;
import com.gani.lib.http.HttpAsync;
import com.gani.lib.http.HttpMethod;

import org.json.JSONException;

import java.io.Serializable;

public class GRestDialogProgress extends GDialogProgress {
  private static final String ARG_METHOD = "method";
  private static final String ARG_URL = "url";
  private static final String ARG_PARAMS = "params";
  private static final String ARG_CALLBACK = "callback";

  private HttpAsync httpRequest;

  public static Intent intent(HttpMethod method, String url, GImmutableParams params, Callback callback) {
    return intentBuilder(GRestDialogProgress.class)
        .withArg(ARG_METHOD, method)
        .withArg(ARG_URL, url)
        .withArg(ARG_PARAMS, params)
        .withArg(ARG_CALLBACK, callback)
        .getIntent();
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    HttpMethod method = (HttpMethod) args().getSerializable(ARG_METHOD);
    String url = args().getString(ARG_URL);
    GImmutableParams params = args().getParams(ARG_PARAMS);
    final Callback callback = (Callback) args().getSerializable(ARG_CALLBACK);

    httpRequest = method.async(url, params, new GRestCallback(this) {
//      @Override
//      protected void onJsonSuccess(GRestResponse r) throws JSONException {
//        callback.onJsonSuccess(GRestDialogProgress.this, r);
//      }

      @Override
      protected void onRestResponse(GRestResponse r) throws JSONException {
        callback.onRestResponse(GRestDialogProgress.this, r);
      }
    }).execute();
  }

  @Override
  protected void onCancel() {
    httpRequest.cancel();
  }



  public static abstract class Callback implements Serializable {
    // To be overridden
    protected void onRestResponse(GRestDialogProgress dialogProgress, GRestResponse r) throws JSONException {
      GRestCallback.displayMessage(r);
    }
  }
}
