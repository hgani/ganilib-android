package com.gani.map;

import android.content.Context;
import android.util.AttributeSet;

import com.gani.lib.ui.view.ViewHelper;
import com.google.android.gms.maps.MapView;

public class GMapView<T extends GMapView> extends MapView {
  private ViewHelper helper;

  public GMapView(Context context) {
    super(context);
    init();
  }

  public GMapView(Context context, AttributeSet attrs) {
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
}
