package com.gani.web;

import android.net.Uri;

import com.gani.lib.logging.GLog;
import com.gani.lib.ui.Ui;
import com.gani.web.htmlform.HtmlFormOnSubmitListener;

import java.io.Serializable;

import javax.microedition.khronos.opengles.GL;

public class PathSpec implements Serializable {
  private String path;
  private String title;
  private HtmlFormOnSubmitListener formListener;

  public PathSpec(String path, String title) {
    this.path = path;
    this.title = title;
  }

  public PathSpec(String path, String title, HtmlFormOnSubmitListener formListener) {
    this(path, title);

    this.formListener = formListener;
  }

  public PathSpec(String path) {
    this(path, null);
  }

  public PathSpec(String path, int titleResId) {
    this(path, Ui.str(titleResId));
  }

  public String getPath() {
    return path;
  }

  public String getTitle() {
    return title;
  }

  public HtmlFormOnSubmitListener getFormListener() {
    return formListener;
  }

  public boolean matches(Uri uri) {
    return path.equals(uri.getPath());
  }

  // Needed for GScreenView's intent equality check.
  @Override
  public boolean equals(Object that) {
    if (that instanceof PathSpec) {
      return this.path.equals(((PathSpec) that).path);
    }
    return false;
  }
}