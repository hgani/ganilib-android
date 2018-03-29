package com.gani.lib.database;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.BaseColumns;

import com.gani.lib.ui.Ui;

import java.util.Map;

public abstract class AbstractSingleCrud implements PolymorphicEnablingUriMatcher.CrudHandler {

  private Map<String, String> projectionMap;

//  // TODO: Remove
//  AbstractSingleCrud() {
//
//  }
//
//  // TODO: Remove. Just select everything.
  AbstractSingleCrud(Class<? extends BaseColumns> tableClass) {
    this.projectionMap = ProjectionUtils.projectionMapFromClass(tableClass);
  }

  // TOOD: Make this final
  protected Map<String, String> projectionMap() {
    return projectionMap;
  }

  @Override
  public final Uri insert(Uri uri, ContentValues values) {
    throw new UnsupportedOperationException();
  }

  @Override
  public final Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
    SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
    queryBuilder.setTables(table());
    // Disable so we just select everything
//    queryBuilder.setProjectionMap(projectionMap());
    queryBuilder.appendWhere(idColumn() + "=" + uriToId(uri));

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
    SQLiteDatabase db = DatabaseInitializer.getWritableDb();

    String finalSelection = idColumn() + "=" + uriToId(uri);
    if (selection !=null) {
      finalSelection = finalSelection + " AND " + selection;
    }

    int count = db.update(table(), values, finalSelection, selectionArgs);
    if (count > 0) {
      Ui.context().getContentResolver().notifyChange(uri, null);
    }
    return count;
  }

  @Override
  public final int delete(Uri uri, String selection, String[] selectionArgs) {
    String finalSelection = idColumn() + "=" + uriToId(uri);
    if (selection != null) {
      finalSelection = finalSelection + " AND " + selection;
    }

    int count = DatabaseInitializer.getWritableDb().delete(table(), finalSelection, selectionArgs);
    if (count > 0) {
      Ui.context().getContentResolver().notifyChange(uri, null);
    }
    return count;
  }
  
  @Override
  public final int bulkInsert(Uri uri, ContentValues[] valuesArray) {
    throw new UnsupportedOperationException();
  }
  
  protected abstract String table();

//  protected abstract Map<String, String> projectionMap();
  
  protected abstract String idColumn();
  
  protected abstract String uriToId(Uri uri);

}
