package com.gani.lib.database;

import static com.gani.lib.database.GDataProvider.KeyValue.COLUMN_KEY;
import static com.gani.lib.database.GDataProvider.KeyValue.COLUMN_VALUE;

class DbKeyValue extends GDbModel {
  public DbKeyValue(String key, Object value) {
    GDbRow data = initRow();
    data.put(COLUMN_KEY, key);
    data.put(COLUMN_VALUE, value);
  }

  @Override
  protected GDbRow createRow() {
    return new GDbRow();
  }
}
