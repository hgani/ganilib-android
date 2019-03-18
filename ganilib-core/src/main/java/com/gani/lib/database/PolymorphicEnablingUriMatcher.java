package com.gani.lib.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import java.util.HashMap;
import java.util.Map;

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



  interface CrudHandler {
    
    String getType(Uri uri);
    
    Uri insert(Uri uri, ContentValues values);
    
    Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder);
    
    int update(Uri uri, ContentValues values, String selection, String[] selectionArgs);

    int delete(Uri uri, String selection, String[] selectionArgs);
    
    int bulkInsert(Uri uri, ContentValues[] valuesArray);

  }
}
