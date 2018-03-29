package com.gani.lib.ui.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.gani.lib.http.GHttp;
import com.gani.lib.http.GImmutableParams;
import com.gani.lib.logging.GLog;
import com.gani.lib.screen.GFragment;
import com.gani.lib.ui.ProgressIndicator;

import static android.R.attr.fragment;
import static org.jsoup.nodes.Document.OutputSettings.Syntax.html;

public class GWebView extends WebView {
  private ViewHelper helper;
  private ProgressIndicator indicator;

  public GWebView(Context context, ProgressIndicator progress) {
    super(context);
    init();
    this.indicator = progress;
  }

  public GWebView(Context context, AttributeSet attrs) {
    super(context, attrs);
    init();
  }
  
  private void init() {
    this.helper = new ViewHelper(this);

    setWebViewClient(new ProgressAwareWebViewClient());

    // Mimic turbolinks-android's WebView as much as possible.
    WebSettings webSettings = getSettings();
//    webSettings.setUserAgentString(ConnectionPreparator.userAgent());
    webSettings.setJavaScriptEnabled(true);
    webSettings.setDomStorageEnabled(true);
    webSettings.setDatabaseEnabled(true);

    // http://stackoverflow.com/questions/9055347/fitting-webpage-contents-inside-a-webview-android
    webSettings.setLoadWithOverviewMode(true);
    webSettings.setUseWideViewPort(true);

    // To enable js alert and confirm dialog.
    setWebChromeClient(new WebChromeClient());
  }

  public GWebView size(Integer width, Integer height) {
    helper.size(width, height);
    return this;
  }

  public GWebView bgColor(int color) {
    setBackgroundColor(color);
    return this;
  }

  public GWebView load(String url, GImmutableParams params) {
    String fullUrl = url + ((params == null) ? "" : "?" + params.asQueryString());
    loadUrl(fullUrl);
    return this;
  }

  public GWebView load(String url) {
    return load(url, null);
  }

  public GWebView loadHtml(String html, String baseUrl) {
    loadDataWithBaseURL(baseUrl, html, "text/html", "utf-8", null);
    return this;
  }

  protected void onPageFinished() {
    // To be overridden
  }

//  public void onClick()


  private class ProgressAwareWebViewClient extends WebViewClient {
    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
      GLog.i(getClass(), "onPageStarted: " + url);
      super.onPageStarted(view, url, favicon);

      indicator.showProgress();
    }

    @Override
    public void onPageFinished(WebView view, String url) {
      GLog.i(getClass(), "onPageFinished: " + url);
      super.onPageFinished(view, url);

      indicator.hideProgress();
      GWebView.this.onPageFinished();
    }

//    // This wasn't working on Android 5.1.1 last time it was tested, but it doesn't matter now that we use Turbolinks on newer OSes.
//    @Override
//    public boolean shouldOverrideUrlLoading(WebView  view, String  url){
//      Uri uri = Uri.parse(url);
//      String host = uri.getHost();
//      if (host != null) {  // Can be null for embedded base64 image.
//        if (getContext() instanceof RichActivity) {
//          Turbolinks.handleVisit((RichActivity) getContext(), uri, WebVisit.Action.ADVANCE);
//        }
//          return true;
//      }
//      return false;
//    }
  }
}
