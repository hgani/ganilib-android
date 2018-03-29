package com.gani.lib.io;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.gani.lib.http.HttpAsyncGet;
import com.gani.lib.http.HttpAsyncPost;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;

public class ResourceCloser {

  public static void close(BufferedReader bufferedReader) {
    if (bufferedReader != null) {
      try {
        bufferedReader.close();
      }
      catch (IOException e) {
        // Ignore
      }
    }
  }

  public static void close(InputStream instream) {
    if (instream != null) {
      try {
        instream.close();
      }
      catch (IOException e) {
        // Ignore
      }
    }
  }
  
  public static void close(OutputStream outstream) {
    if (outstream != null) {
      try {
        outstream.close();
      }
      catch (IOException e) {
        // Ignore
      }
    }
  }

  public static void close(SQLiteDatabase db) {
    if (db != null) {
      db.close();
    }
  }

  public static void close(Cursor cursor) {
    if (cursor != null) {
      cursor.close();
    }
  }

  public static void close(RandomAccessFile file) {
    if (file != null) {
      try {
        file.close();
      }
      catch (IOException e) {
        // Ignore
      }
    }
  }
  
  public static void close(HttpURLConnection connection) {
    if (connection != null) {
      try {
        connection.disconnect();
      }
      catch (Exception e) {
        // Ignore
      }
    }
  }
  
  public static void cancel(HttpAsyncGet get) {
    if (get != null) {
      try {
        get.cancel();
      }
      catch (Exception e) {
        // Ignore
      }
    }
  }
  
  public static void cancel(HttpAsyncPost post) {
    if (post != null) {
      try {
        post.cancel();
      }
      catch (Exception e) {
        // Ignore
      }
    }
  }

}
