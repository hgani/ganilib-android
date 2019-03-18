package com.gani.lib.ui.layout;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class GRelativeLayoutParams<T extends View> extends RelativeLayout.LayoutParams {
  private T view;

  public GRelativeLayoutParams(T view) {
    super(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    this.view = view;
  }

  public GRelativeLayoutParams<T> alignRight() {
    addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
    return this;
  }

  public GRelativeLayoutParams<T> centerVertical() {
    addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
    return this;
  }

  public T end() {
    return view;
  }
}
