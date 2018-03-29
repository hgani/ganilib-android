package com.gani.lib.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.BaseColumns;

import com.gani.lib.ui.Ui;

import java.util.Map;

abstract class AbstractCollectionCrud implements PolymorphicEnablingUriMatcher.CrudHandler {
  private Map<String, String> projectionMap;

//  // TODO: Remove (later when we've migrated to new code)
//  AbstractCollectionCrud() {
//
//  }

  AbstractCollectionCrud(Class<? extends BaseColumns> tableClass) {
    this.projectionMap = ProjectionUtils.projectionMapFromClass(tableClass);
  }

  // TOOD: Make this final (later when we've migrated to new code)
  protected Map<String, String> projectionMap() {
    return projectionMap;
  }

  @Override
  public final Uri insert(Uri uri, ContentValues values) {
    ContentValues vals = new ContentValues(values);
    
    long id = DatabaseInitializer.getWritableDb().insertOrThrow(table(), null, vals);
    Uri singleUri = singleUriFrom(id, values);

    Ui.context().getContentResolver().notifyChange(singleUri, null);
    return singleUri;
  }

  @Override
  public final Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
    SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
    queryBuilder.setTables(table());
    // Disable so we just select everything
//    queryBuilder.setProjectionMap(projectionMap());
    selection = appendParentIdsToSelection(uri, selection);
    
    Cursor cursor = queryBuilder.query(
        DatabaseInitializer.getReadableDb(),
        projection,
        selection,
        selectionArgs,
        null,
        null,
        sortOrder
    );

    cursor.setNotificationUri(Ui.context().getContentResolver(), uri);
    return cursor;
  }

  @Override
  public final int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
    selection = appendParentIdsToSelection(uri, selection);
    int count = DatabaseInitializer.getWritableDb().update(table(), values, selection, selectionArgs);
    if (count > 0) {
      Ui.context().getContentResolver().notifyChange(uri, null);
    }
    return count;
  }

  @Override
  public int delete(Uri uri, String selection, String[] selectionArgs) {
    selection = appendParentIdsToSelection(uri, selection);
    int count = DatabaseInitializer.getWritableDb().delete(table(), selection, selectionArgs);
    if (count > 0) {
      Ui.context().getContentResolver().notifyChange(uri, null);
    }
    return count;
  }

  @Override
  public int bulkInsert(Uri uri, ContentValues[] valuesArray) {
    SQLiteDatabase database = DatabaseInitializer.getWritableDb();
    database.beginTransaction();
    try {
      for (ContentValues contentValues : valuesArray) {
        long id = database.insertOrThrow(table(), null, contentValues);
        if (id <= 0) {
          throw new SQLException("Failed to insert row into " + uri);
        }
      }
      database.setTransactionSuccessful();
      Ui.context().getContentResolver().notifyChange(uri, null);
      return valuesArray.length;
    }
    finally {
      database.endTransaction();
    }
  }
  
  public final long countAll() {
    return DatabaseUtils.queryNumEntries(DatabaseInitializer.getReadableDb(), table());
  }
  
  protected abstract String table();
  
  protected abstract Uri singleUriFrom(long id, ContentValues contentValues);
  
//  protected abstract Map<String, String> projectionMap();
  
  /**
   * Default implementation simply returns <code>selection</code> as only few children need this
   * (only those that have parent IDs in URI)
   */
  protected String appendParentIdsToSelection(Uri uri, String selection) {
    return selection;
  }
}
