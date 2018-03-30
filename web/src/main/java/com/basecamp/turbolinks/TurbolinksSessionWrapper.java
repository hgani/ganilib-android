package com.basecamp.turbolinks;

import android.app.Activity;
import android.content.Context;
import android.webkit.WebView;

public class TurbolinksSessionWrapper {
  private TurbolinksSession session;

  public TurbolinksSessionWrapper(Context context) {
    this.session = TurbolinksSession.getNew(context);
  }

  public TurbolinksSessionWrapper adapter(TurbolinksAdapter adapter) {
    session.adapter(adapter);
    return this;
  }

  public TurbolinksSessionWrapper activity(Activity activity) {
    session.activity(activity);
    return this;
  }

  public TurbolinksSessionWrapper view(TurbolinksView view) {
    session.view(view);
    return this;
  }

  public void restore(String url) {
    session.restoreWithCachedSnapshot(true);

    // This is mainly to prevent issue with file upload whereby the webview gets refreshed and losing its current state after selecting a file.
    // Another positive side effect is performance.
    //
    // NOTE: In the future, in the unlikely event that we want to open a new TL activity from a TL form, we'd need a more generic solution
    // that saves and restores the webview state so user won't lose their input. But this is quite unlikely and preliminary attempt shows
    // that it's not straight forward. In addition it also seems to have a noticeable performance impact.
    if (session.webViewAttachedToNewParent) {
      session.visit(url);
    }
    else {
      // Reset state
      session.restoreWithCachedSnapshot = false;
    }
  }

  public void advance(String url) {
    session.visit(url);
  }

  public WebView getWebView() {
    return session.getWebView();
  }
}
