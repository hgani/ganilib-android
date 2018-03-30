package com.gani.web.htmlform;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.gani.lib.http.GHttp;
import com.gani.lib.model.GBundle;
import com.gani.lib.screen.GActivity;
import com.gani.lib.screen.GFragment;
import com.gani.lib.ui.Ui;
import com.gani.lib.ui.style.Length;
import com.gani.web.PathSpec;
import com.gani.web.R;

public class HtmlFormFragment extends GFragment {
  public static final String FORM_PATH = "form_path";
  public static final String SUBMIT_LISTENER = "submit_listener";

  public static Intent intent(Class<? extends GActivity> cls, PathSpec spec) {
    Intent intent = new Intent(Ui.context(), cls);
    intent.putExtra(FORM_PATH, spec.getPath());
    intent.putExtra(SUBMIT_LISTENER, spec.getFormListener());
    return intent;
  }

  private static final int PADDING = Length.dpToPx(20);
  private HtmlForm form;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View fragmentLayout = inflater.inflate(R.layout.common_fragment, null);

    LinearLayout containerLayout = (LinearLayout) fragmentLayout.findViewById(R.id.container);
    containerLayout.setPadding(PADDING, PADDING, PADDING, PADDING);

    GBundle bundle = args();
    String path = bundle.getString(FORM_PATH);

    this.form = new HtmlForm(this, containerLayout, GHttp.instance().baseUrl() + path);
    form.setOnSubmitListener(((HtmlFormOnSubmitListener) bundle.getSerializable(SUBMIT_LISTENER)));

    return fragmentLayout;
  }

  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    form.buildFields();
  }
}
