package com.gani.lib.select;

import com.gani.lib.http.HttpAsyncGet;
import com.gani.lib.io.ResourceCloser;
import com.gani.lib.ui.list.DbCursorRecyclerAdapter;

public abstract class DtoControllerItemSelect<I extends SelectableItem, T extends SelectGroup.Tab> {
  private DbCursorRecyclerAdapter recyclerAdapter;
  private HttpAsyncGet completeDataGet;

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
