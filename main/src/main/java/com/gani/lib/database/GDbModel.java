package com.gani.lib.database;

import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;

import java.util.List;

// TODO: Deprecate. For DB stuff, use DbTable
public abstract class GDbModel<D extends GDbData, R extends GDbRow & GDbData> {
  private Long id;
  private R data;

  // Useful for value objects.
  protected GDbRow initRow() {
    if (data != null) {
      throw new IllegalStateException("Data has been initialized");
    }
    return (data = createRow());
  }

  protected abstract R createRow();

  protected GDbRow initRow(Long id) {
    this.id = id;
    return initRow().put(BaseColumns._ID, id);
  }

//  protected GDbRow initRow(Long id, GDbRow initialData) {
//    this.id = id;
//    initRow();
//    for (Map.Entry<String, Object> entry : initialData.entries()) {
//      data.put(entry.getKey(), entry.getValue());
//    }
//    return data;
//  }
  
  public final Long getId() {
    return id;
  }

  protected final GDbRow getDbRow() {
    if (data == null) {
      throw new IllegalStateException("Should call initRow() first");
    }
    return data;
  }

  public final R toDbRow() {
    if (data == null) {
      throw new IllegalStateException("Should call initRow() first");
    }
    // TODO: Either:
    // 1. Clone
    // 2. Return immutable object. See toDbData()
    // 3. Refactor so this method is not needed.
    return data;
  }

  // Return the immutable type of the mutable object.
  public final D toDbData() {
    if (data == null) {
      throw new IllegalStateException("Should call initRow() first");
    }
    return (D) data;
  }

//  public final boolean getBoolean(String column) {
//    return toDbRow().getBoolean(column);
//  }

  public void insert(DataUris dataUris) {
    insertRows(dataUris.getCollectionContentUri(), this);
  }
  
  public void update(DataUris dataUris) {
    updateRow(dataUris.getSingleContentUri(getId()), this);
  }
  
  public boolean exists(DataUris dataUris) {
    return countRows(dataUris, 
        new String[] { BaseColumns._ID }, 
        new String[] { String.valueOf(getId()) }) > 0;
  }
  
  public void insertOrUpdate(DataUris dataUris) {
    SQLiteDatabase database = DatabaseInitializer.getWritableDb();
    database.beginTransaction();
    try {
      if (exists(dataUris)) {
        update(dataUris);
      }
      else {
        insert(dataUris);
      }
    }
    finally {
      database.endTransaction();
    }
  }

  protected static final int insertRows(Uri uri, GDbModel model) {
    return insertRows(uri, new GDbModel[] { model });
  }

  protected static final int insertRows(Uri uri, List<? extends GDbModel> modelList) {
    return insertRows(uri, modelList.toArray(new GDbModel[modelList.size()]));
  }

  protected static final int insertRows(DataUris dataUris, List<? extends GDbModel> modelList) {
    return insertRows(dataUris.getCollectionContentUri(), modelList.toArray(new GDbModel[modelList.size()]));
  }

  protected static final int insertRows(Uri uri, GDbModel[] modelArray) {
    GDbRow[] dataArray = new GDbRow[modelArray.length];
    int index = 0;
    for (GDbModel model : modelArray) {
      dataArray[index++] = model.toDbRow();
    }
    return DbUtils.insertRows(uri, dataArray);
  }

  protected static void deleteRows(Uri uri) {
    DbUtils.deleteRows(uri);
  }

  protected static void deleteRows(DataUris dataUris) {
    DbUtils.deleteRows(dataUris.getCollectionContentUri());
  }

  protected static int countRows(DataUris dataUris) {
    return DbUtils.countRows(dataUris);
  }

  protected static int countRows(DataUris dataUris, String[] whereColumns, String[] whereValues) {
    return DbUtils.countRows(dataUris, whereColumns, whereValues);
  }

  protected static int updateRow(Uri uri, GDbModel model) {
    return DbUtils.updateRow(uri, model.toDbRow());
  }
}