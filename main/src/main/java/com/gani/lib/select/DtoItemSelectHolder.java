package com.gani.lib.select;

import android.view.ViewGroup;
import android.widget.CheckBox;

import com.gani.lib.R;
import com.gani.lib.logging.GLog;
import com.gani.lib.ui.list.DtoBindingHolder;

public abstract class DtoItemSelectHolder<I extends SelectableItem> extends DtoBindingHolder<I> {
  private ItemSelectScreenHelper<I, ?> helper;
//  private boolean multiselect;
  private CheckBox selectButton;

  protected DtoItemSelectHolder(ViewGroup layout, ItemSelectScreenHelper<I, ?> activity, boolean multiselect) {
    super(layout, false);

    this.helper = activity;
//    this.multiselect = multiselect;
    this.selectButton = getLayout().findViewById(R.id.toggle_select);
  }

  @Override
  public void update(I item) {
    GLog.t(getClass(), "UPDATE: " + helper.getMutableSelectedItems());
    selectButton.setChecked(helper.getMutableSelectedItems().contains(item));
    selectButton.setOnCheckedChangeListener(helper.new ActivityNotifier(item));
  }

//  @Override
//  public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
//    if (view.getId() == R.id.toggle_select) {
//      CheckBox selectButton = (CheckBox) view;
//      selectButton.setOnCheckedChangeListener(null); // Null out listener in case we're reusing a view that possesses a previously set listener
//
//      I item = createSelectableItem(cursor);
//
//      selectButton.setChecked(helper.getMutableSelectedItems().contains(item));
//      selectButton.setOnCheckedChangeListener(new ActivityNotifier(item));
//
//      return true;
//    }
//
//    return false;
//  }



}
