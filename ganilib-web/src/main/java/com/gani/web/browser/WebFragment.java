package com.gani.web.browser;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.gani.lib.screen.GFragment;
import com.gani.lib.ui.view.GWebView;
import com.gani.web.R;

public class WebFragment extends GFragment {
  public static final String ARG_URL = "url";

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View fragmentLayout = inflater.inflate(R.layout.common_fragment, null);

    LinearLayout matchParentLayout = (LinearLayout) fragmentLayout.findViewById(R.id.container);

    matchParentLayout.addView(new GWebView(getContext(), this).load(args().getString(ARG_URL)));

    return fragmentLayout;
  }

  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    disableRefreshPull();
  }
}
