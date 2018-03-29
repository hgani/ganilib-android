package com.gani.lib.http;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;

final class PostDelegate extends HttpDelegate {
  private static final long serialVersionUID = 1L;

  private HttpMethod method;
  private GImmutableParams params;

  PostDelegate(String nakedUrl, GImmutableParams params, HttpHook hook, HttpMethod method) {
    super(nakedUrl, hook);
    this.method = method;
    this.params = GImmutableParams.fromNullable(params);
  }
  
  @Override
  protected String getMethod() {
    return method.name();
  }
  
  @Override
  protected HttpURLConnection makeConnection() throws IOException {
    HttpURLConnection connection = GHttp.instance().openConnection(getFullUrl(), params, method);
    connection.setDoOutput(true);

    byte[] data = GHttp.instance().processParams(params, method)
        .toMutable().put("_method", getMethod()).toImmutable()
        .asQueryString().getBytes("UTF-8");
    connection.getOutputStream().write(data);
    return connection;
  }

  @Override
  protected String getFullUrl() {
    return getUrl();
  }
}
