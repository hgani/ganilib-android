package com.gani.lib.ui.view.pager;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;

import androidx.viewpager.widget.PagerAdapter;

import com.gani.lib.ui.Ui;
import com.gani.lib.ui.view.GImageView;

import java.util.ArrayList;
import java.util.List;

public class DrawablePagerAdapter extends PagerAdapter {
  private List<Drawable> drawables;
  private String[] urls;
  private Integer height;

  public DrawablePagerAdapter height(Integer height) {
    this.height = height;
    return this;
  }

  public DrawablePagerAdapter clear() {
    this.drawables = null;
    this.urls = null;
    notifyDataSetChanged();
    return this;
  }

  public DrawablePagerAdapter drawables(int... resources) {
    drawables = new ArrayList<>(resources.length);
    for (int res : resources) {
      drawables.add(Ui.drawable(res));
    }
    notifyDataSetChanged();
    return this;
  }

  public DrawablePagerAdapter urls(String... urls) {
    this.urls = urls;
    notifyDataSetChanged();
    return this;
  }

  public DrawablePagerAdapter urls(List<String> urls) {
    return urls(urls.toArray(new String[urls.size()]));
  }

  // See https://stackoverflow.com/questions/7263291/viewpager-pageradapter-not-updating-the-view
  public int getItemPosition(Object object) {
    return POSITION_NONE;
  }

  @Override
  public int getCount() {
    if (drawables != null) {
      return drawables.size();
    }
    else if (urls != null) {
      return urls.length;
    }
    return 0;
  }

  @Override
  public Object instantiateItem(ViewGroup container, int position) {
    GImageView view = new GImageView(container.getContext()).height(height).adjustViewBounds();

    if (drawables != null) {
      view.drawable(drawables.get(position));
    }
    else if (urls != null) {
      view.imageUrl(urls[position]);
    }

    container.addView(view);
    return view;
  }

  @Override
  public void destroyItem(ViewGroup container, int position, Object object) {
    container.removeView((View) object);
  }

  @Override
  public boolean isViewFromObject(View view, Object object) {
    return view == object;
  }
}