package com.gani.web.htmlform;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v7.widget.LinearLayoutCompat;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.gani.lib.http.GHttp;
import com.gani.lib.http.GHttpCallback;
import com.gani.lib.http.GHttpError;
import com.gani.lib.http.GHttpResponse;
import com.gani.lib.http.HttpAsyncGet;
import com.gani.lib.http.HttpAsyncMultipart;
import com.gani.lib.http.HttpHook;
import com.gani.lib.logging.GLog;
import com.gani.lib.screen.GFragment;
import com.gani.lib.ui.view.GButton;
import com.gani.lib.ui.view.GTextView;
import com.gani.lib.ui.view.GView;
import com.gani.web.R;
import com.gani.web.htmlform.field.HtmlCheckBox;
import com.gani.web.htmlform.field.HtmlDataList;
import com.gani.web.htmlform.field.HtmlEditText;
import com.gani.web.htmlform.field.HtmlRadioButton;
import com.gani.web.htmlform.field.HtmlSpinner;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickResult;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class HtmlForm {
  private static final int GAP = 12;

//  private final String TAG = HTMLForm.class.getName();
  private final String INPUT_TAG = "input";
  private final String TEXTAREA_TAG = "textarea";
  private final String SELECT_TAG = "select";

  private final String TEXT_TYPE = "text";
  private final String URL_TYPE = "url";
  private final String HIDDEN_TYPE = "hidden";
  private final String SUBMIT_TYPE = "submit";
  private final String RADIO_TYPE = "radio";
  private final String CHECKBOX_TYPE = "checkbox";
  private final String EMAIL_TYPE = "email";
  private final String PASSWORD_TYPE = "password";
  private final String DATALIST_TYPE = "data_list";
  private final String TEL_TYPE = "tel";
  private final String FILE_TYPE = "file";

  public static final String NAME_ATTR = "name";

  public static final String RADIO_TYPE_QUERY = "input[type=radio]";
  public static final String CHECKBOX_TYPE_QUERY = "input[type=checkbox]";

  private GFragment fragment;
  private Context mContext;
  private LinearLayout mLayout;
  private String mURL;
//  private ArrayList<Integer> mViewIds = new ArrayList<>();
//  private Document mDocument;
  private Element mForm;
  private Elements mFields;
  private HtmlFormOnSubmitListener mListener;
  private String csrfToken;
  private Button submitButton;

  private HashMap<String, HttpAsyncMultipart.Uploadable> attachments = new HashMap<>();

  public HashMap<String, HttpAsyncMultipart.Uploadable> getAttachments() {
    return attachments;
  }


  public HtmlForm(GFragment fragment, LinearLayout layout, String url) {
    this.fragment = fragment;
    this.mContext = fragment.getContext();
    this.mLayout = layout;
    this.mURL = url;
  }

//  public String getURL() {
//    return mURL;
//  }

  public Element getFormElement() {
    return mForm;
  }

  public GFragment getFragment() {
    return fragment;
  }

//  public Document getDocument() {
//    return mDocument;
//  }

  public LinearLayout getLayout() {
    return mLayout;
  }

  public void buildFields() {
    // TODO: Implement merging instead of nuking the views and their states.
    mLayout.removeAllViews();

    fragment.showProgress();

    GHttpCallback callback = new GHttpCallback<GHttpResponse, GHttpError>() {
      @Override
      public void onHttpResponse(GHttpResponse response) {
        if (!response.hasError()) {
          Document document = Jsoup.parse(response.asString());
          parse(document);
        } else {
          GHttp.instance().alertHelper().alertFormParsingError(mContext, response);
        }

        fragment.hideProgress();
      }

//      @Override
//      public void onHttpSuccess(GHttpResponse response) {
//        if (!response.hasError()) {
//          Document document = Jsoup.parse(response.asString());
//          parse(document);
//        } else {
//          GHttp.instance().alertHelper().alertFormParsingError(mContext, response);
//        }
//
//        fragment.hideProgress();
//      }
//
//      @Override
//      public void onHttpFailure(GHttpError error) {
//        GHttp.instance().alertHelper().alertFormParsingError(mContext, error.getResponse());
//        fragment.hideProgress();
////                GLog.e(getClass(), "Failed retrieving form");
//      }
    };

    new HttpAsyncGet(mURL, null, HttpHook.DUMMY, callback).execute();
  }

  public void setOnSubmitListener(HtmlFormOnSubmitListener listener) {
    this.mListener = listener;
  }

//    private HTMLEditText createInputField(int index, Element field, LayoutParams params) {
//        HTMLEditText editText = new HTMLEditText(mContext, field, params);
//        editText.setId(mViewIds.get(index));
//
//        return editText;
//    }

  private HtmlForm getCurrentForm() {
    return this;
  }

  private void addFieldWithLabel(View view, Element element) {
    addLabel(element);
    mLayout.addView(view);

//    if (view instanceof GView) {
//      ((GView) view).margin(null, null, null, 12);
//    }
  }

  private void addFieldWithoutLabel(View view) {
    if (view instanceof GView) {
      ((GView) view).margin(null, GAP, null, null);
    }

    mLayout.addView(view);
  }

  private int parseInputTag(final Element field, int index) {
    HtmlEditText htmlEditText;

    switch (field.attr("type")) {
      case DATALIST_TYPE:
//        addLabel(field);
        HtmlDataList htmlDataList = new HtmlDataList(mContext, field);
        addFieldWithLabel(htmlDataList, field);
        break;
      case TEL_TYPE:
//        addLabel(field);
        htmlEditText = new HtmlEditText(mContext, this, field);
        htmlEditText.setSingleLine(true);
        htmlEditText.setInputType(InputType.TYPE_CLASS_PHONE);
        addFieldWithLabel(htmlEditText, field);
        break;
      case FILE_TYPE:
//        addLabel(field);
        final ImageView imageView = new ImageView(mContext);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 100));
        imageView.setImageResource(R.mipmap.default_image);
        imageView.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            PickSetup pickSetup = new PickSetup();
            pickSetup.setIconGravity(Gravity.LEFT);
            pickSetup.setButtonOrientation(LinearLayoutCompat.HORIZONTAL);
            PickImageDialog.build(pickSetup)
                    .setOnPickResult(new IPickResult() {
                      @Override
                      public void onPickResult(PickResult pickResult) {
                        if (pickResult.getError() == null) {
                          imageView.setImageBitmap(pickResult.getBitmap());

                          Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                          ByteArrayOutputStream stream = new ByteArrayOutputStream();
                          bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                          HttpAsyncMultipart.Uploadable attachment = new HttpAsyncMultipart.Uploadable("photo.jpg", "image/jpeg", stream.toByteArray());
                          attachments.put(field.attr("name"), attachment);
                        } else {
                          Toast.makeText(getFragment().getActivity(), pickResult.getError().getMessage(), Toast.LENGTH_LONG).show();
                        }
                      }
                    })
                    .show(getFragment().getFragmentManager());
          }
        });
        addFieldWithLabel(imageView, field);
        break;
      case TEXT_TYPE:
