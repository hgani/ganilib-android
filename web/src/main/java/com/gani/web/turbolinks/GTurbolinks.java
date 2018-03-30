package com.gani.web.turbolinks;

import android.net.Uri;
import android.os.Build;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.basecamp.turbolinks.InlineTurbolinksView;
import com.basecamp.turbolinks.TurbolinksAdapter;
import com.basecamp.turbolinks.TurbolinksView;
import com.gani.lib.http.GHttp;
import com.gani.lib.logging.GLog;
import com.gani.lib.ui.Ui;
import com.basecamp.turbolinks.TurbolinksSessionWrapper;
import com.gani.web.WebVisit;

import java.net.URI;

public abstract class GTurbolinks {
  public static final String PENDING_TITLE = "";

  private GTurbolinksSupportActivity activity;
  private TurbolinksView view;
  private String url;
  //  private TurbolinksSession session;
  private TurbolinksSessionWrapper session;
  private TurbolinksAdapter adapter;

  public GTurbolinks(GTurbolinksSupportActivity activity, TurbolinksView view, String path) {
    URI uri = URI.create(path);
    if (uri.getHost() != null) {
      throw new IllegalArgumentException("Relative path expected. Got: " + path);
    }

    this.activity = activity;
    this.view = view;
//    this.url = baseUrl() + path;
    this.url = GHttp.instance().baseUrl() + path;

    // Use separate sessions so we have separate web views thus greatly simplifying their lifecycle management. See InlineTurbolinksView.
    this.session = (view instanceof InlineTurbolinksView) ? wrapContentSession() : matchParentSession();
    this.adapter = createAdapter();

    // Set WRAP_CONTENT height to override the default's MATCH_PARENT
    session.getWebView().setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

    // NOTE: This generally works well but has a side effect of looking slow to load when the page is already cached, because
    // the content will only be shown (height is adjusted) once the content has been reloaded form server.
    // Ideally, we can detect to see if any cached content exists. Only apply this technique if it does *not* exist.
    //
    // Without this, the web view will start off with the same height of the previous content, which will make it jumpy when the previous content is bigger than the currently loading content.
//    session.getWebView().setLayoutParams(new ViewGroup.LayoutParams(
//        ViewGroup.LayoutParams.MATCH_PARENT, 10));;
  }
//
//  protected abstract String baseUrl();

  public void visit() {
    GLog.i(getClass(), "TL visit: " + url);
    session
        .adapter(adapter)
        .activity(activity)
        .view(view)
        .advance(url);
  }

  public void restore() {
    GLog.i(getClass(), "TL restore: " + url);
    // Since the webView is shared between activities, we need to tell GTurbolinks to load the location from the previous activity upon restarting
    session
        .adapter(adapter)
        .activity(activity)
//        .restoreWithCachedSnapshot(true)
        .view(view)
        .restore(url);
//        .advance(url);
  }

  protected String getUrl() {
    return url;
  }

  private static TurbolinksSessionWrapper wrapContentSession;
  private static TurbolinksSessionWrapper matchParentSession;

  private static TurbolinksSessionWrapper matchParentSession() {
    if (matchParentSession == null) {
//      matchParentSession = prepare(TurbolinksSession.getDefault(App.context()));
      matchParentSession = prepare(new TurbolinksSessionWrapper(Ui.context()));
    }
    return matchParentSession;
  }

  private static TurbolinksSessionWrapper wrapContentSession() {
    if (wrapContentSession == null) {
      wrapContentSession = prepare(new TurbolinksSessionWrapper(Ui.context()));
    }
    return wrapContentSession;
  }

  public WebView getWebView() {
    return session.getWebView();
  }

  public static void reset() {
    // This should be more reliable than the solution proposed in https://github.com/turbolinks/turbolinks-android/issues/56
    matchParentSession = null;
    wrapContentSession = null;
  }

  private static TurbolinksSessionWrapper prepare(TurbolinksSessionWrapper session) {
    WebView webView = session.getWebView();
    GHttp.instance().prepareWebView(webView);
    return session;

//    WebSettings settings = session.getWebView().getSettings();
//    settings.setUserAgentString(CvHttp.userAgent());
//    return session;
  }

