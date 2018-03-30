package com.gani.lib.http;

import android.content.Context;
import android.webkit.WebView;

import com.gani.lib.io.PersistentCookieStore;
import com.gani.lib.io.WebkitCookieManagerProxy;
import com.gani.lib.ui.Ui;

import org.json.JSONException;

import java.io.IOException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpURLConnection;
import java.net.URL;

public abstract class GHttp<P extends GImmutableParams> {
  private static GHttp instance;

  public static void register(GHttp i) {
    instance = i;
  }

  public static GHttp instance() {
    return instance;
  }

  protected GHttp() {
    initPermanentCookieHandler();
  }

  private static void initPermanentCookieHandler() {
    android.webkit.CookieSyncManager.createInstance(Ui.context());

    // Use ACCEPT_ALL instead of ACCEPT_ORIGINAL_SERVER so that it is cross-subdomain.
    CookieManager defaultManager = new CookieManager(null, CookiePolicy.ACCEPT_ALL);
    PersistentCookieStore cookieStore = new PersistentCookieStore(defaultManager.getCookieStore());

    // See http://stackoverflow.com/questions/18057624/two-way-sync-for-cookies-between-httpurlconnection-java-net-cookiemanager-and
    CookieHandler.setDefault(new WebkitCookieManagerProxy(cookieStore, java.net.CookiePolicy.ACCEPT_ALL));
  }

  protected abstract String networkErrorMessage();

  public HttpURLConnection openConnection(String url, P params, HttpMethod method) throws IOException {
    HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
    prepareConnection(connection, params, method);
    return connection;
  }

  protected void prepareConnection(HttpURLConnection connection, P params, HttpMethod method) {
    // To be overridden
  }

  protected GImmutableParams processParams(P params, HttpMethod method) {
    // To be overridden
    return params;
  }

  // To be overridden
  protected GHttpResponse createHttpResponse(String url) {
    return new GHttpResponse(url);
  }

  // To be overridden
  public GHttpAlert alertHelper() {
    return new GHttpAlert() {
      @Override
      public void reportCodeError(GHttpResponse r) throws JSONException {

      }

      @Override
      public void reportJsonError(GRestResponse r, JSONException e) {

      }

      @Override
      public String messageForJsonError(String url, JSONException ex) {
        return null;
      }

//      @Override
//      public void alertJsonError(Context c, GRestResponse r, JSONException e) {
//
//      }

      @Override
      public void alertCommonError(Context context, GHttpResponse r) throws JSONException {

      }
    };
  }

  public abstract String baseUrl();

  public void prepareWebView(WebView webView) {
    // To be overridden.
  }
}
