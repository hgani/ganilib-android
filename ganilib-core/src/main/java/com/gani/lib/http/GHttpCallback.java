package com.gani.lib.http;

import org.json.JSONException;

import java.io.Serializable;


public interface GHttpCallback<HR extends GHttpResponse, HE extends GHttpError> extends Serializable {
  void onHttpResponse(HR response);

//  public void onHttpSuccess(HR response);
//  public void onHttpFailure(HE error);



//  // TODO: Need retry? Especially for cognito.
//  abstract class SyncSilentRest<RR extends GRestResponse> implements GHttpCallback {
//    public final void onHttpSuccess(GHttpResponse response) {
//      final RR r = (RR) response.asRestResponse();
//      try {
//        if (r.isAllOk()) {
//          onRestSuccess(r);
//        } else {
//          onError();
//          GHttp.instance().alertHelper().reportCodeError(response);
//        }
//      } catch (JSONException e) {
//        onError();
////        HttpUtils.reportJsonError(r, e);
//        GHttp.instance().alertHelper().reportJsonError(r, e);
//      }
//    }
//
//    public final void onHttpFailure(GHttpError error) {
//      onError();
//    }
//
//    // NOTE: Execute on background thread. Use UI-safe classes such as ToastUtils.
//    protected abstract void onRestSuccess(RR r) throws JSONException;
//
//    protected void onError() {
//      // To be overridden.
//    }
//  }
}
