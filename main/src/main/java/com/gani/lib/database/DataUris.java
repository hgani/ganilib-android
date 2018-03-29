package com.gani.lib.database;

import android.net.Uri;

public class DataUris {
//  static final String AUTHORITY = "com." + GDataProvider + ".domain.contentprovider.dataprovider";

  private static String authorityName;
  private static String authority;
  private String tableName;
  private String collectionPath;

  static void setAuthorityName(String name) {
    authorityName = name;
    authority = "com." + name + ".domain.contentprovider.dataprovider";
  }

  static String getAuthority() {
    return authority;
  }

  static String getAuthorityName() {
    return authorityName;
  }
  
  /**
   * @param collectionPathNoSlash no leading slash
   */
  public DataUris(String collectionPathNoSlash, String tableName) {
    this.collectionPath = collectionPathNoSlash;
    this.tableName = (tableName == null) ? determineTableName(collectionPathNoSlash) : tableName;
  }

  public DataUris(String collectionPathNoSlash) {
    this(collectionPathNoSlash, null);
  }

  // TODO: Consider using the same name as the collection name
  private String determineTableName(String str) {
    if (str.length() > 0 && str.charAt(str.length()-1)=='s') {
      str = str.substring(0, str.length()-1);
    }
    return str;
  }
  
  public String getTableName() {
    return tableName;
  }
  
  /**
   * @return no leading slash
   */
  String getCollectionPath() {
    return collectionPath;
  }
  
  /**
   * @return no leading slash
   */
  String getSinglePathPattern() {
    return collectionPath + "/#";
  }
  
  public Uri getCollectionContentUri(long... parentIds) {
    return Uri.parse(String.format("content://%s/%s", getAuthority(), replaceParentsInCollectionPath(parentIds)));
  }
  
  public Uri getSingleContentUri(long id, long... parentIds) {
    return Uri.parse(String.format("content://%s/%s/%s", getAuthority(), replaceParentsInCollectionPath(parentIds), id));
  }
  
  private String replaceParentsInCollectionPath(long... parentIds) {
    String replacedParents = collectionPath;
    for (long parentId : parentIds) {
      replacedParents = replacedParents.replaceFirst("#", String.valueOf(parentId));
    }
    return replacedParents;
  }
  
  @Override
  public int hashCode() {
    return collectionPath.hashCode();
  }
  
  @Override
  public boolean equals(Object o) {
    if (o instanceof DataUris) {
      DataUris other = (DataUris) o;
      return collectionPath == other.collectionPath;
    }
    return false;
  }
}