//        addLabel(field);
        htmlEditText = new HtmlEditText(mContext, this, field);
        htmlEditText.setSingleLine(true);
        addFieldWithLabel(htmlEditText, field);
        break;
      case EMAIL_TYPE:
//        addLabel(field);
        htmlEditText = new HtmlEditText(mContext, this, field);
        htmlEditText.setSingleLine(true);
        htmlEditText.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        addFieldWithLabel(htmlEditText, field);
        break;
      case PASSWORD_TYPE:
//        addLabel(field);
        htmlEditText = new HtmlEditText(mContext, this, field);
        htmlEditText.setSingleLine(true);
        // See http://stackoverflow.com/questions/21326790/calling-edittext-setinputtypeinputtype-type-text-variation-password-does-not-c
        htmlEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        // Without this, the hint gets displayed using a different font
        htmlEditText.setTypeface(Typeface.DEFAULT);
        addFieldWithLabel(htmlEditText, field);
        break;
      case URL_TYPE:
//        addLabel(field);
        htmlEditText = new HtmlEditText(mContext, this, field);
        htmlEditText.setSingleLine(true);
        htmlEditText.setInputType(InputType.TYPE_TEXT_VARIATION_URI);
        addFieldWithLabel(htmlEditText, field);
        break;
      case HIDDEN_TYPE:
        htmlEditText = new HtmlEditText(mContext, this, field);
        htmlEditText.setVisibility(View.GONE);
        addFieldWithoutLabel(htmlEditText);
        break;
      case CHECKBOX_TYPE:
        HtmlCheckBox htmlCheckBox = new HtmlCheckBox(mContext, field);
        addFieldWithoutLabel(htmlCheckBox);
        break;
      case RADIO_TYPE:
        RadioGroup radioGroup = new RadioGroup(mContext);
        radioGroup.setTag(field.attr(NAME_ATTR));
        addFieldWithoutLabel(radioGroup);

        Elements radioButtons = mForm.getElementsByAttributeValue(NAME_ATTR, field.attr(NAME_ATTR));
        radioButtons = radioButtons.select(RADIO_TYPE_QUERY);

        for (int i = 0; i < radioButtons.size(); i++) {
          Element radio = mFields.get(index + i);
          HtmlRadioButton htmlRadioButton = new HtmlRadioButton(mContext, radio);
          if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            htmlRadioButton.setId(View.generateViewId());
          }
          radioGroup.addView(htmlRadioButton);
        }

        index = index + (radioButtons.size() - 1);
        break;
      case SUBMIT_TYPE:
        GButton button = new GButton(mContext).size(ViewGroup.LayoutParams.MATCH_PARENT, null);
        button.setTag(field.attr(NAME_ATTR));
        button.setText(field.val());
        button.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
