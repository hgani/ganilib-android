package com.gani.lib.ui.style;

import android.content.Context;
import android.graphics.Point;
import android.view.WindowManager;

import com.gani.lib.ui.Ui;

public class Length {
  public static int pxToDp(int px) {
    return Math.round(px / Ui.resources().getDisplayMetrics().density);
  }
  public static int dpToPx(int dp) {
    return Math.round(dp * Ui.resources().getDisplayMetrics().density);
  }

  private static Point windowRawSize() {
    WindowManager wm = (WindowManager) Ui.context().getSystemService(Context.WINDOW_SERVICE);
    Point size = new Point();
    wm.getDefaultDisplay().getSize(size);
    return size;
//    Display display = wm.getDefaultDisplay();
//    return new Point(display.getWidth(), display.getHeight());
  }

  public static int longerSide() {
    Point size = windowRawSize();
    return (size.x > size.y) ? size.x : size.y;
  }

  public static int windowWidth() {
    return pxToDp(windowRawSize().x);
  }

  public static int windowWidthPx(){
    return windowRawSize().x;
  }

  public static int windowHeight() {
    return pxToDp(windowRawSize().y);
  }

  public static int windowHeightPx(){
    return windowRawSize().y;
  }
}
