package com.gani.web.turbolinks;

import com.gani.lib.http.GImmutableParams;
import com.gani.lib.screen.GActivity;
import com.gani.web.PathSpec;

public abstract class GTurbolinksSupportActivity extends GActivity {
  private GTurbolinks turbolinks;



  ///// Turbolinks /////

  public void registerTurbolinksRestore(GTurbolinks turbolinks) {
    this.turbolinks = turbolinks;
  }

  @Override
  protected void onRestart() {
    super.onRestart();

    if (turbolinks != null) {
      turbolinks.restore();
    }
  }

  public abstract void startTurbolinksScreen(PathSpec pathSpec, GImmutableParams params);
//  {
//    // Override for TL support
//  }

  /////
}
