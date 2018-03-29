package com.gani.lib.http;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;


class GetDelegate extends HttpDelegate {
  private static final long serialVersionUID = 1L;

  private GImmutableParams params;
  private HttpMethod method;

  GetDelegate(String nakedUrl, GImmutableParams params, HttpHook hook) {
    super(nakedUrl, hook);

    this.params = GImmutableParams.fromNullable(params);
    this.method = HttpMethod.GET;
  }

  Object getParam(String key) {
    return params.get(key);
  }

  @Override
  protected String getMethod() {
    return method.name();
  }
  
  @Override
  protected HttpURLConnection makeConnection() throws MalformedURLException, IOException {
    return GHttp.instance().openConnection(getFullUrl(), params, method);
  }

  @Override
  protected String getFullUrl() {
    String url = getUrl();

    GImmutableParams finalParams = GHttp.instance().processParams(params, method);

    if (finalParams.size() <= 0) {
      return url;
    }

    String separator = (url.contains("?")) ? "&" : "?";

    return url + separator + finalParams.asQueryString();
  }
}