  public static boolean isSupported() {
    return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT);
  }

//  public static boolean handleVisit(GActivity activity, Uri uri, WebVisit.HtmlFormHandler action) {
//    return new WebVisit(activity, uri, action).handle();
//  }

  protected abstract void handleVisit(GTurbolinksSupportActivity activity, Uri uri, WebVisit.Action action);
  protected abstract void handleError(int errorCode);

  private TurbolinksAdapter createAdapter() {
    return new TurbolinksAdapter() {
      @Override
      public void onPageFinished() {
        GLog.t(getClass(), "TL onPageFinished: " + url);
      }

//      private void handleError(int errorCode) {
//        ToastUtils.showNormal(Ui.str(R.string.error_turbolinks) + " " + Ui.str(R.string.error_report_suffix));
//
//        switch (errorCode) {
//          case 500:  // Likely to happen
//            return;  // Don't need to report the error because the server will notify us.
//          default:  // This includes negative errorCode which is returned by WebViewClient (https://developer.android.com/reference/android/webkit/WebViewClient.html).
////            switch (errorCode) {
////              case 404:  // Likely to happen
////                activity.finish();
////              default:
////                // Let user see the original message. Don't hide the error because we don't know what we don't know.
////            }
//            GLog.t(getClass(), "Failed loading GTurbolinks page: " + url + " -- Error code: " + errorCode);
//            ErrorReporter.getInstance().reportSilently("Failed loading GTurbolinks page: " + url + " -- Error code: " + errorCode);
//        }
//      }

      @Override
      public void onReceivedError(int errorCode) {
        GLog.t(getClass(), "TL onReceivedError");
        handleError(errorCode);
      }

      @Override
      public void pageInvalidated() {
        GLog.t(getClass(), "TL pageInvalidated");
      }

      @Override
      public void requestFailedWithStatusCode(int statusCode) {
        GLog.t(getClass(), "TL requestFailedWithStatusCode");
        handleError(statusCode);
      }

      @Override
      public void visitCompleted() {
        GLog.t(getClass(), "TL visitCompleted: " + url);

        // Put this here because onPageFinished() doesn't get called when redirection is involved.
        if (PENDING_TITLE.equals(activity.getTitle())) {
          String title = getWebView().getTitle();
          Uri uri = Uri.parse(url);
          // visitCompleted() gets called twice. Avoid setting title when it's called the first time because title is set to the URL.
          if (!title.contains(uri.getHost())) {
            activity.setTitle(title);
          }
        }
//        // Execute later to ensure that the content has fully loaded/rendered to reduce jumpiness caused by height changing to 0 (content hasn't rendered) then to the real size (after content is rendered).
//        Ui.run(new Runnable() {
//          @Override
//          public void run() {
//            WebView webView = session.getWebView();
//            // Without this, the default Turbolink's web view can still expand when web content expands, *but* it will not shrink when the content shrinks
//            // Useful for displaying inline web content mixed with native content within the same screen.
//            ViewGroup.LayoutParams params = webView.getLayoutParams();
//            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
//            webView.setLayoutParams(params);
//          }
//        });
      }

      private static final String ACTION_ADVANCE = "advance";

      @Override
      public void visitProposedToLocationWithAction(String location, String action) {
        GLog.t(getClass(), "TL visitProposedToLocation: " + location + " withAction: " + action);

        // NOTE: Our navigation solution is not perfect yet. We still need a "back" as described in: https://github.com/turbolinks/turbolinks/issues/88
        // The "back" visit is similar to "advance" with the only difference that it should close the current screen.
        if (ACTION_ADVANCE.equals(action)) {
          handleVisit(activity, Uri.parse(location), WebVisit.Action.ADVANCE);
        } else {  // replace
//          if (!handleVisit(activity, Uri.parse(location), WebVisit.HtmlFormHandler.REPLACE)) {
          GTurbolinks.this.url = location;
          visit();
//          }
        }
      }
    };
  }
}
