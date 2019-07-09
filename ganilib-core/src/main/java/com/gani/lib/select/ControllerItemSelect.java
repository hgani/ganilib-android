package com.gani.lib.select;

import android.database.Cursor;

import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import com.gani.lib.http.HttpAsyncGet;
import com.gani.lib.io.ResourceCloser;
import com.gani.lib.ui.list.DbCursorRecyclerAdapter;

public abstract class ControllerItemSelect<I extends SelectableItem, T extends SelectGroup.Tab> implements LoaderManager.LoaderCallbacks<Cursor> {
  private DbCursorRecyclerAdapter recyclerAdapter;
  private HttpAsyncGet completeDataGet;

  @Override
  public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
    recyclerAdapter.swapCursor(cursor);
  }

  @Override
  public void onLoaderReset(Loader<Cursor> loader) {
    recyclerAdapter.swapCursor(null);
  }
  
  public void onDestroy() {
    ResourceCloser.cancel(completeDataGet);
  }
  
  final void asyncRetrieveDataIfNecessary() {
    HttpAsyncGet request = createRequestIfNecessary();
    if (request != null) {
      ResourceCloser.cancel(completeDataGet);
      
      completeDataGet = request.execute();
    }
  }
  
  protected abstract void setSelectGroup(SelectGroup group);
  protected abstract HttpAsyncGet createRequestIfNecessary();

  protected void initListAdapter(DbCursorRecyclerAdapter recyclerAdapter) {
    this.recyclerAdapter = recyclerAdapter;
  }
}
