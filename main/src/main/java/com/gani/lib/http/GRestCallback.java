package com.gani.lib.http;

import android.content.Context;
import android.os.AsyncTask;

import com.gani.lib.dialog.GDialogProgress;
import com.gani.lib.json.GJsonObject;
import com.gani.lib.logging.GLog;
import com.gani.lib.screen.GFragment;
import com.gani.lib.ui.ProgressIndicator;
import com.gani.lib.ui.alert.ToastUtils;

import org.json.JSONException;


public abstract class GRestCallback<HR extends GHttpResponse, RR extends GRestResponse, HE extends GHttpError> implements GHttpCallback<HR, HE> {
  private Context context;
  private ProgressIndicator indicator;
  private boolean async;

  public GRestCallback(Context context, ProgressIndicator indicator) {
    this.context = context;
    this.indicator = indicator;
    this.async = false;
  }

  // To be used by child.
  protected final Context getContext() {
    return context;
  }

  public GRestCallback(GFragment fragment) {
    this(fragment.getContext(), fragment);
  }

  public GRestCallback(GDialogProgress dialog) {
    this(dialog, dialog);
  }

  public GRestCallback async(boolean value) {
    this.async = value;
    return this;
  }

//  protected abstract void onJsonSuccess(RR r) throws JSONException;
//
//  @Override
//  public final void onHttpSuccess(HR response) {
//    final RR r = (RR) response.asRestResponse();
//    new AsyncTask<Void, Void, Exception>() {
//      @Override
//      protected Exception doInBackground(Void... params) {
//        try {
//          onRestSuccess(r);
//        } catch (JSONException e) {
//          return e;
//        }
//
//        return null;
//      }
//
//      @Override
//      protected void onPostExecute(Exception e) {
//        if (e instanceof JSONException) {
//          GHttp.instance().alertHelper().alertJsonError(context, r, (JSONException) e);
//        }
//
//        doFinally();
//      }
//    }.execute();
//
////    final RR r = (RR) response.asRestResponse();
////    try {
////      GLog.t(getClass(), "onHttpSuccess1");
////      if (r.isAllOk()) {
////        GLog.t(getClass(), "onHttpSuccess1");
////
////        new AsyncTask<Void, Void, Exception>() {
////          @Override
////          protected Exception doInBackground(Void... params) {
////            try {
////              onRestSuccess(r);
////            } catch (JSONException e) {
////              return e;
////            }
////
////            return null;
////          }
////
////          @Override
////          protected void onPostExecute(Exception e) {
////            if (e instanceof JSONException) {
////              GHttp.instance().alertHelper().alertJsonError(context, r, (JSONException) e);
////            }
////
////            doFinally();
////          }
////        }.execute();
////
////        onJsonSuccess(r);
////      } else {
////        GLog.t(getClass(), "ON ERROR");
////        onError();
////        doFinally();
////        GHttp.instance().alertHelper().alertCommonError(context, response);
////      }
////    } catch (JSONException e) {
////      onError();
////      doFinally();
////      GHttp.instance().alertHelper().alertJsonError(context, r, e);
////    }
//  }


  @Override
  public final void onHttpResponse(HR response) {
    final RR r = (RR) response.asRestResponse();

    if (this.async) {
      new AsyncTask<Void, Void, Exception>() {
        @Override
        protected Exception doInBackground(Void... params) {
          try {
            onRestResponse(r);
          } catch (JSONException e) {
            return e;
          }

          return null;
        }

        @Override
        protected void onPostExecute(Exception e) {
          if (e instanceof JSONException) {
  //          GHttp.instance().alertHelper().alertJsonError(context, r, (JSONException) e);
            onJsonError(r, (JSONException) e);
          }

          doFinally();
        }
//      }.execute();

      }.executeOnExecutor(AsyncHttpTask.THREAD_POOL_EXECUTOR);
    }
    else {
      try {
        onRestResponse(r);
      } catch (JSONException e) {
        onJsonError(r, e);
      }
      finally {
        doFinally();
      }
    }
  }

  protected void onJsonError(RR response, JSONException e) {
    GLog.e(getClass(), "Failed parsing JSON result", e);
  }

//  @Override
//  public final void onHttpFailure(HE error) {
//    GLog.t(getClass(), "onHttpFailure1");
//
////    onError();
////    doFinally();
////    error.handle(context);
//  }

  protected final void onBeforeHttp() {
    indicator.showProgress();
  }

  // NOTE: Execute on background thread. Use UI-safe classes such as ToastUtils.
  protected void onRestResponse(RR r) throws JSONException {
    displayMessage(r);
  }

  public static boolean displayMessage(GRestResponse r) {
    try {
      GJsonObject restData = r.getResult();
      String message = restData.getNullableString("message");
      if (message != null) {
        ToastUtils.showNormal(message);
        return true;
      }
    } catch (JSONException e) {
      // Will be handled later.
    }
    return false;
  }

//    protected abstract void onJsonParsingError(RR r) throws JSONException;

  protected void doFinally() {
    indicator.hideProgress();
  }

//  protected void onError() {
//    // To be overridden.
//  }


//  public static abstract class Default extends GRestCallback<GHttpResponse, GRestResponse, GHttpError> {
//    public Default(Context context, ProgressIndicator indicator) {
//      super(context, indicator);
//    }
//
//    public Default(GFragment fragment) {
//      super(fragment);
//    }
//
//    //    @Override
////    public void handleDefault(Context context) {
////      Toast.makeText(context, getMessage(), Toast.LENGTH_LONG).show();
////    }
//
//
////    @Override
////    protected void onRestResponse(GRestResponse r) throws JSONException {
////      displayMessage(r);
////    }
////
////    protected boolean displayMessage(GRestResponse r) {
////      try {
////        GJsonObject restData = r.getResult();
////        String message = restData.getNullableString("message");
////        if (message != null) {
////          ToastUtils.showNormal(message);
////          return true;
////        }
////      }
////      catch (JSONException e) {
////        // Will be handled later.
////      }
////      return false;
////    }
//
//  }
}
