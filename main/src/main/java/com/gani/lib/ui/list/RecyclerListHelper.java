package com.gani.lib.ui.list;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public class RecyclerListHelper {
  private RecyclerView recyclerView;
  private LinearLayoutManager layoutManager;

  public RecyclerListHelper(RecyclerView recyclerView) {
    this.recyclerView = recyclerView;
    this.layoutManager = new LinearLayoutManager(recyclerView.getContext());

    recyclerView.setLayoutManager(layoutManager);
  }

  public void reverse() {
    layoutManager.setReverseLayout(true);
  }

  public long getLastCompletelyVisibleItemId() {
    int pos = layoutManager.findLastCompletelyVisibleItemPosition();
    return recyclerView.getAdapter().getItemId(pos - 1);  // Subtract header
  }
}
