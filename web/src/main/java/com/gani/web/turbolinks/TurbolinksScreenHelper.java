package com.gani.web.turbolinks;

import android.os.Bundle;

import com.gani.web.R;

public abstract class TurbolinksScreenHelper extends TurbolinksActivityHelper {
  public TurbolinksScreenHelper(GTurbolinksSupportActivity activity, Bundle savedInstanceState) {
    super(activity, savedInstanceState);
  }

  @Override
  protected final void initViewContent() {
    getActivity().setContentWithToolbar(R.layout.screen_turbolinks, false);
  }
}
