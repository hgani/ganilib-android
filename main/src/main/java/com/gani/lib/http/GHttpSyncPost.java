package com.gani.lib.http;

public class GHttpSyncPost implements HttpSync {
  private PostDelegate delegate;
 
  public GHttpSyncPost(String nakedUrl, GImmutableParams params, HttpHook hook, HttpMethod method) {
    this.delegate = new PostDelegate(nakedUrl, params, hook, method);
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
  
  @Override
  public void cancel() {
    delegate.cancel();
  }
}
