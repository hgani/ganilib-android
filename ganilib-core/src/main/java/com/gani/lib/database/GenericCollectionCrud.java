package com.gani.lib.database;

import android.content.ContentValues;
import android.net.Uri;

public class GenericCollectionCrud extends AbstractCollectionCrud {
  private GDbTable table;

  protected GenericCollectionCrud(GDbTable table) {
    super(table.getClass());

    this.table = table;
  }

  @Override
  public String getType(Uri uri) {
    return "vnd.android.cursor.dir/vnd." + DataUris.getAuthorityName() + "." + table.getName();
  }

  @Override
  protected String table() {
    return table.getName();
  }

  @Override
  protected Uri singleUriFrom(long id, ContentValues contentValues) {
    return table.getDataUris().getSingleContentUri(id);
  }
}
