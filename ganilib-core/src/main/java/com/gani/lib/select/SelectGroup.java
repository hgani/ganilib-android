package com.gani.lib.select;

import android.database.ContentObserver;
import android.os.Bundle;
import android.view.View;
import android.widget.TabHost;

import androidx.fragment.app.FragmentActivity;

import java.io.Serializable;

public interface SelectGroup extends Serializable {
  String getParamValue();
  String name();
  
  interface Tab extends SelectGroup {
    int getLabelResId();
  }
  
  enum NullTab implements Tab {
    DEFAULT {
      @Override
      public int getLabelResId() {
        throw new UnsupportedOperationException();
      }

      @Override
      public String getParamValue() {
        throw new UnsupportedOperationException();
      }
    };

    

    public static SelectGroup.ViewHelper<NullTab> getHelper(String bundleKey) {
      return new SelectGroup.ViewHelper<NullTab>(bundleKey) {
        @Override
        public NullTab getTab(String groupName) {
          return NullTab.valueOf(groupName);
        }
        @Override
        public NullTab[] getAll() {
          return new NullTab[] { DEFAULT };
        }
      };
    }
  }

  abstract class ViewHelper<T extends SelectGroup.Tab> {
    private String bundleKey;
    private TabHelper delegate;
    private View content;
    TabHost.TabContentFactory tabContentFactory;

    public void switchTo(Tab tab) {
      delegate.switchTo(tab);
    }

    protected ViewHelper(String bundleKey) {
      this.bundleKey = bundleKey;
    }

    @SuppressWarnings("unchecked")
    private T getSelected(Bundle savedInstanceState) {
      return (savedInstanceState == null)? getDefault() : (T) savedInstanceState.getSerializable(bundleKey);
    }
    
    private void initTabView(FragmentActivity activity, TabHost tabs) {
      this.delegate = new TabHelper(activity, tabs);
      tabs.setup();
    }
    
    private void initContentView(View content) {
      this.content = content;
      this.tabContentFactory = new TabHost.TabContentFactory() {
        @Override
        public View createTabContent(String tag) {
          return ViewHelper.this.content;
        }
      };
    }
    
    public ContentObserver addTabSpec(T tab, TabHelper.TabLabelAugmenter augmenter) {
      return delegate.addTabSpec(tab, tabContentFactory, augmenter);
    }

    public void initTabs(TabHost.OnTabChangeListener listener, T activeTab) {
      initTabs(listener, activeTab, getAll());
    }
    
    public void initTabs(TabHost.OnTabChangeListener listener, T activeTab, T... tabs) {
      for (T t : tabs) {
        delegate.addTabSpec(t, tabContentFactory);
      }

      TabHost tabHost = delegate.getTabHost();
      tabHost.setCurrentTabByTag(activeTab.name());  // Programmatic setting of tab must be before registering listener to avoid setting it off
      tabHost.setOnTabChangedListener(listener);
      
      if (activeTab == NullTab.DEFAULT) {
        delegate.hide();
      }
    }
    
    public T initView(Bundle savedInstanceState, FragmentActivity activity, TabHost tabs, View content) {
      initTabView(activity, tabs);
      initContentView(content);
      return getSelected(savedInstanceState);
    }
    
    public View findView(int viewId) {
      return content.findViewById(viewId);
    }
    
    public void save(Bundle outState, T group) {
      outState.putSerializable(bundleKey, group);
    }

    public final T getDefault() {
      return getAll()[0];
    }
    
    public abstract T[] getAll();
    public abstract T getTab(String groupName);
  }
}
