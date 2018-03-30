package com.basecamp.turbolinks;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.webkit.WebView;

public class InlineTurbolinksView extends TurbolinksView {
    public InlineTurbolinksView(Context context) {
        super(context);
        init();
    }

    public InlineTurbolinksView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public InlineTurbolinksView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public InlineTurbolinksView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        removeView(getRefreshLayout());
    }

    @Override
    boolean attachWebView(WebView webView, boolean screenshotsEnabled, boolean pullToRefreshEnabled) {
        if (webView.getParent() == this) return false;

        if (webView.getParent() instanceof TurbolinksView) {
            TurbolinksView previousTurbolinksView = (TurbolinksView) webView.getParent();
            previousTurbolinksView.removeView(webView);
        }

        addView(webView);
        return true;
    }
}
