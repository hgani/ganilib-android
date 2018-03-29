package com.gani.lib.select;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.gani.lib.R;
import com.gani.lib.screen.GActivity;
import com.gani.lib.ui.Ui;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class ItemSelectScreenHelper<I extends SelectableItem, T extends SelectGroup.Tab> {
  static final String PARAM_SELECTED_ITEMS = "selectedItems";

  private static final String BUNDLE_SELECTED_ITEMS = "selectedItems";

//  public static final String RETURN_ITEMS = "items";

  public static <I extends SelectableItem, T extends SelectGroup.Tab> Intent intent(
      Class<? extends GActivity> cls, List<I> currentSelection) {
    Intent intent = new Intent(Ui.context(), cls);
    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
    intent.putExtra(PARAM_SELECTED_ITEMS, (Serializable) currentSelection);
//    intent.putExtra(FragmentItemSelect.PARAM_MULTISELECT, multiselect);
    return intent;
  }

  private GActivity activity;
  private FragmentItemSelect<I, T> fragment;
  private Set<I> selectedItems;
  private boolean multiselect;

  public ItemSelectScreenHelper(GActivity activity, Bundle savedInstanceState, FragmentItemSelect<I, T> fragment,
                                boolean multiselect) {
    this.activity = activity;
    this.fragment = fragment;
    this.multiselect = multiselect;
    this.selectedItems = (savedInstanceState == null)?
        new LinkedHashSet<I>((List<I>) activity.getIntent().getSerializableExtra(PARAM_SELECTED_ITEMS)) :
//        new LinkedHashSet<I>((List<I>) initialSelection) :
        (LinkedHashSet<I>) savedInstanceState.getSerializable(BUNDLE_SELECTED_ITEMS);

//    onCreate(savedInstanceState);
    activity.setFragmentWithToolbar(fragment, false, savedInstanceState);
  }

  public ItemSelectScreenHelper(Bundle savedInstanceState, boolean multiselect) {
    this.multiselect = multiselect;
    this.selectedItems = (LinkedHashSet<I>) savedInstanceState.getSerializable(BUNDLE_SELECTED_ITEMS);
  }

//  protected ItemSelectScreenHelper(FragmentItemSelect<I, T> fragment) {
//    this.fragment = fragment;
//  }
//
//  private void onCreate(Bundle savedInstanceState) {
//    activity.setFragmentWithToolbar(fragment, false, savedInstanceState);
//  }

  public void initResult() {
    activity.setOkResult(FragmentItemSelect.RETURN_ITEMS, new ArrayList<I>(selectedItems));
  }

  public void onSaveInstanceState(Bundle outState) {
//    super.onSaveInstanceState(outState);
    outState.putSerializable(BUNDLE_SELECTED_ITEMS, (Serializable) selectedItems);
  }

  public Set<I> getMutableSelectedItems() {
    return selectedItems;
  }

  public View findViewById(@IdRes int id) {
    return activity.findViewById(id);
  }



  final class ActivityNotifier implements CompoundButton.OnCheckedChangeListener {
    private I item;

    ActivityNotifier(I item) {
      this.item = item;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
      if (isChecked) {
        if (!multiselect) {
          RecyclerView listView = (RecyclerView) activity.findViewById(R.id.list_common);
          for (int i = 0; i < listView.getChildCount(); i++) {
            View itemView = listView.getChildAt(i);
            CheckBox selectButton = ((CheckBox) itemView.findViewById(R.id.toggle_select));
            // selectButton may be null if this is a section header
            if (selectButton != null && selectButton != buttonView) {
              selectButton.setChecked(false);
            }
          }

          selectedItems.clear();
        }
        selectedItems.add(item);
      }
      else {
        selectedItems.remove(item);
      }
    }
  }
}
