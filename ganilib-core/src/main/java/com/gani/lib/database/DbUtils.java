package com.gani.lib.database;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

import com.gani.lib.ui.Ui;

import java.util.ArrayList;
import java.util.List;

public class DbUtils {
  private static final ContentResolver resolver() {
    return Ui.context().getContentResolver();
  }

  public static final Long getNullableLong(Cursor cursor, int columnIndex) {
    return cursor.isNull(columnIndex)? null : cursor.getLong(columnIndex);
  }
  
  public static final Integer getNullableInteger(Cursor cursor, int columnIndex) {
    return cursor.isNull(columnIndex)? null : cursor.getInt(columnIndex);
  }

  public static final boolean getBoolean(Cursor cursor, int columnIndex) {
  	return cursor.getInt(columnIndex) > 0;
  }
  
  public static final boolean getBoolean(Cursor cursor, String columnName) {
    return getBoolean(cursor, cursor.getColumnIndex(columnName));
  }
  
  public static final boolean isNullOrEmptyString(Cursor cursor, int columnIndex) {
    if (cursor.isNull(columnIndex)) {
      return true;
    }
    
    return cursor.getString(columnIndex).trim().isEmpty();
  }
  
  public static final String getWhereTemplate(String[] whereColumns) {
    String whereTemplate = "";
    if (whereColumns != null) {
      for (String column : whereColumns) {
        whereTemplate += (whereTemplate == "") ? "" : " and ";
        whereTemplate += column + "=?";
      }
    }
    return whereTemplate;
  }
  
  public static final String[] getWhereParams(Object[] whereArgs) {
    List<String> selectionArgList = null;
    if (whereArgs != null) {
      selectionArgList = new ArrayList<>(whereArgs.length);
      for (Object selectionArg : whereArgs) {
        if(selectionArg != null) {
          selectionArgList.add(selectionArg.toString());
        }
      }
      return selectionArgList.toArray(new String[selectionArgList.size()]);
    }
    return null;
  }

  public static final int deleteRows(DataUris dataUris, String[] whereColumns, String[] whereValues) {
    return resolver().delete(dataUris.getCollectionContentUri(), getWhereTemplate(whereColumns), whereValues);
  }

  public static final int deleteRows(DataUris dataUris) {
    return deleteRows(dataUris, null, null);
  }

  public static final int deleteRows(Uri uri) {
    return resolver().delete(uri, null, null);
  }

  public static final boolean exists(DataUris dataUris, long id) {
    return countRows(dataUris, new String[] { BaseColumns._ID }, new String[] { String.valueOf(id) }) > 0;
  }

//  public static final int deleteRow(DataUris dataUris, Long id) {
//    return resolver().delete(GDataProvider.NotificationSummary.URIS.getSingleContentUri(id), null, null);
//  }

  public static final int insertRows(DataUris dataUris, ContentValues[] contentValuesArray) {
    return resolver().bulkInsert(dataUris.getCollectionContentUri(), contentValuesArray);
  }

  public static final int insertRows(Uri uri, GDbRow[] dataArray) {
    ContentValues[] valuesArray = new ContentValues[dataArray.length];
    int index = 0;
    for (GDbRow data : dataArray) {
      valuesArray[index++] = data.toContentValues();
    }
    return resolver().bulkInsert(uri, valuesArray);
  }
  
  public static final int insertRows(DataUris dataUris, GDbRow[] dataArray) {
//    ContentValues[] valuesArray = new ContentValues[dataArray.length];
//    int index = 0;
//    for (GDbRow data : dataArray) {
//      valuesArray[index++] = data.toContentValues();
//    }
//    return resolver().bulkInsert(dataUris.getCollectionContentUri(), valuesArray);
    return insertRows(dataUris.getCollectionContentUri(), dataArray);
  }

  public static final int insertRows(DataUris dataUris, List<GDbRow> dataList) {
    return insertRows(dataUris, dataList.toArray(new GDbRow[dataList.size()]));
  }

  public static final int countRows(DataUris dataUris, String[] whereColumns, String[] whereValues) {
    Cursor cursor = resolver().query(dataUris.getCollectionContentUri(),
        null, getWhereTemplate(whereColumns), whereValues, null);
    int rowCount = cursor.getCount();
    cursor.close();
    return rowCount;
  }

  public static final int countRows(DataUris dataUris, String selection, String[] whereValues) {
    Cursor cursor = resolver().query(dataUris.getCollectionContentUri(), null, selection, whereValues, null);
    int rowCount = cursor.getCount();
    cursor.close();
    return rowCount;
  }

  public static final int countRows(DataUris dataUris) {
    return countRows(dataUris, (String) null, null);
  }
  
  // NOTE: Always update using single row URI to ensure single row observer gets notified.
  public static final int updateRow(Uri uri, GDbRow row) {
    return resolver().update(uri, row.toContentValues(), null, null);
  }

  public static final int updateRow(Uri uri, GDbRow row, String[] whereColumns, String[] whereValues) {
    return resolver().update(uri, row.toContentValues(), getWhereTemplate(whereColumns), whereValues);
  }
  
  static final void registerObserver(DataUris dataUris, ContentObserver observer) {
    resolver().registerContentObserver(dataUris.getCollectionContentUri(), true, observer);
  }

  static final void unregisterObserver(ContentObserver observer) {
    resolver().unregisterContentObserver(observer);
  }

  public static final void query(DataUris uris, String[] projections, String[] whereColumns, String[] whereValues, CursorProcessor processor) {
     Cursor cursor = null;
     try {
        cursor = resolver().query(uris.getCollectionContentUri(), projections, getWhereTemplate(whereColumns), whereValues, null);
        processor.process(cursor);
     } finally {
      if (cursor != null && !cursor.isClosed()) {
         cursor.close();
      }
    }
  }
  
  public interface CursorProcessor {
    void process(Cursor cursor);
  }
  
  public static final int countRows(Uri uri, String selectionClause) {
    // TODO
    // Consider use this statement:
    // DatabaseUtils.queryNumEntries(DatabaseHelper.getReadableDb(), table());
     Cursor cursor = null;
     try {
        cursor = Ui.context().getContentResolver().query(
              uri,
              new String[] { "count(*) as count" },
              selectionClause,
              null,
              null);
        cursor.moveToFirst();
        return cursor.getInt(0);
     } finally {
        if (cursor != null && !cursor.isClosed()) {
           cursor.close();
        }
     }
  }
  
  public static final int maxValueAtField(Uri uri, String fieldName, String selectionClause) {
     Cursor cursor = null;
     try {
        cursor = Ui.context().getContentResolver().query(
              uri,
              new String[] { "max(" + fieldName + ") as max"},
              selectionClause,
              null,
              null);
        cursor.moveToFirst();
        return cursor.getInt(0);
     } finally {
        if (cursor != null && !cursor.isClosed()) {
           cursor.close();
      }
    }
  }
  
  public static final GDbCursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
    return new GDbCursor(resolver().query(uri, projection, selection, selectionArgs, sortOrder));
  }

  public static final GDbCursor query(DataUris uris, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
    return new GDbCursor(resolver().query(uris.getCollectionContentUri(), projection, selection, selectionArgs, sortOrder));
  }
}
