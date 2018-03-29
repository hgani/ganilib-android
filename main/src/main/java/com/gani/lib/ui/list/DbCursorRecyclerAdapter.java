package com.gani.lib.ui.list;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.gani.lib.R;
import com.gani.lib.database.GDbCursor;
import com.gani.lib.logging.GLog;
import com.gani.lib.ui.Ui;

public abstract class DbCursorRecyclerAdapter<C extends GDbCursor> extends CursorRecyclerAdapter<RecyclerView.ViewHolder> {
  // Beware not to use IDs that conflict with item IDs (e.g. comments use -1 for pending).
  // See constructor
  private static final int STABLE_HEADER_ID = -1000;
  private static final int STABLE_FOOTER_ID = -1001;

  // Not used at the moment, but can be used in the future to deferentiate INITIAL, SUBSEQUENT, etc.
  public enum State {
    NORMAL
  }

  private State state;

  protected DbCursorRecyclerAdapter() {
    super(null);

    resetState();

    // Avoid web view from flickering when refreshing the list (e.g. when user pulls the refresh icon). See https://code.google.com/p/android/issues/detail?id=177517
    setHasStableIds(true);
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

  protected abstract CursorBindingHolder onCreateItemHolder(ViewGroup parent, int viewType);

  public RecyclerListHelper initForList(RecyclerView recyclerView, boolean withSeparator) {
    Context context = recyclerView.getContext();
    if (withSeparator) {
      recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL_LIST));
    }
    recyclerView.setAdapter(this);
    return new RecyclerListHelper(recyclerView);
  }

  public void initForList(RecyclerView recyclerView) {
    initForList(recyclerView, true);
  }

  public void initForList(RecyclerView.ItemDecoration decoration, RecyclerView recyclerView){
    recyclerView.addItemDecoration(decoration);
    recyclerView.setAdapter(this);
  }

  protected GenericBindingHolder onCreateHeaderHolder(ViewGroup parent) {
    return new BlankGenericItemHolder(parent);
  }

  protected GenericBindingHolder onCreateFooterHolder(ViewGroup parent) {
    GLog.t(getClass(), "onCreateFooterHolder()");
    return new BlankGenericItemHolder(parent);
  }

  // Can be called from any thread
  public void update() {
//    App.runOnUiThread(new Runnable() {
//      @Override
//      public void run() {
//        notifyDataSetChanged();
//      }
//    });
    update(State.NORMAL);
  }

  public void update(final State state) {
    Ui.run(new Runnable() {
      @Override
      public void run() {
        DbCursorRecyclerAdapter.this.state = state;
        notifyDataSetChanged();
      }
    });
  }

  private void resetState() {
    state = State.NORMAL;
  }

  @Override
  public long getItemId(int position) {
    if (isPositionHeader(position)) {
      return STABLE_HEADER_ID;
    }
    else if (isPositionFooter(position)) {
      return STABLE_FOOTER_ID;
    }
    return super.getItemId(position);
  }

  @Override
  public final void onBindViewHolder(RecyclerView.ViewHolder holder, int i) {
    if (isPositionHeader(i)) {
      ((GenericBindingHolder) holder).update(state);
    }
    else if (isPositionFooter(i)) {
      ((GenericBindingHolder) holder).update(state);
    }
    else {
      super.onBindViewHolder(holder, i - 1);
    }
    resetState();
  }

  @Override
  public final void onBindViewHolderCursor(RecyclerView.ViewHolder holder, Cursor cursor) {
//    ((CursorBindingHolder) holder).bind(new GDbCursor(cursor));
    ((CursorBindingHolder) holder).bind(createCursor(cursor));
  }

  protected abstract C createCursor(Cursor c);

  @Override
  public final int getItemCount() {
    return super.getItemCount() + 2;  // Header and footer
  }

  public final int getDataCount() {
    return super.getItemCount();
  }

  @Override
  public final int getItemViewType(int position) {
    if (isPositionHeader(position)) {
      return R.id.listitem_header;
    } else if (isPositionFooter(position)) {
      return R.id.listitem_footer;
    }
//    GDbCursor cursor = new GDbCursor(getCursor());
    C cursor = createCursor(getCursor());
    if (!cursor.moveToPosition(position - 1)) {
      throw new IllegalStateException("Couldn't move cursor to position " + position);
    }
    return determineViewType(cursor);
  }

  // Should return 1 or higher
  protected int determineViewType(C cursor) {
    return R.id.listitem_normal;
  }

  private boolean isPositionHeader(int position) {
    return position == 0;
  }

  private boolean isPositionFooter(int position) {
    return position == super.getItemCount() + 1;
  }

//  private static View inflate(ViewGroup parent, int layoutId) {
//    return LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
//  }



  public static class BlankGenericItemHolder extends GenericBindingHolder {
    public BlankGenericItemHolder(ViewGroup parent) {
      super(inflate(parent, R.layout.blank), false);
    }

    @Override
    protected void update(State state) {
      // Do nothing
    }
  }

  public static class BlankCursorItemHolder<C extends GDbCursor> extends CursorBindingHolder<C> {
    public BlankCursorItemHolder(ViewGroup parent) {
      super(inflate(parent, R.layout.blank), false);
    }

    @Override
    protected void bind(C cursor) {
      // Do nothing
    }
  }

  public static abstract class GenericBindingHolder extends AbstractBindingHolder {
    public GenericBindingHolder(ViewGroup layout, boolean selectable) {
      super(layout, selectable);
    }

    protected abstract void update(State state);
  }

  public static abstract class CursorBindingHolder<T extends GDbCursor> extends AbstractBindingHolder {
    public CursorBindingHolder(ViewGroup layout, boolean selectable) {
      super(layout, selectable);
    }

    protected abstract void bind(T cursor);
  }
}
