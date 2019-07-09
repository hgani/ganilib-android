package com.gani.lib.ui.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import androidx.appcompat.widget.AppCompatImageView;
import android.util.AttributeSet;

import com.bumptech.glide.Glide;
import com.gani.lib.ui.Ui;

public class GImageView extends AppCompatImageView {
  private ViewHelper helper;

  public GImageView(Context context) {
    super(context);
    init();
  }

  public GImageView(Context context, AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  private void init() {
    this.helper = new ViewHelper(this);
  }

  // TODO: Remove once we make sure none of our projects is using this anymore
  public void setImageUrl(String url) {
    if (url != null) {
      Glide.with(getContext())
          .load(url)
          .into(this);
    }
  }

  public GImageView imageUrl(String url) {
    if (url != null) {
      Glide.with(getContext().getApplicationContext()) //using application context prevents app crash when closing activity during image load
          .load(url)
          .into(this);
    }
    return this;
  }

  public GImageView size(Integer width, Integer height) {
    helper.size(width, height);
    return this;
  }

  public GImageView width(Integer width) {
    helper.width(width);
    return this;
  }

  public GImageView height(Integer height) {
    helper.height(height);
    return this;
  }

  // NOTE: Deprecated
  public GImageView background(String code) {
    background(Ui.color(code));
    return this;
  }

  // NOTE: Deprecated
  public GImageView background(int color) {
    setBackgroundColor(color);
    return this;
  }

  public GImageView bgColor(String code) {
    background(Ui.color(code));
    return this;
  }

  public GImageView bgColor(int res) {
    helper.bgColor(res);
    return this;
  }

  public GImageView bg(int res) {
    helper.bg(res);
    return this;
  }

  public GImageView drawable(Drawable drawable) {
    setImageDrawable(drawable);
    return this;
  }

  public GImageView drawable(int resId) {
    setImageDrawable(Ui.drawable(resId));
    return this;
  }

  public GImageView margin(Integer left, Integer top, Integer right, Integer bottom) {
    helper.margin(left, top, right, bottom);
    return this;
  }

  public GImageView adjustViewBounds() {
    setAdjustViewBounds(true);
    return this;
  }

  public GImageView click(OnClickListener listener) {
    helper.click(listener);
    return this;
  }
}
