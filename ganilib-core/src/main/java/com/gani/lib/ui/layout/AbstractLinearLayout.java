package com.gani.lib.ui.layout;

import android.content.Context;
import android.graphics.drawable.Drawable;
import androidx.core.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.gani.lib.ui.view.ViewHelper;

public class AbstractLinearLayout<T extends AbstractLinearLayout> extends LinearLayout {
  private ViewHelper helper;

  protected AbstractLinearLayout(Context context) {
    super(context);

    init();
  }

  public AbstractLinearLayout(Context context, AttributeSet attrs) {
    super(context, attrs);
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

  public T vertical(){
    this.setOrientation(LinearLayout.VERTICAL);
    return self();
  }

  public T horizontal(){
    this.setOrientation(LinearLayout.HORIZONTAL);
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

  public T bgColor(String code) {
    helper.bgColor(code);
    return self();
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

  public void setWeightOf(View child, int weight) {
    LayoutParams params = (LayoutParams) child.getLayoutParams();
    if (params == null) {
      params = new LayoutParams(
              ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }
    params.width = 0;
    params.weight = weight;
    child.setLayoutParams(params);
  }

  public T rtl() {
    ViewCompat.setLayoutDirection(this, ViewCompat.LAYOUT_DIRECTION_RTL);
    return self();
  }

  public T onClick(OnClickListener listener) {
    setOnClickListener(listener);
    return self();
  }

  public T append(View child) {
    addView(child);
    return self();
  }
}
