package com.gani.lib.ui.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.AppCompatSpinner;
import android.util.AttributeSet;
import android.widget.SpinnerAdapter;

public class GSpinner extends AppCompatSpinner implements GView {
//  private static Spec defaultSpec = new Spec();
//
//  public static void setDefaultSpec(Spec defaultSpec) {
//    GSpinner.defaultSpec = defaultSpec;
//  }

  private ViewHelper helper;

  public GSpinner(Context context) {
    super(context);
    init();
  }

  public GSpinner(Context context, AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  private void init() {
    this.helper = new ViewHelper(this);
//    defaultSpec.init(this);
  }

  public GSpinner size(Integer width, Integer height) {
//    ViewGroup.LayoutParams params = getLayoutParams();
//    if (width != null) {
//      params.width = Length.dpToPx(width);
//    }
//    if (height != null) {
//      params.height = Length.dpToPx(height);
//    }
//    setLayoutParams(params);

    helper.size(width, height);
    return this;
  }

  public GSpinner adapter(SpinnerAdapter adapter) {
    setAdapter(adapter);
    return this;
  }

  public GSpinner bgResource(int resId) {
    setBackgroundResource(resId);
    return this;
  }

  public GSpinner bgTintList(ColorStateList colorStateList) {
    ViewCompat.setBackgroundTintList(this, colorStateList);
    return this;
  }

  public GSpinner enabled(boolean enabled) {
    setEnabled(enabled);
    return this;
  }

  @Override
  public GSpinner padding(Integer left, Integer top, Integer right, Integer bottom) {
    helper.padding(left, top, right, bottom);
    return this;
  }

  @Override
  public GSpinner margin(Integer left, Integer top, Integer right, Integer bottom) {
    helper.margin(left, top, right, bottom);
    return this;
  }

  public GSpinner onItemSelected(OnItemSelectedListener listener) {
    setOnItemSelectedListener(listener);
    return this;
  }
}
