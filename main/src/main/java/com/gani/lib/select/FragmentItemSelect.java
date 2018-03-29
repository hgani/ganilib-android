package com.gani.lib.select;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;

import com.gani.lib.R;
import com.gani.lib.logging.GLog;
import com.gani.lib.screen.GFragment;

public abstract class FragmentItemSelect<I extends SelectableItem, T extends SelectGroup.Tab> extends GFragment {
//  static final String PARAM_SELECTED_ITEMS = "selectedItems";
//  static final String PARAM_MULTISELECT = "multiselect";
//
  public static final String RETURN_ITEMS = "items";

  private SelectGroup.ViewHelper<T> tabHelper;

  private T selectedTab;
//  private ControllerItemSelect<I, T> controller;

  protected FragmentItemSelect(SelectGroup.ViewHelper<T> helper) {
    this.tabHelper = helper;
  }

//  protected abstract SelectGroup getDefaultGroup();

  private View onCreateView(LayoutInflater inflater, int layoutId, Bundle savedInstanceState) {
    View fragmentLayout = inflater.inflate(layoutId, null);

    selectedTab = tabHelper.initView(savedInstanceState, getActivity(),
        (TabHost) fragmentLayout.findViewById(R.id.tabhost),
        inflater.inflate(R.layout.tabcontent_common_list, null));
    initTabsIn(inflater);

    return fragmentLayout;
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//    View fragmentLayout = inflater.inflate(R.layout.fragment_common_list, null);
//
//    selectedTab = tabHelper.initView(savedInstanceState, getActivity(),
//        (TabHost) fragmentLayout.findViewById(R.id.tabhost),
//        inflater.inflate(R.layout.tabcontent_common_list, null));
//    initTabsIn(inflater);
//
//    return fragmentLayout;
    return onCreateView(inflater, R.layout.fragment_common_list, savedInstanceState);
  }

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);

    if (savedInstanceState == null) {
      onRefresh();
    }
  }

  @Override
  protected void onRefresh() {
    super.onRefresh();

    // TODO
//    controller.asyncRetrieveDataIfNecessary();
  }

  @Override
  public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    tabHelper.save(outState, selectedTab);
  }

  @Override
  public void onDestroy() {
    // TODO
//    controller.onDestroy();
    super.onDestroy();
  }

  private void initTabsIn(LayoutInflater inflater) {
    RecyclerView listView = (RecyclerView) tabHelper.findView(R.id.list_common);

    // TODO
//    controller = createController(listView, selectedTab, getArguments().getBoolean(PARAM_MULTISELECT));
//
//    getLoaderManager().initLoader(0, null, controller);
//
    tabHelper.initTabs(new TabContentSwitcher(), selectedTab);

    initList(listView);
  }

  protected abstract void initList(RecyclerView listView);

//  protected abstract ControllerItemSelect<I, T> createController(RecyclerView listView, T group, boolean multiSelect);



  private class TabContentSwitcher implements TabHost.OnTabChangeListener {
    @Override
    public void onTabChanged(String topicTabName) {
      GLog.t(getClass(), "onTabChanged");
      selectedTab = tabHelper.getTab(topicTabName);

      // TODO
//      controller.setSelectGroup(selectedTab);
//
//      controller.asyncRetrieveDataIfNecessary();
    }
  }
}
