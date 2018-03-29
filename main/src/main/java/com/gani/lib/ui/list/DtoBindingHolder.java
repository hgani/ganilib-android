package com.gani.lib.ui.list;

import android.view.ViewGroup;

public abstract class DtoBindingHolder<DO> extends AbstractBindingHolder {
  public DtoBindingHolder(ViewGroup layout, boolean selectable) {
    super(layout, selectable);
  }

  public abstract void update(DO object);
}