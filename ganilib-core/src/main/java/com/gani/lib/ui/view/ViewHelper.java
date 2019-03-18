package com.gani.lib.ui.view;

import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.gani.lib.logging.GLog;
import com.gani.lib.ui.Ui;
import com.gani.lib.ui.layout.GRelativeLayoutParams;
import com.gani.lib.ui.style.Length;

public class ViewHelper {
  private View view;

  public ViewHelper(View view) {
    this(view, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
  }

  public ViewHelper(View view, ViewGroup.LayoutParams layoutParams) {
    this.view = view;

    // Ensure layout params can't be null.
    view.setLayoutParams(layoutParams);
  }

  public <T extends View> GRelativeLayoutParams relative(T view) {
//    view.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    GRelativeLayoutParams<T> params = new GRelativeLayoutParams(view);
    this.view.setLayoutParams(params);
    return params;
  }

  public void alignParentRight() {
    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view.getLayoutParams();
    params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
  }

  public void size(Integer width, Integer height) {
    ViewGroup.LayoutParams params = view.getLayoutParams();
    if (width != null) {
      if (width >= 0) {
        params.width = Length.dpToPx(width);
      }
      else {
        params.width = width;
      }
    }
    if (height != null) {
      if (height >= 0) {
        params.height = Length.dpToPx(height);
      }
      else {
        params.height = height;
      }
    }
    view.setLayoutParams(params);
  }

  public void width(Integer width) {
    size(width, null);
  }

  public void height(Integer height) {
    size(null, height);
  }

  public void padding(Integer left, Integer top, Integer right, Integer bottom) {
    if (left == null) {
      left = view.getPaddingLeft();
    }
    else {
      left = Length.dpToPx(left);
    }

    if (top == null) {
      top = view.getPaddingTop();
    }
    else {
      top = Length.dpToPx(top);
    }

    if (right == null) {
      right = view.getPaddingRight();
    }
    else {
      right = Length.dpToPx(right);
    }

    if (bottom == null) {
      bottom = view.getPaddingBottom();
    }
    else {
      bottom = Length.dpToPx(bottom);
    }

    view.setPadding(left, top, right, bottom);
  }

  public void margin(Integer left, Integer top, Integer right, Integer bottom) {
    ViewGroup.LayoutParams params = view.getLayoutParams();
    if (params instanceof ViewGroup.MarginLayoutParams) {
      ViewGroup.MarginLayoutParams marginParams = ((ViewGroup.MarginLayoutParams) params);

//      ((ViewGroup.MarginLayoutParams) params).setMargins(
//          Length.dpToPx(left),
//          Length.dpToPx(top),
//          Length.dpToPx(right),
//          Length.dpToPx(bottom));


      if (left == null) {
//        left = view.getPaddingLeft();
        left = marginParams.leftMargin;
      }
      else {
        left = Length.dpToPx(left);
      }

      if (top == null) {
//        top = view.getPaddingTop();
        top = marginParams.topMargin;
      }
      else {
        top = Length.dpToPx(top);
      }

      if (right == null) {
//        right = view.getPaddingRight();
        right = marginParams.rightMargin;
      }
      else {
        right = Length.dpToPx(right);
      }

      if (bottom == null) {
//        bottom = view.getPaddingBottom();
        bottom = marginParams.bottomMargin;
      }
      else {
        bottom = Length.dpToPx(bottom);
      }
//
//      view.setPadding(left, top, right, bottom);

      marginParams.setMargins(left, top, right, bottom);
    }
    else {
      GLog.w(getClass(), "Unable to set margin for: " + view);
    }
  }

  public void click(View.OnClickListener listener) {
    view.setOnClickListener(listener);
  }


  public void bgColor(int res) {
    view.setBackgroundColor(res);
  }

  public void bgColor(String code) {
    view.setBackgroundColor(Ui.color(code));
  }

  public void bg(int res) {
    int sdk = android.os.Build.VERSION.SDK_INT;
    if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
      view.setBackgroundDrawable(Ui.drawable(res));
    } else {
      view.setBackground(Ui.drawable(res));
    }
  }
}
