package com.gani.lib.database;

import android.database.ContentObserver;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;

import androidx.loader.content.CursorLoader;

import com.gani.lib.ui.Ui;

import java.util.LinkedList;
import java.util.List;

public abstract class GDbTable<C extends GDbCursor> implements BaseColumns {
//  // TODO: Remove. Use getCollectionLoader() without argument instead.
//  // a solution for common/shared structure, such as PostView which gets used in multiple screens/tables.
//  // Uri is only needed for multi-screen model such as Post and PostView.
  public CursorLoader getLoader(Uri uri) {
    return getLoader(uri, null, null);
  }

  public CursorLoader getLoader(Uri uri, String[] whereColumns, Object[] whereArgs) {
    return new CursorLoader(
        Ui.context(),
        uri,
        null,
        DbUtils.getWhereTemplate(whereColumns),
        DbUtils.getWhereParams(whereArgs),
        getOrderColumn());
  }

  public DataUris getDataUris() {
    try {
      return (DataUris) getClass().getDeclaredField("URIS").get(null);
    } catch (NoSuchFieldException e) {
      throw new RuntimeException(e);
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  public String getOrderColumn() {
    String orderColumn = null;
    try {
      orderColumn = (String) getClass().getDeclaredField("COLUMN_ORDER").get(null);
    } catch (NoSuchFieldException e) {
      // Let the field be null

      // Make the field mandatory
      //throw new RuntimeException(e);
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    }
    return orderColumn;
  }

  public String getName() {
    try {
      return (String) getClass().getDeclaredField("TABLE").get(null);
    } catch (NoSuchFieldException e) {
      throw new RuntimeException(e);
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  private Uri getCollectionUri() {
    return getDataUris().getCollectionContentUri();
  }

  private Uri getSingleUri(long id) {
    return getDataUris().getSingleContentUri(id);
  }

  public int insertRows(List<? extends GDbModel> dataArray) {
    return insertRows(getDataUris().getCollectionContentUri(), dataArray.toArray(new GDbModel[dataArray.size()]));
  }

  public int insertRows(GDbModel... dataArray) {
    return insertRows(getDataUris().getCollectionContentUri(), dataArray);
  }

  protected static final int insertRows(Uri uri, GDbModel[] modelArray) {
    GDbRow[] dataArray = new GDbRow[modelArray.length];
    int index = 0;
    for (GDbModel model : modelArray) {
      dataArray[index++] = model.toDbRow();
    }
    return DbUtils.insertRows(uri, dataArray);
  }

  public void updateRows(List<? extends GDbModel> dataArray){
    updateRows(getDataUris().getCollectionContentUri(), dataArray.toArray(new GDbModel[dataArray.size()]));
  }

  public void updateRows(Uri uri, GDbModel[] modelArray){

    LinkedList<GDbRow> dbRows= new LinkedList<GDbRow>();
    for(GDbModel model : modelArray){
      //
      if(DbUtils.updateRow(uri, model.toDbRow()) == -1){
        dbRows.add(model.toDbRow());
      }
      else{DbUtils.updateRow(uri, model.toDbRow());}
    }

    GDbRow[] dataArray = new GDbRow[dbRows.size()];
    int index = 0;
    for(GDbRow row : dbRows){
      dataArray[index++] = row;
    }

    DbUtils.insertRows(uri, dataArray);
  }

  public void updateRow(GDbModel model, String[] whereColumns, String[] whereValues) {
    DbUtils.updateRow(getDataUris().getCollectionContentUri(), model.toDbRow(), whereColumns, whereValues);
  }

  public CursorLoader getLoader(String[] whereColumns, Object[] whereArgs) {
    return getLoader(getCollectionUri(), whereColumns, whereArgs);
  }

  public CursorLoader getLoader() {
    return getLoader(getCollectionUri());
  }

  public CursorLoader getLoader(long id) {
    return getLoader(getSingleUri(id), null, null);
  }

  public C query(long id) {
    return query(getDataUris().getSingleContentUri(id));
  }

  public C query(Uri uri) {
    return query(uri, null, null);
  }

  public C query(Uri uri, String[] whereColumns, Object[] whereArgs) {
    String orderColumn = null;
    try {
      orderColumn = (String) getClass().getDeclaredField("COLUMN_ORDER").get(null);
    } catch (NoSuchFieldException e) {
      // Let the field be null

      // Make the field mandatory
      //throw new RuntimeException(e);
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    }

    return createCursor(Ui.context().getContentResolver().query(
        uri,
        null,
        DbUtils.getWhereTemplate(whereColumns),
        DbUtils.getWhereParams(whereArgs),
        orderColumn));
  }

  protected abstract C createCursor(Cursor cursor);

  public C query(String[] whereColumns, Object[] whereArgs) {
    return query(getDataUris().getCollectionContentUri(), whereColumns, whereArgs);
  }

  public void deleteRows(String[] whereColumns, Object[] whereArgs) {
    DbUtils.deleteRows(getDataUris(), whereColumns, DbUtils.getWhereParams(whereArgs));
  }

  protected abstract String columnsSpec();

  public void createTable(SQLiteDatabase database) {
//    if (columnsSpec() != null) {  // Consider making this NON-optional
    database.execSQL("create table " + getName() + " (" + columnsSpec() + ");");
//    }
  }

  public void dropTableIfExists(SQLiteDatabase database) {
    database.execSQL("drop table if exists " + getName());
  }

  public void recreateTable(SQLiteDatabase database) {
    dropTableIfExists(database);
    createTable(database);
  }

  public void createIndex(SQLiteDatabase database, String columnName) {
    String tableName = getName();
    database.execSQL("create index " + tableName + "_" + columnName + "_idx on " + tableName + "(" + columnName + ");");
  }

  public void registerObserver(ContentObserver observer) {
    DbUtils.registerObserver(getDataUris(), observer);
  }
//
//  public void unregisterObserver(ContentObserver observer) {
//    DbUtils.unregisterObserver(observer);
//  }

}