//                  boolean valid = true;
//                  for (int i = 0; i < mLayout.getChildCount(); i++) {
//                    View child = mLayout.getChildAt(i);
//                    if (child instanceof HtmlFormValidatable) {
//                      if (!((HtmlFormValidatable) child).validate()) {
//                        valid = false;
//                      }
//                    }
//                  }
//
//                  if (valid) {
//                    mListener.onSubmit(getCurrentForm());
//                  }
            mListener.onSubmit(getCurrentForm());
          }
        });
        addFieldWithoutLabel(button);

        submitButton = button;
        break;
    }
    return index;
  }

  private void parse(Document document) {
    mForm = document.select("form").first();
    mFields = mForm.select("input, textarea, select");

    for (int index = 0; index < mFields.size(); index++) {
      Element field = mFields.get(index);

      switch (field.tagName()) {
        case INPUT_TAG:
          index = parseInputTag(field, index);
          break;
        case TEXTAREA_TAG:
//          addLabel(field);
          HtmlEditText htmlEditText = new HtmlEditText(mContext, this, field);
          htmlEditText.setLines(3);
          addFieldWithLabel(htmlEditText, field);
          break;
        case SELECT_TAG:
//          addLabel(field);
          HtmlSpinner htmlSpinner = new HtmlSpinner(mContext, this, field);
          addFieldWithLabel(htmlSpinner, field);
          break;
      }
    }

    extractCsrfToken(document);
    mListener.afterBuild(getCurrentForm());
  }

  public static String parseCsrfToken(Document document) {
    Element tokenElement  = document.select("meta[name=csrf-token]").first();
    if (tokenElement != null) {
      return tokenElement.attr("content");
    }
    return null;
  }

  public void extractCsrfToken(Document document) {
//    Element tokenElement  = document.select("meta[name=csrf-token]").first();
//    if (tokenElement != null) {
//      csrfToken = tokenElement.attr("content");
//    }
    csrfToken = parseCsrfToken(document);
  }

  public String getCsrfToken() {
    return csrfToken;
  }

  private void addLabel(Element field) {
    Elements label = field.parent().getElementsByTag("label");

    if (label.size() == 0) {
      label = field.parent().parent().getElementsByTag("label");
    }

    if (label.size() > 0) {
      GTextView textView = new GTextView(mContext).margin(null, GAP, null, 2);
      textView.setTextColor(Color.BLACK);
      textView.setTypeface(null, Typeface.BOLD);
      textView.setText(label.text());
      mLayout.addView(textView);
    }
  }

  void updateValid() {
    if (submitButton != null) {
      boolean valid = true;
      for (int i = 0; i < mLayout.getChildCount(); i++) {
        View child = mLayout.getChildAt(i);
        if (child instanceof HtmlFormValidatable) {
          if (!((HtmlFormValidatable) child).isValid()) {
            valid = false;
          }
        }
      }
      submitButton.setEnabled(valid);
    }
  }
}
