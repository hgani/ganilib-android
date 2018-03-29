package com.gani.lib.ui.layout;

import android.widget.LinearLayout;

import com.gani.lib.ui.style.Length;

public class GLayoutParams<T extends GLayoutParams> extends LinearLayout.LayoutParams {
  public GLayoutParams() {
    super(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
  }

  private T self() {
    return (T) this;
  }

  public T width(int width) {
    this.width = Length.dpToPx(width);
    return self();
  }

  public T height(int height) {
    this.height = Length.dpToPx(height);
    return self();
  }

  public T margins(int left, int top, int right, int bottom){
    super.setMargins(Length.dpToPx(left), Length.dpToPx(top), Length.dpToPx(right), Length.dpToPx(bottom));
    return self();
  }
}
