package com.gani.lib.screen;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import android.widget.FrameLayout;

public abstract class IScreenView extends FrameLayout {
  public IScreenView(GActivity activity) {
    super(activity);
  }

  protected abstract void initNavigation(boolean topNavigation, ActionBar actionBar);
  public abstract void setBody(int resId);
  public abstract Toolbar getToolbar();
  public abstract void openDrawer();
}
