package com.gani.lib.screen;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;

import com.gani.lib.R;
import com.gani.lib.ui.icon.GIcon;
import com.gani.lib.ui.style.Length;

class NavigationHomeBadge {
  private GScreenView screenView;
  private Context context;
  private LayerDrawable drawable;

  private static LayerDrawable reusedDrawable;

  NavigationHomeBadge(GScreenView screenView) {
    this.screenView = screenView;
    this.context = screenView.getContext();

    // Gets the drawable layerlist that has the navigation view and the badger items
    if  (Build.VERSION.SDK_INT > 15) {
      drawable = (LayerDrawable) ((Build.VERSION.SDK_INT >= 21) ?
          context.getApplicationContext().getDrawable(R.drawable.action_bar_home_drawable) :
          context.getResources().getDrawable(R.drawable.action_bar_home_drawable));
    }
    else {
      // TODO: Throw this away once we decide not to support API level 15
      // See http://stackoverflow.com/questions/30650159/nullpointerexception-in-supportappcompat-v7-library-on-api-15
      if (reusedDrawable == null) {
        reusedDrawable = (LayerDrawable) context.getResources().getDrawable(R.drawable.action_bar_home_drawable);
      }
      drawable = reusedDrawable;
    }

//    drawable = (LayerDrawable) context.getResources().drawable(R.drawable.action_bar_home_drawable);

//    BadgeDrawable.setBadgeCount(context, drawable, 0);
    setCount(0);
  }

  Drawable getDrawable() {
    return drawable;
  }

  void setCount(int count) {
//    BadgeDrawable.setBadgeCount(context, drawable, count);

    BadgeDrawable badge;
//      Drawable actionBarIcon = new IconDrawable(context, CvIcon.cv_menu).actionBarSize();
//      Drawable actionBarIcon = CvIcon.cv_menu.drawable().actionBarSize();

    // Reuse drawable if possible
//      Drawable reuse = icon.findDrawableByLayerId(R.id.ic_badge);
//      if (reuse != null && reuse instanceof BadgeDrawable) {
//        badge = (BadgeDrawable) reuse;
//      } else {
//        badge = new BadgeDrawable(context);
//      }

    GIcon icon = screenView.menuIcon();
    if (icon != null) {
      Drawable actionBarIcon = icon.drawable().actionBarSize();

      // Experiment with not reusing and see if it causes any issue
      badge = new BadgeDrawable(context);
      badge.setCount(count);

      drawable.mutate();
      drawable.setDrawableByLayerId(R.id.ic_badge, badge);
      drawable.setDrawableByLayerId(R.id.ic_navigation, actionBarIcon);
      drawable.invalidateSelf();
    }
  }



  private static class BadgeDrawable extends Drawable {

    private float mTextSize;
    private Paint mBadgePaint;
    private Paint mTextPaint;
    private Rect mTxtRect = new Rect();

    private String mCount = "";
    private boolean mWillDraw = false;

    public BadgeDrawable(Context context) {
//      mTextSize = context.getResources().getDimension();
      mTextSize = Length.dpToPx(12);

      mBadgePaint = new Paint();
      mBadgePaint.setColor(Color.RED);
      mBadgePaint.setAntiAlias(true);
      mBadgePaint.setStyle(Paint.Style.FILL);

      mTextPaint = new Paint();
      mTextPaint.setColor(Color.WHITE);
      mTextPaint.setTypeface(Typeface.DEFAULT_BOLD);
      mTextPaint.setTextSize(mTextSize);
      mTextPaint.setAntiAlias(true);
      mTextPaint.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    public void draw(Canvas canvas) {
      if (!mWillDraw) {
        return;
      }

      Rect bounds = getBounds();
      float width = bounds.right - bounds.left;
      float height = bounds.bottom - bounds.top;

      // Position the badge in the top-right quadrant of the icon.
      float temporaryRadius = ((Math.min(width, height) / 2) - 1) /2;
      float centerX = width - temporaryRadius - 1;
      float centerY = temporaryRadius + 1;

//      float radius = 24;
      float radius = mTextSize;

      // Draw badge circle.
      canvas.drawCircle(centerX, centerY, radius, mBadgePaint);

      // Draw badge count text inside the circle.
      mTextPaint.getTextBounds(mCount, 0, mCount.length(), mTxtRect);
      float textHeight = mTxtRect.bottom - mTxtRect.top;
      float textY = centerY + (textHeight / 2f);
      canvas.drawText(mCount, centerX, textY, mTextPaint);
    }

    /*
    Sets the count (i.e notifications) to display.
     */
    private void setCount(int count) {
      mCount = Integer.toString(count);

      // Only draw a badge if there are notifications.
      mWillDraw = count > 0;

//      invalidateSelf();
    }

    @Override
    public void setAlpha(int alpha) {
      // do nothing
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
      // do nothing
    }

    @Override
    public int getOpacity() {
      return PixelFormat.UNKNOWN;
    }

//    static void setBadgeCount(Context context, LayerDrawable icon, int count) {
//      BadgeDrawable badge;
////      Drawable actionBarIcon = new IconDrawable(context, CvIcon.cv_menu).actionBarSize();
////      Drawable actionBarIcon = CvIcon.cv_menu.drawable().actionBarSize();
//      Drawable actionBarIcon = screenView.menuIcon();
//
//      // Reuse drawable if possible
////      Drawable reuse = icon.findDrawableByLayerId(R.id.ic_badge);
////      if (reuse != null && reuse instanceof BadgeDrawable) {
////        badge = (BadgeDrawable) reuse;
////      } else {
////        badge = new BadgeDrawable(context);
////      }
//
//      // Experiment with not reusing and see if it causes any issue
//      badge = new BadgeDrawable(context);
//      badge.setCount(count);
//
//      icon.mutate();
//      icon.setDrawableByLayerId(R.id.ic_badge, badge);
//      icon.setDrawableByLayerId(R.id.ic_navigation, actionBarIcon);
//      icon.invalidateSelf();
//    }
  }
}