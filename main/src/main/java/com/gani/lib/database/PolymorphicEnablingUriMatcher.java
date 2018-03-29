package com.gani.lib.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import java.util.HashMap;
import java.util.Map;

import static android.content.UriMatcher.NO_MATCH;

class PolymorphicEnablingUriMatcher extends NegativeFriendlyUriMatcher {
  private int code = 0;
  private Map<Integer, CrudHandler> codeToCrudHandlerMap = new HashMap<Integer, CrudHandler>();
  
  PolymorphicEnablingUriMatcher() {
    super(NO_MATCH);
  }

  private void registerUri(String authority, String path, CrudHandler crudHandler) {
    addURI(authority, path, ++code);
    codeToCrudHandlerMap.put(code, crudHandler);
  }

  void registerUri(String path, CrudHandler crudHandler) {
    registerUri(DataUris.getAuthority(), path, crudHandler);
  }
  
  CrudHandler translateUri(Uri uri) {
    int code = match(uri);
    return codeToCrudHandlerMap.get(code);
  }



  static interface CrudHandler {
    
    public abstract String getType(Uri uri);
    
    public abstract Uri insert(Uri uri, ContentValues values);
    
    public abstract Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder);
    
    public abstract int update(Uri uri, ContentValues values, String selection, String[] selectionArgs);

    public abstract int delete(Uri uri, String selection, String[] selectionArgs);
    
    public abstract int bulkInsert(Uri uri, ContentValues[] valuesArray);

  }
}
