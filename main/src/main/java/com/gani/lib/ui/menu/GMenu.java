package com.gani.lib.ui.menu;

import android.content.Context;
import android.view.Menu;
import android.view.MenuItem;

import com.gani.lib.ui.icon.GIcon;

public class GMenu {
  public static final int ORDER_UNIMPORTANT = 1000;
  public static final int ORDER_SPECIFIC = 10;
  public static final int ORDER_COMMON = 20;

  private Menu menu;
//  private Context context;

  public GMenu(Menu menu) {
    this.menu = menu;
  }

  // For backward compatibility only
  public GMenu(Menu menu, Context context) {
    this.menu = menu;
  }

//  public GMenu(Menu menu, Context context) {
//    this.menu = menu;
//    this.context = context;
//  }
//
//  public GMenu(Menu menu, GFragment fragment) {
//    this(menu, fragment.getContext());
//  }

  public boolean hasVisibleItems() {
    return menu.hasVisibleItems();
  }

//  private MenuItem add(String str, CvIcon icon, Integer showAsAction) {
//    MenuItem item = menu.add(Menu.NONE, Menu.NONE, Menu.NONE, str);
//    if (showAsAction != null) {  // Not applicable for certain types of menu, e.g. popup menu.
//      item.setShowAsAction(showAsAction);
//    }
//    if (icon != null) {
//      item.setIcon(icon.drawable().actionBarSize());
//    }
//    return item;
//  }
//
//  public MenuItem add(int strId, CvIcon icon, Integer showAsAction) {
//    return add(Ui.str(strId), icon, showAsAction);
//  }
//
//  public MenuItem add(int strId, CvIcon icon, int showAsAction, Intent intent) {
//    MenuItem item = add(strId, icon, showAsAction);
//    item.setIntent(intent);
//    return item;
//  }
//
//  public MenuItem add(int strId, CvIcon icon, Integer showAsAction, MenuItem.OnMenuItemClickListener listener) {
//    MenuItem item = add(strId, icon, showAsAction);
//    item.setOnMenuItemClickListener(listener);
//    return item;
//  }
//
//  public MenuItem addTurbolinks(PathSpec pathSpec, CvIcon icon, Integer showAsAction) {
//    MenuItem item = add(pathSpec.getTitle(), icon, showAsAction);
//    item.setIntent(GDialogTurbolinks.intent(pathSpec));
//    return item;
//  }

  public MenuItem add(String str, GIcon icon, Integer showAsAction, OnClickListener listener) {
    MenuItem item = menu.add(Menu.NONE, Menu.NONE, Menu.NONE, str);
    if (showAsAction != null) {  // Not applicable for certain types of menu, e.g. popup menu.
      item.setShowAsAction(showAsAction);
    }
    if (icon != null) {
      item.setIcon(icon.drawable().actionBarSize());
    }
    if (listener != null) {
      item.setOnMenuItemClickListener(listener);
    }
    return item;
  }

  public MenuItem addSecondary(int strId, int order, OnClickListener listener) {
    MenuItem item = menu.add(Menu.NONE, Menu.NONE, Menu.CATEGORY_CONTAINER + ORDER_UNIMPORTANT + order, strId);
    item.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
    item.setOnMenuItemClickListener(listener);
    return item;
  }

  public MenuItem addBlankSecondary() {
    MenuItem item = menu.add(Menu.NONE, Menu.NONE, Menu.CATEGORY_CONTAINER + ORDER_UNIMPORTANT + GMenu.ORDER_SPECIFIC, "");
    item.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
    return item;
  }



  public abstract static class OnClickListener implements MenuItem.OnMenuItemClickListener {
    @Override
    public final boolean onMenuItemClick(MenuItem menuItem) {
      onClick(menuItem);
      return true;
    }

    protected abstract void onClick(MenuItem menuItem);
  }
}