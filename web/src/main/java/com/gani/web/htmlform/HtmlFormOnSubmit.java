package com.gani.web.htmlform;

import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import com.gani.lib.http.GHttp;
import com.gani.lib.http.GHttpCallback;
import com.gani.lib.http.GParams;
import com.gani.lib.http.GRestCallback;
import com.gani.lib.http.GRestResponse;
import com.gani.lib.http.HttpAsyncPost;
import com.gani.lib.http.HttpHook;
import com.gani.lib.http.HttpMethod;
import com.gani.lib.json.GJsonObject;
import com.gani.lib.logging.GLog;
import com.gani.lib.screen.GActivity;
import com.gani.lib.ui.view.GButton;
import com.gani.web.PathSpec;
import com.gani.web.htmlform.field.HtmlCheckBox;
import com.gani.web.htmlform.field.HtmlDataList;
import com.gani.web.htmlform.field.HtmlEditText;
import com.gani.web.htmlform.field.HtmlRadioButton;
import com.gani.web.htmlform.field.HtmlSpinner;

import org.json.JSONException;
import org.jsoup.select.Elements;


import java.util.ArrayList;
import java.util.List;

public abstract class HtmlFormOnSubmit implements HtmlFormOnSubmitListener {
  private void storeParams(GParams params, String tag, String value) {
    if (tag instanceof String && value != null) {
      params.put(tag, value);
    }
  }

  private void storeParams(GParams params, String tag, List<String> values) {
    if (tag instanceof String && values != null) {
      params.put(tag, values.toArray(new String[values.size()]));
    }
  }

  public GParams getParams(HtmlForm form) {
    LinearLayout layout = form.getLayout();
    GParams params = GParams.create();

    for (int i = 0; i < form.getLayout().getChildCount(); i++) {
      String tag = (String) layout.getChildAt(i).getTag();
      View field = layout.getChildAt(i);

      if (layout.getChildAt(i) instanceof HtmlEditText) {
        String value = ((HtmlEditText) layout.getChildAt(i)).getText().toString();
        storeParams(params, tag, value);
      } else if (layout.getChildAt(i) instanceof HtmlDataList) {
        String value = ((HtmlDataList) layout.getChildAt(i)).getText().toString();
        storeParams(params, tag, value);
      } else if (layout.getChildAt(i) instanceof HtmlSpinner) {
        String value = ((HtmlSpinner) layout.getChildAt(i)).getSelectedItem().toString();
        storeParams(params, tag, value);
      } else if (layout.getChildAt(i) instanceof RadioGroup) {
        String value;
        int radioButtonId = ((RadioGroup) layout.getChildAt(i)).getCheckedRadioButtonId();

        if (radioButtonId > -1) {
          HtmlRadioButton radioButton = (HtmlRadioButton) layout.findViewById(radioButtonId);
          value = radioButton.getValue();
        } else {
          value = null;
        }

        storeParams(params, tag, value);
      } else if (layout.getChildAt(i) instanceof HtmlCheckBox) {
        Elements checkBoxes = form.getFormElement().getElementsByAttributeValue(HtmlForm.NAME_ATTR, tag);
        checkBoxes = checkBoxes.select(HtmlForm.CHECKBOX_TYPE_QUERY);

        if (checkBoxes.size() > 1) {
          ArrayList<String> values = new ArrayList<>();

          for (int j = 0; j < checkBoxes.size(); j++) {
            HtmlCheckBox htmlCheckBox = (HtmlCheckBox) layout.getChildAt(i + j);

            if (htmlCheckBox.isChecked()) {
              String value = htmlCheckBox.getValue();
              values.add(value);
            }
          }

          i = i + (checkBoxes.size() - 1);
          storeParams(params, tag, values);
        } else {
          HtmlCheckBox htmlCheckBox = (HtmlCheckBox) layout.getChildAt(i);

          if (htmlCheckBox.isChecked()) {
            String value = htmlCheckBox.getValue();
            storeParams(params, tag, value);
          }
        }

      } else if (field instanceof Button) {
        String value = ((GButton) field).getText().toString();
        storeParams(params, tag, value);
      }
    }

    // For Rails
    String token = form.getCsrfToken();
    if (token != null) {
      params.put("authenticity_token", token);
    }

    return params;
  }

  protected abstract Intent createTurbolinksIntent(PathSpec pathSpec);
//  public abstract void execute   (TaRichActivity activity, TaJsonObject params) throws JSONException;


  @Override
  public void afterBuild(HtmlForm form) { }

  protected String transformUrl(String origUrl) {
    return origUrl + ".json";
  }

  @Override
  public void onSubmit(final HtmlForm form) {
    String endpoint = transformUrl(GHttp.instance().baseUrl() + form.getFormElement().attr("action"));

    GParams params = getParams(form);
    GHttpCallback restCallback = new GRestCallback(form.getFragment()) {
      @Override
      protected void onRestResponse(GRestResponse r) throws JSONException {
        super.onRestResponse(r);

        GActivity activity = form.getFragment().getGActivity();
        GJsonObject result = r.getResult();

        GJsonObject handler = result.getNullableObject("handler");
        if (handler != null) {
          String name = handler.getString("name");
          try {
            String prefix = HtmlFormOnSubmit.this.getClass().getPackage().getName() + ".handler";
            HtmlFormHandler.create(prefix, name).execute(activity, handler);
          }
          catch (HtmlFormHandler.NotFoundException e) {
            GLog.e(getClass(), "Handler not found", e);
            throw new JSONException("Handler not found");
          }
        }

        String url = result.getString("visit");

        Uri uri = Uri.parse(url);
        PathSpec path = new PathSpec(uri.getPath());

        activity.startActivity(createTurbolinksIntent(path));
        activity.finish();
      }

//      @Override
//      protected void onRestSuccess(GRestResponse r) throws JSONException {
//        GActivity activity = form.getFragment().getGActivity();
//        GJsonObject result = r.getResult();
//
//        GJsonObject handler = result.getNullableObject("handler");
//        if (handler != null) {
//          String name = handler.getString("name");
//          try {
//            String prefix = HtmlFormOnSubmit.this.getClass().getPackage().getName() + ".handler";
//            HtmlFormHandler.create(prefix, name).execute(activity, handler);
//          }
//          catch (HtmlFormHandler.NotFoundException e) {
//            GLog.e(getClass(), "Handler not found", e);
//            throw new JSONException("Handler not found");
//          }
//        }
//
//        String url = result.getString("visit");
//
//        Uri uri = Uri.parse(url);
//        PathSpec path = new PathSpec(uri.getPath());
//
//        activity.startActivity(createTurbolinksIntent(path));
//        activity.finish();
//      }
//
//      @Override
//      protected final void onJsonSuccess(GRestResponse r) throws JSONException {
////    String message = r.getJResult().getNullableString(ParamKeys.MESSAGE);
////    if (message != null) {
////      ToastUtils.showNormal(message);
////    }
//      }
    };

    // The HTTP method is specified as a field.
    new HttpAsyncPost(endpoint, params.toImmutable(), HttpHook.DUMMY, HttpMethod.from(params), restCallback).execute();
  }
}
