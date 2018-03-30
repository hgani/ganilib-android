package com.gani.web.htmlform;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gani.lib.screen.GFragment;
import com.gani.lib.ui.layout.VerticalLayout;
import com.gani.lib.ui.style.Length;
import com.gani.web.R;

public abstract class AbstractHtmlFormFragment extends GFragment {
  private static final int PADDING = Length.dpToPx(20);
  private HtmlForm htmlForm;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View fragmentLayout = inflater.inflate(R.layout.common_fragment, null);

    VerticalLayout paddedLayout = new VerticalLayout(getContext()).size(ViewGroup.LayoutParams.MATCH_PARENT, null).padding(20, null, 20, null);
    ((ViewGroup) fragmentLayout.findViewById(R.id.content_layout)).addView(paddedLayout);

    htmlForm = new HtmlForm(this, paddedLayout, getUrl());
    htmlForm.setOnSubmitListener(getOnSubmit());

    return fragmentLayout;
  }

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);

    // TODO: If the activity has been destroyed in the background, we should be reconstructing the previous state as opposed to simply refreshing it.
    onRefresh();
  }

  @Override
  protected void onRefresh() {
    super.onRefresh();

    htmlForm.buildFields();
  }

  protected abstract String getUrl();
  protected abstract HtmlFormOnSubmit getOnSubmit();
}
