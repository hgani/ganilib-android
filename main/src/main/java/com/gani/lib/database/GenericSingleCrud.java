package com.gani.lib.database;


import android.net.Uri;

public class GenericSingleCrud extends AbstractSingleCrud {
  private GDbTable table;

  protected GenericSingleCrud(GDbTable table) {
    super(table.getClass());

    this.table = table;
  }

  @Override
  public String getType(Uri uri) {
    return "vnd.android.cursor.item/vnd." + DataUris.getAuthorityName() + "." + table.getName();
  }

  @Override
  protected String table() {
    return table.getName();
  }

  @Override
  protected String idColumn() {
    return table._ID;
  }

  @Override
  protected String uriToId(Uri uri) {
//    String positionColumn = null;
    int position = 1;  // Use default if not specified.
    try {
      position = table.getClass().getDeclaredField("ID_POSITION_IN_URI").getInt(null);
    } catch (NoSuchFieldException e) {
      // Let the field be null

      // Make the field mandatory
      //throw new RuntimeException(e);
    } catch (IllegalAccessException e) {
//      throw new RuntimeException(e);
    }
//    return uri.getPathSegments().get(GDataProvider.Conversation.ID_POSITION_IN_URI);
//    return uri.getPathSegments().get(1);
    return uri.getPathSegments().get(position);
  }
}
