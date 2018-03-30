//package com.gani.web.htmlform;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//
//import com.gani.lib.screen.GActivity;
//import com.gani.lib.screen.GFragment;
//import com.gani.lib.ui.Ui;
//import com.gani.web.PathSpec;
//
//public class HtmlFormScreenHelper {
//  public static final String FORM_PATH = "FORM_PATH";
//  public static final String SUBMIT_LISTENER = "SUBMIT_LISTENER";
//
//  public static Intent intent(Class<? extends GActivity> cls, PathSpec spec) {
//    Intent intent = new Intent(Ui.context(), cls);
//    intent.putExtra(FORM_PATH, spec.getPath());
//    intent.putExtra(SUBMIT_LISTENER, spec.getFormListener());
//    return intent;
//  }
//
//
//  private GActivity activity;
//  private HtmlForm form;
//
//  public HtmlFormScreenHelper(GActivity activity, Bundle savedInstanceState) {
//    this.activity = activity;
//
//    onCreate(savedInstanceState);
//  }
//
//  private void onCreate(@Nullable Bundle savedInstanceState) {
////        super.onCreate(savedInstanceState);
////        setContentView(R.layout.sign_up_screen);
////    activity.setContentWithToolbar(R.layout.screen_html_form, true);
//
//    activity.setFragmentWithToolbar(createNewIntentFragment(), false, savedInstanceState);
//
////
////    Intent intent = getIntent();
////    String path = intent.getStringExtra(FORM_PATH);
////    String className = intent.getStringExtra(SUBMIT_LISTENER);
////
////    GBundle bundle = activity.args();
////    String path = bundle.getString(FORM_PATH);
//////    String className = bundle.getString(SUBMIT_LISTENER);
////
////    LinearLayout linearLayout = (LinearLayout) activity.findViewById(R.id.layout);
//////    HTMLForm htmlForm = new HTMLForm(this, linearLayout, App.ENDPOINT_URL + path);
////    this.form = new HTMLForm(activity, linearLayout, GHttp.instance().baseUrl() + path);
////
//////    form.setOnSubmitListener(new HTMLFormOnSubmitListener() {
//////      @Override
//////      public void onSubmit(HTMLForm form) {
//////        HTMLFormScreenHelper.this.onSubmit();
//////      }
//////    });
////
////    form.setOnSubmitListener(((HTMLFormOnSubmitListener) bundle.getSerializable(SUBMIT_LISTENER)));
////
////    form.buildFields();
//  }
//
//  private GFragment createNewIntentFragment() {
//    return new HtmlFormFragment();
//  }
//
//  public HtmlForm getForm() {
//    return form;
//  }
//}
