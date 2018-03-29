package com.gani.lib.http;


public class HttpSyncGet implements HttpSync {
  private GetDelegate delegate;
 
  public HttpSyncGet(String nakedUrl, GImmutableParams params, HttpHook hook) {
    this.delegate = new GetDelegate(nakedUrl, params, hook);
  }

  @Override
  public String getUrl() {
    return delegate.getUrl();
  }

  @Override
  public GHttpResponse execute() throws HttpSyncException, HttpCanceledException {
    GHttpResponse response = delegate.launchIfNotCanceled();
    if (response.getError().hasError()) {
      throw new HttpSyncException(response.getError());
    }
    return response;
  }

  public void execute(GHttpCallback callback) {
    try {
      callback.onHttpResponse(execute());
//      callback.onHttpSuccess(execute());
    }
    catch (HttpSyncException e) {
      callback.onHttpResponse(e.getError().getResponse());
//      callback.onHttpFailure(e.getError());
    }
    catch (HttpCanceledException e) {
      // Be silent for now.
    }
  }

  @Override
  public void cancel() {
    delegate.cancel();
  }
}
