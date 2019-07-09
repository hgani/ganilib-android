package com.gani.lib.ui.view;

import android.content.Context;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import android.util.AttributeSet;

public class GAutoCompleteTextView<T extends GAutoCompleteTextView> extends AppCompatAutoCompleteTextView implements GView {
  private ViewHelper helper;

  public GAutoCompleteTextView(Context context) {
    super(context);
    init();
  }

  public GAutoCompleteTextView(Context context, AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  private void init() {
    this.helper = new ViewHelper(this);
  }

  private T self() {
    return (T) this;
  }

  public GAutoCompleteTextView size(Integer width, Integer height) {
    helper.size(width, height);
    return self();
  }

  @Override
  public GAutoCompleteTextView padding(Integer left, Integer top, Integer right, Integer bottom) {
    helper.padding(left, top, right, bottom);
    return self();
  }

  @Override
  public GAutoCompleteTextView margin(Integer left, Integer top, Integer right, Integer bottom) {
    helper.margin(left, top, right, bottom);
    return self();
  }
}
