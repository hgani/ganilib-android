package com.gani.lib.http;






public class HttpAsyncGet implements HttpAsync {
  private GetDelegate delegate;
  private AsyncHttpTask asyncTask;
//
//  public HttpAsyncGet(String nakedUrl, GParams params, HttpHook hook, GHttpCallback callback) {
//    this.delegate = new GetDelegate(nakedUrl, params, hook);
//    this.asyncTask = new AsyncHttpTask(callback, delegate);
//  }

  public HttpAsyncGet(String nakedUrl, GImmutableParams params, HttpHook hook, GHttpCallback callback) {
    this.delegate = new GetDelegate(nakedUrl, params, hook);
    this.asyncTask = new AsyncHttpTask(callback, delegate);
  }

  @Override
  public String getUrl() {
    return delegate.getUrl();
  }

  @Override
  public HttpAsyncGet execute() {
    delegate.launch(asyncTask);
    return this;
  }

  @Override
  public void cancel() {
    delegate.cancel();
    asyncTask.safeCancel();
  }
  
  public Object getParam(String key) {
    return delegate.getParam(key);
  }
}
