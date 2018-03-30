package com.gani.lib.dialog;

import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.TextView;

import com.gani.lib.R;
import com.gani.lib.logging.GLog;
import com.gani.lib.model.GBundle;
import com.gani.lib.screen.GActivity;
import com.gani.lib.ui.ProgressIndicator;

/**
 * This fake dialog knows how to re-draw itself correctly on reorientation and should be configured not to be destroyed and re-created
 * on reorientation.
 * To use properly, the Activity should have the following setting in AndroidManifest.xml:
 * <br />
 * <pre>android:configChanges="orientation|screenSize"</pre>
 */
public abstract class GDialogProgress extends GActivity implements ProgressIndicator {
  private boolean completed = false;
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreateForDialog(savedInstanceState);
    setupViews();
  }

  @Override
  public final void onConfigurationChanged(Configuration newConfig) {
    super.onConfigurationChanged(newConfig);
    setupViews();
  }
  
  @Override
  protected void onDestroy() {
    if (isFinishing()) {
      if (!completed) {
        onCancel();
      }
    }
    super.onDestroy();
  }

  private final void setupViews() {
    setContentForDialog(contentView());

    setTitle(title());

    String text = text();
    if (text != null) {
      setText(text);
    }
  }
  
  protected int contentView() {
    return R.layout.dialog_progress;
  }
  
//  protected int title() {
//    return R.string.title_progress;
//  }

  protected String title() { return null; }
  protected String text() {
    return null;
  }

  protected void onCancel() {
    // Do nothing by default
  }

  // TODO: Consider overriding finish() to throw an exception because there shouldn't be any
  // reason not to use this method.
  protected final void finishDueToCompletion() {
    completed = true;
    finish();
  }

  @Override
  public void showProgress() {
    // Nothing to do since this dialog is a progess indicator itself.
  }

  @Override
  public void hideProgress() {
    finishDueToCompletion();
  }

  private void setText(String text) {
    ((TextView) findViewById(R.id.message)).setText(text);
  }
}
