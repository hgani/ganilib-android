//package com.gani.lib.select;
//
//import android.content.Intent;
//import android.os.Bundle;
//
//import com.gani.lib.screen.GActivity;
//import com.gani.lib.ui.Ui;
//
//import java.io.Serializable;
//import java.util.ArrayList;
//import java.util.LinkedHashSet;
//import java.util.List;
//import java.util.Set;
//
//public abstract class ScreenItemSelect<I extends SelectableItem, T extends SelectGroup.Tab> extends GActivity {
//  static final String PARAM_SELECTED_ITEMS = "selectedItems";
//
//  private static final String BUNDLE_SELECTED_ITEMS = "selectedItems";
//
//  public static final String RETURN_ITEMS = "items";
//
//  public static <I extends SelectableItem, T extends SelectGroup.Tab> Intent intent(
//      Class<? extends ScreenItemSelect<I, T>> cls, List<I> selectedItems, boolean multiselect) {
//    Intent intent = new Intent(Ui.context(), cls);
//    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//    intent.putExtra(FragmentItemSelect.PARAM_SELECTED_ITEMS, (Serializable) selectedItems);
//    intent.putExtra(FragmentItemSelect.PARAM_MULTISELECT, multiselect);
//    return intent;
//  }
//
//  private FragmentItemSelect<I, T> fragment;
//  private Set<I> selectedItems;
//
//  protected ScreenItemSelect(FragmentItemSelect<I, T> fragment) {
//    this.fragment = fragment;
//  }
//
//  @Override
//  protected void onCreate(Bundle savedInstanceState) {
//    super.onCreate(savedInstanceState);
//
//    setFragmentWithToolbar(fragment, false, savedInstanceState);
//
//    this.selectedItems = (savedInstanceState == null)?
//        new LinkedHashSet<I>((List<I>) getIntent().getSerializableExtra(PARAM_SELECTED_ITEMS)) :
//        (LinkedHashSet<I>) savedInstanceState.getSerializable(BUNDLE_SELECTED_ITEMS);
//  }
//
//  @Override
//  public void onBackPressed() {
//    setOkResult(RETURN_ITEMS, new ArrayList<I>(selectedItems));
//    super.onBackPressed();
//  }
//
//  @Override
//  public void onSaveInstanceState(Bundle outState) {
//    super.onSaveInstanceState(outState);
//    outState.putSerializable(BUNDLE_SELECTED_ITEMS, (Serializable) selectedItems);
//  }
//
//  public Set<I> getMutableSelectedItems() {
//    return selectedItems;
//  }
//
//}
