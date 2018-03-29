package com.gani.lib.http;

public abstract class HttpHook<T extends GHttpResponse> {
  public abstract T processResponse(T response);
  public abstract void launchAsyncTask(AsyncHttpTask task);

  public static final HttpHook DUMMY = new HttpHook() {
    @Override
    public GHttpResponse processResponse(GHttpResponse response) {
      return response;  // Do nothing, this is the default hook.
    }

    @Override
    public void launchAsyncTask(AsyncHttpTask task) {
      task.executeIfNotCanceled();
    }
  };


//  DUMMY() {
//    @Override
//    public GHttpResponse processResponse(GHttpResponse response) {
//      return response;  // Do nothing, this is the default hook.
//    }
//
//    @Override
//    void launchAsyncTask(AsyncHttpTask task) {
//      task.executeIfNotCanceled();
//    }
//  },
}

//public enum HttpHook {
//  DUMMY() {
//    @Override
//    public GHttpResponse processResponse(GHttpResponse response) {
//      return response;  // Do nothing, this is the default hook.
//    }
//
//    @Override
//    void launchAsyncTask(AsyncHttpTask task) {
//      task.executeIfNotCanceled();
//    }
//  },
//  AUTH_REST() {
//    private GHttpResponse reauthenticate(GHttpResponse origResponse) {
//      GHttp.instance().reauthenticate(origResponse);
//      return origResponse;
//
////      ErrorReporter.getInstance().log("Except in test mode, " +
////          "reauthentication should not be happening: " + origResponse.getUrl());
////      final Credential credential = Credential.getInstance();
////      if (credential.exists()) {
////        GLog.d(getClass(), "Authenticating and let the server replay the "
////            + "previously requested resource/operation ...");
//////        try {
//////          return new GHttpSyncPost(AsyncUnauthedRequests.AUTH, GParams.credential(),
//////              HttpHook.UNAUTH_REST, HttpMethod.POST).execute();
////
////        // Need to be synchronous so we can return result back to original caller
////        GHttpSyncPost request = new GHttpSyncPost(AsyncUnauthedRequests.AUTH, GParams.credential().toImmutable(), HttpHook.UNAUTH_REST, HttpUtils.HttpMethod.POST);
////        try {
////          GHttpResponse response = request.execute();
////          if (response.asRestResponse().isAllOk()) {
////            Credential.save(credential.getId(), credential.getEmail(), credential.getPassword(),
////                response.asRestResponse().getJsonResult().getString(ParamKeys.SIGNIN_AUTHENTICITY_TOKEN));
////          }
////          else {
////            origResponse.getError().markForAuth("Wrong credentials");
////          }
////        } catch (HttpSync.HttpSyncException e) {
////          origResponse.getError().markForAuth("Failed reauthenticating: " + e.getMessage());
////        } catch (HttpSync.HttpCanceledException e) {
////          origResponse.getError().markForAuth("Failed reauthenticating: " + e.getMessage());
////        } catch (JSONException e) {
////          origResponse.getError().markForAuth("Failed reauthenticating: " + e.getMessage());
////        }
////      }
////      else {
////        origResponse.getError().markForAuth("Credential does not exist");
////      }
////      return origResponse;
//    }
//
//    @Override
//    public GHttpResponse processResponse(GHttpResponse response) {
//      if (!response.getError().hasError()) { // If there's an error, we may not have JSON
//        try {
//          GRestResponse restResponse = response.asRestResponse();
//          if (restResponse.isAuthenticationRequired()) {
//            return reauthenticate(response);
//          } else if (restResponse.isAuthenticationRejected()) {
//            response.getError().markForAuth("Authentication rejected");
//          }
//        } catch (JSONException e) {
//          response.getError().markForJson(e);
//        }
//      }
//      return response;
//    }
//
//    @Override
//    void launchAsyncTask(AsyncHttpTask task) {
//      AsyncHttpQueuer.getInstance().queueAsync(task);
//    }
//  },
//  UNAUTH_REST() {
//    @Override
//    public GHttpResponse processResponse(GHttpResponse response) {
//      if (!response.getError().hasError()) { // If there's an error, we may not have JSON
//        try {
//          if (response.asRestResponse().isAuthenticationRequired()) {
//            response.getError().markForAuth("Used non-authenticated connection");
//          }
//        }
//        catch (JSONException e) {
//          response.getError().markForJson(e);
//        }
//      }
//      return response;
//    }
//
//    @Override
//    void launchAsyncTask(AsyncHttpTask task) {
//      task.executeIfNotCanceled();
//    }
//  };
//
//  abstract GHttpResponse processResponse(GHttpResponse response);
//  abstract void launchAsyncTask(AsyncHttpTask task);
//}
