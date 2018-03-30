package com.gani.lib.http;

public class HttpAsyncPost implements HttpAsync {
  private PostDelegate delegate;
  private AsyncHttpTask asyncTask;
 
  public HttpAsyncPost(String nakedUrl, GImmutableParams params, HttpHook hook, HttpMethod method, GHttpCallback callback) {
    this.delegate = new PostDelegate(nakedUrl, params, hook, method);
    this.asyncTask = new AsyncHttpTask(callback, delegate);
  }

  @Override
  public String getUrl() {
    return delegate.getUrl();
  }

  @Override
  public HttpAsyncPost execute() {
    delegate.launch(asyncTask);
    return this;
  }

  @Override
  public void cancel() {
    delegate.cancel();
    asyncTask.safeCancel();
  }
}
