package com.gani.lib.ui.list;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.ViewGroup;

import com.gani.lib.R;

import java.util.List;

public abstract class DtoRecyclerAdapter<DO, VH
    extends DtoBindingHolder<DO>> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
  private List<DO> objects;

  protected DtoRecyclerAdapter(List<DO> objects) {
    this.objects = objects;
  }

  public void initForList(RecyclerView recyclerView) {
    initForList(recyclerView, true);
  }

  public RecyclerListHelper initForList(RecyclerView recyclerView, boolean withSeparator) {
    Context context = recyclerView.getContext();
    if (withSeparator) {
      recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL_LIST));
    }
    recyclerView.setAdapter(this);
    return new RecyclerListHelper(recyclerView);
  }

  private boolean isPositionHeader(int position) {
    return position == 0;
  }

  private boolean isPositionFooter(int position) {
    return position == getItemCount() - 1;
  }

  @Override
  public final int getItemViewType(int position) {
    if (isPositionHeader(position)) {
      return R.id.listitem_header;
    } else if (isPositionFooter(position)) {
      return R.id.listitem_footer;
    }
    return determineViewType(getItem(position - 1));
  }

  // Should return 1 or higher
  protected int determineViewType(DO item) {
    return R.id.listitem_normal;
  }

  @Override
  public final void onBindViewHolder(RecyclerView.ViewHolder holder, int i) {
    if (isPositionHeader(i)) {
      ((GenericBindingHolder) holder).update();
    }
    else if (isPositionFooter(i)) {
      ((GenericBindingHolder) holder).update();
    }
    else {
      ((VH) holder).update(getItem(i - 1));
    }
  }

  @Override
  public final RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    // Need to use `if` instead of `switch`. See http://stackoverflow.com/questions/8476912/menu-item-ids-in-an-android-library-project
    if (viewType == R.id.listitem_header) {
      return onCreateHeaderHolder(parent);
    }
    else if (viewType == R.id.listitem_footer) {
      return onCreateFooterHolder(parent);
    }
    else {
      return onCreateItemHolder(parent, viewType);
    }
  }

  protected abstract VH onCreateItemHolder(ViewGroup parent, int viewType);

  protected RecyclerView.ViewHolder onCreateHeaderHolder(ViewGroup parent) {
    return new BlankGenericItemHolder(parent);
  }

  protected RecyclerView.ViewHolder onCreateFooterHolder(ViewGroup parent) {
    return  new BlankGenericItemHolder(parent);
  }

  @Override
  public int getItemCount() {
    return objects.size() + 2;  // Header and footer
  }

//  /**
//   * @see android.widget.ListAdapter#getItemId(int)
//   */
//  @Override
//  public long getItemId(int position) {
////    if (mDataValid && mCursor != null) {
////      if (mCursor.moveToPosition(position)) {
////        return mCursor.getLong(mRowIDColumn);
////      } else {
////        return 0;
////      }
////    } else {
////      return 0;
////    }
//    return objects.get(position);
//  }

  public DO getItem(int position) {
    return objects.get(position);
  }



  public static abstract class GenericBindingHolder extends AbstractBindingHolder {
    public GenericBindingHolder(ViewGroup layout, boolean selectable) {
      super(layout, selectable);
    }

    protected abstract void update();
  }

  public static class BlankGenericItemHolder extends GenericBindingHolder {
    public BlankGenericItemHolder(ViewGroup parent) {
      super(inflate(parent, R.layout.blank), false);
    }

    @Override
    protected void update() {

    }
  }
}