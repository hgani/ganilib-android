package com.gani.lib.ui.list;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gani.lib.R;
import com.gani.lib.ui.Ui;

public abstract class AbstractBindingHolder extends RecyclerView.ViewHolder {
    private ViewGroup layout;

    public AbstractBindingHolder(ViewGroup layout, boolean selectable) {
      super(layout);
      this.layout = layout;

      if (selectable) {
        unhighlightSelectable();
      }
    }

    public ViewGroup getLayout() {
      return layout;
    }

    public Context getContext() {
      return layout.getContext();
    }

    protected static ViewGroup inflate(ViewGroup parent, int layoutId) {
      return (ViewGroup) LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
    }

    protected void unselectable() {
      layout.setBackgroundDrawable(Ui.resources().getDrawable(R.color.transparent));
    }

    protected void highlightSelectable() {
      layout.setBackgroundDrawable(Ui.resources().getDrawable(R.drawable.background_post_highlight_selector));
    }

    protected void unhighlightSelectable() {
      // See http://stackoverflow.com/questions/8732662/how-to-set-background-highlight-to-a-linearlayout/28087443#28087443
      TypedValue outValue = new TypedValue();
      Ui.context().getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);
      layout.setBackgroundResource(outValue.resourceId);
    }
  }
