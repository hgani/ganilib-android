package com.gani.web.turbolinks;

import android.os.Bundle;

import com.gani.web.R;

public abstract class TurbolinksDialogHelper extends TurbolinksActivityHelper {
  public TurbolinksDialogHelper(GTurbolinksSupportActivity activity, Bundle savedInstanceState) {
    super(activity, savedInstanceState);
  }

  @Override
  protected final void initViewContent() {
    getActivity().setContentForDialog(R.layout.screen_inline_turbolinks);
  }
}
