package com.gani.lib.database;

import android.database.Cursor;
import android.net.Uri;

import com.gani.lib.ui.Ui;

public class Incrementer implements DbOrder {

  private int value;
  
  public Incrementer() {
    value = 0;
  }
  
  public Incrementer(Uri collectionUri, String orderColumn) {
    Cursor cursor = Ui.context().getContentResolver().query(
        collectionUri,
        new String[] { "max(" + orderColumn + ") as max_val" },
        null,
        null,
        null);
    cursor.moveToFirst();
    value = cursor.getInt(0);
    cursor.close();
  }

  public Incrementer(GDbTable table) {
    this(table.getDataUris().getCollectionContentUri(), table.getOrderColumn());
  }

  @Override
  public int nextVal() {
    return ++value;
  }
}
