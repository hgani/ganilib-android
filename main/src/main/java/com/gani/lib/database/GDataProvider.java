package com.gani.lib.database;



import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import java.util.LinkedHashSet;

// This is the ContentProvider for this entire application. Tables are handled by XyzCrud classes.
public abstract class GDataProvider extends ContentProvider {
  public static final String COMMON_PAGE = "page";
  private static final LinkedHashSet<DataUris> REGISTERED_TABLE_URIS = new LinkedHashSet<>();
  private static final LinkedHashSet<GDbTable> REGISTERED_TABLES = new LinkedHashSet<>();

  public static final GDbTable[] registeredTables() {
    return REGISTERED_TABLES.toArray(new GDbTable[REGISTERED_TABLES.size()]);
  }

  private static final PolymorphicEnablingUriMatcher URI_MATCHER = new PolymorphicEnablingUriMatcher();

  protected abstract String authorityName();
  protected abstract void registerTables();
  
  @Override
  public final boolean onCreate() {
    DataUris.setAuthorityName(authorityName());
    registerTables();
    return true;
  }
  
  @Override
  public final String getType(Uri uri) {
    return URI_MATCHER.translateUri(uri).getType(uri);
  }
  
  @Override
  public final Uri insert(Uri uri, ContentValues values) {
    return URI_MATCHER.translateUri(uri).insert(uri, values);
  }
  
  @Override
  public final Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
    return URI_MATCHER.translateUri(uri).query(uri, projection, selection, selectionArgs, sortOrder);
  }
  
  @Override
  public final int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
    return URI_MATCHER.translateUri(uri).update(uri, values, selection, selectionArgs);
  }

  @Override
  public final int delete(Uri uri, String selection, String[] selectionArgs) {
    return URI_MATCHER.translateUri(uri).delete(uri, selection, selectionArgs);
  }
  
  @Override
  public final int bulkInsert(Uri uri, ContentValues[] values) {
    return URI_MATCHER.translateUri(uri).bulkInsert(uri, values);
  }
//
//  public static void register(DataUris dataUris, AbstractCollectionCrud collectionCrud, AbstractSingleCrud singleCrud) {
//    if (REGISTERED_TABLE_URIS.contains(dataUris)) {
//      throw new IllegalArgumentException("Duplicate DataUris: " + dataUris.getCollectionPath());
//    }
//    REGISTERED_TABLE_URIS.add(dataUris);
//
//    URI_MATCHER.registerUri(dataUris.getCollectionPath(), collectionCrud);
//    if (singleCrud != null) {
//      URI_MATCHER.registerUri(dataUris.getSinglePathPattern(), singleCrud);
//    }
//  }

  public static void register(GDbTable table, AbstractCollectionCrud collectionCrud, AbstractSingleCrud singleCrud) {
    DataUris dataUris = table.getDataUris();
    if (REGISTERED_TABLE_URIS.contains(dataUris)) {
      throw new IllegalArgumentException("Duplicate DataUris: " + dataUris.getCollectionPath());
    }
    REGISTERED_TABLE_URIS.add(dataUris);
    REGISTERED_TABLES.add(table);

    URI_MATCHER.registerUri(dataUris.getCollectionPath(), collectionCrud);
    if (singleCrud != null) {
      URI_MATCHER.registerUri(dataUris.getSinglePathPattern(), singleCrud);
    }
  }

  public static void register(GDbTable table) {
    register(table, new GenericCollectionCrud(table), new GenericSingleCrud(table));
//    if (REGISTERED_TABLE_URIS.contains(dataUris)) {
//      throw new IllegalArgumentException("Duplicate DataUris: " + dataUris.getCollectionPath());
//    }
//    REGISTERED_TABLE_URIS.add(dataUris);
//
//    URI_MATCHER.registerUri(dataUris.getCollectionPath(), new GenericCollectionCrud(table));
//    URI_MATCHER.registerUri(dataUris.getSinglePathPattern(), new GenericSingleCrud(table));
  }

//
//  public static void register(DataUris dataUris, GDbTable table) {
//    if (REGISTERED_TABLE_URIS.contains(dataUris)) {
//      throw new IllegalArgumentException("Duplicate DataUris: " + dataUris.getCollectionPath());
//    }
//    REGISTERED_TABLE_URIS.add(dataUris);
//
//    URI_MATCHER.registerUri(dataUris.getCollectionPath(), new GenericCollectionCrud(table));
//    URI_MATCHER.registerUri(dataUris.getSinglePathPattern(), new GenericSingleCrud(table));
//  }

//  private static void register(DataUris dataUris, AbstractCollectionCrud collectionCrud) {
//    register(dataUris, collectionCrud, null);
//  }


  public static final class KeyValue extends GDbTable {
    public static final String TABLE = "key_value";

    public static final String COLUMN_KEY = "data_key";
    public static final String COLUMN_VALUE = "data_value";

    public static final DataUris URIS = new DataUris("key_values");

    public static final KeyValue TABLE_HELPER = new KeyValue();

    @Override
    protected String columnsSpec() {
      return COLUMN_KEY + " text," +
          COLUMN_VALUE + " text";  // json
    }

    @Override
    protected GDbCursor createCursor(Cursor cursor) {
      return new GDbCursor(cursor);
    }
  }
}
