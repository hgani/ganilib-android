package com.gani.lib.select;

import android.database.ContentObserver;
import android.os.Build;
import android.os.Handler;
import android.view.View;
import android.widget.TabHost;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import com.gani.lib.database.GDbTable;
import com.gani.lib.ui.Ui;

public class TabHelper {
  private FragmentActivity activity;
  private TabHost tabs;

  public TabHelper(FragmentActivity activity, TabHost tabs) {
    this.activity = activity;
    this.tabs = tabs;
  }

  public void switchTo(SelectGroup.Tab tab) {
    tabs.setCurrentTabByTag(tab.name());
  }
  
  public TabHost getTabHost() {
    return tabs;
  }

  public void clearTab() {
    tabs.clearAllTabs();
  }

  public ContentObserver addTabSpec(final SelectGroup.Tab group, TabHost.TabContentFactory factory, final TabLabelAugmenter labelAugmenter) {
    final TabHost.TabSpec spec = tabs.newTabSpec(group.name());
    spec.setContent(factory);
    
    ContentObserver observer = null;
    if (labelAugmenter == null) {
      if (group == SelectGroup.NullTab.DEFAULT) {
        setIndicatorOf(spec, "");
      }
      else {
        setIndicatorOf(spec, Ui.str(group.getLabelResId()));
      }
    }
    else {
      // Apparently this is the only way to change a tab's label. See http://stackoverflow.com/questions/16836053/change-tab-label-android
      observer = new ContentObserver(new Handler()) {
        @Override
        public void onChange(boolean selfChange) {
          if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            ((TextView) tabs.getTabWidget().getChildAt(0).findViewById(android.R.id.title)).setText(Ui.str(group.getLabelResId(), labelAugmenter.getAugment()));
          }
        }
      };
//      DbUtils.registerObserver(labelAugmenter.getObserverdUri(), observer);
      labelAugmenter.getObservedTable().registerObserver(observer);
      setIndicatorOf(spec, Ui.str(group.getLabelResId(), labelAugmenter.getAugment()));
    }

    tabs.addTab(spec);
    return observer;
  }

  public void addTabSpec(final SelectGroup.Tab group, TabHost.TabContentFactory factory) {
    addTabSpec(group, factory, null);
  }

  private void setIndicatorOf(TabHost.TabSpec spec, String label) {
    spec.setIndicator(label);
//    int sdkVersion = Build.VERSION.SDK_INT;
//    if (sdkVersion < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
//      // Use custom style because the default one sucks
//      spec.setIndicator(createCustomIndicator(label));
//    }
//    else {  // Use the default Holo theme
//      spec.setIndicator(label);
//    }
  }

//  private View createCustomIndicator(String label) {
//    View tabIndicator = LayoutInflater.from(activity).inflate(R.layout.tabindicator, tabs.getTabWidget(), false);
//    TextView title = (TextView) tabIndicator.findViewById(R.id.title);
//    title.setText(label);
//    return tabIndicator;
//  }
  
  public void hide() {
    tabs.findViewById(android.R.id.tabs).setVisibility(View.GONE);
  }

  
  public interface TabLabelAugmenter {
    GDbTable getObservedTable();
    String getAugment();
  }
}
