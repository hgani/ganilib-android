package com.gani.web.turbolinks;

import android.content.Intent;
import android.os.Bundle;

import com.basecamp.turbolinks.TurbolinksView;
import com.gani.lib.http.GImmutableParams;
import com.gani.lib.screen.GActivity;
import com.gani.lib.ui.Ui;
import com.gani.web.PathSpec;
import com.gani.web.R;

public abstract class TurbolinksActivityHelper {
  public static final String EXTRA_PATH_SPEC = "specPath";
  public static final String EXTRA_PARAMS = "params";

  public static Intent intent(Class cls, PathSpec pathSpec, GImmutableParams params) {
    Intent intent = new Intent(Ui.context(), cls);
    intent.putExtra(EXTRA_PATH_SPEC, pathSpec);
    intent.putExtra(EXTRA_PARAMS, params);
    return intent;
  }

  private GTurbolinksSupportActivity activity;
  private GTurbolinks turbolinks;
  private FileUploadSupport fileUploadSupport;

  public TurbolinksActivityHelper(GTurbolinksSupportActivity activity, Bundle savedInstanceState) {
    this.activity = activity;

    onCreate(savedInstanceState);
  }

  private void onCreate(Bundle savedInstanceState) {
    // Can't use args() before super.onCreateForScreen()
    PathSpec pathSpec = (PathSpec) activity.getIntent().getSerializableExtra(EXTRA_PATH_SPEC);
    String title = pathSpec.getTitle();
    if (title != null) {
      // For some reason, for dialog activity, this has to be before super.onCreateForScreen()
      activity.setTitle(title);
    }

//    super.onCreate(savedInstanceState);

    initViewContent();

    this.turbolinks = createTurbolinks(activity, ((TurbolinksView) activity.findViewById(R.id.turbolinks_view)),
        pathSpec.getPath() + "?" + activity.args().getParams(EXTRA_PARAMS).asQueryString());
    this.fileUploadSupport = new FileUploadSupport(activity, turbolinks.getWebView());

    fileUploadSupport.register();
    turbolinks.visit();
  }

  protected abstract GTurbolinks createTurbolinks(GTurbolinksSupportActivity activity, TurbolinksView view, String path);

  public void onActivityResult(int requestCode, int resultCode, Intent intent) {
//    super.onActivityResult(requestCode, resultCode, intent);

    fileUploadSupport.onActivityResult(requestCode, resultCode, intent);
  }

  protected abstract void initViewContent();

  public final void onRestart() {
//    super.onRestart();

    fileUploadSupport.register();  // Reattach webview to this activity.
    turbolinks.restore();
  }

  protected GActivity getActivity() {
    return activity;
  }
}
