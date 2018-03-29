package com.gani.lib.select;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.gani.lib.R;
import com.gani.lib.database.GDbCursor;
import com.gani.lib.ui.list.DbCursorRecyclerAdapter;

public abstract class ViewBinderItemSelect<I extends SelectableItem, T extends SelectGroup.Tab> extends DbCursorRecyclerAdapter.CursorBindingHolder {
  private ItemSelectScreenHelper<I, T> activity;
  private boolean multiselect;
  private CheckBox selectButton;

  protected ViewBinderItemSelect(ViewGroup layout, ItemSelectScreenHelper<I, T> activity, boolean multiselect) {
    super(layout, false);

    this.activity = activity;
    this.multiselect = multiselect;
    this.selectButton = getLayout().findViewById(R.id.toggle_select);
  }

  @Override
  protected void bind(GDbCursor cursor) {
    selectButton.setOnCheckedChangeListener(null); // Null out listener in case we're reusing a view that possesses a previously set listener

    I item = createSelectableItem(cursor);

    selectButton.setChecked(activity.getMutableSelectedItems().contains(item));
    selectButton.setOnCheckedChangeListener(new ActivityNotifier(item));
  }

//  @Override
//  public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
//    if (view.getId() == R.id.toggle_select) {
//      CheckBox selectButton = (CheckBox) view;
//      selectButton.setOnCheckedChangeListener(null); // Null out listener in case we're reusing a view that possesses a previously set listener
//
//      I item = createSelectableItem(cursor);
//
//      selectButton.setChecked(activity.getMutableSelectedItems().contains(item));
//      selectButton.setOnCheckedChangeListener(new ActivityNotifier(item));
//
//      return true;
//    }
//
//    return false;
//  }
  
  protected abstract I createSelectableItem(GDbCursor cursor);
  
  
  
  private final class ActivityNotifier implements CompoundButton.OnCheckedChangeListener {
    private I item;
    
    private ActivityNotifier(I item) {
      this.item = item;
    }
    
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
      if (isChecked) {
        if (!multiselect) {
//          ListView listView = (ListView) activity.findViewById(R.id.list_common);
          RecyclerView listView = (RecyclerView) activity.findViewById(R.id.list_common);
          for (int i = 0; i < listView.getChildCount(); i++) {
            View itemView = listView.getChildAt(i);
            CheckBox selectButton = ((CheckBox) itemView.findViewById(R.id.toggle_select));
            // selectButton may be null if this is a section header
            if (selectButton != null && selectButton != buttonView) {
              selectButton.setChecked(false);
            }
          }
          
          activity.getMutableSelectedItems().clear();
        }
        activity.getMutableSelectedItems().add(item);
      }
      else {
        activity.getMutableSelectedItems().remove(item);
      }
    }
    
  }

}
