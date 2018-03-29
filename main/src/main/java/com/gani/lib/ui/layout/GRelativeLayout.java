package com.gani.lib.ui.layout;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.gani.lib.R;
import com.gani.lib.ui.Ui;
import com.gani.lib.ui.view.ViewHelper;

import static android.R.attr.button;
import static android.R.attr.width;

public class GRelativeLayout<T extends GRelativeLayout> extends RelativeLayout {
  private ViewHelper helper;

  public GRelativeLayout(Context context) {
    super(context);

    init();
  }

  private void init() {
    this.helper = new ViewHelper(this);
  }

  private T self() {
    return (T) this;
  }

  public T size(Integer width, Integer height) {
    helper.size(width, height);
    return self();
  }

  public T width(Integer width) {
    helper.width(width);
    return self();
  }

  public T height(Integer height) {
    helper.height(height);
    return self();
  }

  public T padding(Integer left, Integer top, Integer right, Integer bottom) {
    helper.padding(left, top, right, bottom);
    return self();
  }

  public T margin(Integer left, Integer top, Integer right, Integer bottom) {
    helper.margin(left, top, right, bottom);
    return self();
  }

  public T gravity(int gravity) {
    setGravity(gravity);
    return self();
  }

  public T bgColor(int color) {
    setBackgroundColor(color);
    return self();
  }

  public T bgColor(String color) {
    return bgColor(Ui.color(color));
  }

  public T bg(Drawable drawable) {
    int sdk = android.os.Build.VERSION.SDK_INT;
    if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
      setBackgroundDrawable(drawable);
    } else {
      setBackground(drawable);
    }

    // Not sure why this doesn't work
//    ViewCompat.setBackground(this, drawable);
    return self();
  }

  public T append(View child) {
    addView(child);
    return self();
  }
}
