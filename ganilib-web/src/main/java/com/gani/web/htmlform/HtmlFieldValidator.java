package com.gani.web.htmlform;

import android.text.TextUtils;
import android.widget.CompoundButton;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.Objects;

public abstract class HtmlFieldValidator implements CompoundButton.OnCheckedChangeListener {
  private static final String ATTR_VALIDATE = "data-validate";

  private static final String JSON_MESSAGES = "messages";
  private static final String JSON_OPTIONS = "options";

  private static final String VALIDATE_KIND = "kind";
  private static final String VALIDATE_PRESENCE = "presence";
  private static final String VALIDATE_LENGTH = "length";
  private static final String VALIDATE_BLACKLIST = "blacklist";

  private static final String OPTIONS_MAXIMUM = "maximum";
  private static final String OPTIONS_FORMATS = "formats";

  private static final String MESSAGES_BLANK = "blank";
  private static final String MESSAGES_TOO_LONG = "too_long";
//
//  private static final String DATETIME_PICKER_TYPE = "datetime_picker";
//  private static final String DATE_PICKER_TYPE = "date_picker";
//  private static final String TIME_PICKER_TYPE = "time_picker";
//
//  private static final String DATE_FORMAT = "\\d{4}-\\d{2}-\\d{2}";
//  private static final String TIME_FORMAT = "\\d{2}:\\d{2}";
//  private static final String DATETIME_FORMAT = DATE_FORMAT + "\\s" + TIME_FORMAT;

  private HtmlForm form;
  private Element mField;
//  private HTMLEditText mHtmlEditText;
//  private HTMLSpinner mHtmlSpinner;
//  private HTMLCheckBox mHtmlCheckBox;
//  private HTMLRadioButton mHtmlRadioButton;
  private ArrayList<String> mErrorMessages = new ArrayList<>();

  public HtmlFieldValidator(HtmlForm form, Element field) {
    this.form = form;
    this.mField = field;
  }

//  public HTMLFieldValidation(Element field, HTMLSpinner htmlSpinner) {
//    this.mField = field;
//    this.mHtmlSpinner = htmlSpinner;
//  }
//
//  public HTMLFieldValidation(Element field, HTMLEditText htmlEditText) {
//    this.mField = field;
//    this.mHtmlEditText = htmlEditText;
//  }
//
//  public HTMLFieldValidation(Element field, HTMLCheckBox htmlCheckBox) {
//    this.mField = field;
//    this.mHtmlCheckBox = htmlCheckBox;
//  }
//
//  public HTMLFieldValidation(Element field, HTMLRadioButton htmlRadioButton) {
//    this.mField = field;
//    this.mHtmlRadioButton = htmlRadioButton;
//  }

  @Override
  public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//        TODO: no example usage validation for checkbox and radio button
//        compoundButton.setError("ASD");
  }

  protected void clearErrorMessages() {
    mErrorMessages.clear();
  }

  protected void addErrorMessage(String message) {
    mErrorMessages.add(message);
  }

  protected boolean hasErrorMessage() {
    return mErrorMessages.size() > 0;
  }

  public boolean isValid() {
    return !hasErrorMessage();
  }

  protected String readableErrorMessages() {
    return TextUtils.join("\n", mErrorMessages);
  }

  private boolean isEnabled() {
    return false;
  }

  public boolean run(String value, TextView view) {
    if (!isEnabled()) {
      return true;
    }

    clearErrorMessages();
//    String value = getText().toString();
//    String value = view.toString();
    validateField(value);
    validateSpecific(value);

    boolean valid = !hasErrorMessage();
    if (valid) {
      view.setError(null);
    } else {
      view.setError(readableErrorMessages());
    }
    form.updateValid();

    return valid;
  }

  protected void validateSpecific(String value) {
    // To be overridden
  }

//
//  private void validate(HTMLEditText view) {
//    mErrorMessages.clear();
//    String value = view.getText().toString();
//    validateField(value);
//
//    if (mField.classNames().contains(DATE_PICKER_TYPE)) {
//      validateDateTimeFormat(value, DATE_FORMAT);
//    } else if (mField.classNames().contains(TIME_PICKER_TYPE)) {
//      validateDateTimeFormat(value, TIME_FORMAT);
//    } else if (mField.classNames().contains(DATETIME_PICKER_TYPE)) {
//      validateDateTimeFormat(value, DATETIME_FORMAT);
//    }
//  }
//
//  private void validateDateTimeFormat(String value, String dateFormat) {
//    Pattern r = Pattern.compile("^" + dateFormat + "$");
//    Matcher m = r.matcher(value);
//
//    if (!m.find()) {
//      mErrorMessages.add("Invalid");
//    }
//  }

  protected void validateField(String value) {
    if (!mField.attr(ATTR_VALIDATE).equals("")) {
      try {
        JSONArray validateOptions = new JSONArray(mField.attr(ATTR_VALIDATE));

        for (int index = 0; index < validateOptions.length(); index++) {
          JSONObject validation = validateOptions.getJSONObject(index);
          JSONObject messages = validation.getJSONObject(JSON_MESSAGES);
          JSONObject options = validation.getJSONObject(JSON_OPTIONS);

          if (Objects.equals(validation.getString(VALIDATE_KIND), VALIDATE_PRESENCE)) {
            if (value.isEmpty()) {
              mErrorMessages.add(messages.getString(MESSAGES_BLANK));
            }
          }

          if (Objects.equals(validation.getString(VALIDATE_KIND), VALIDATE_LENGTH)) {
            if (value.length() > options.getInt(OPTIONS_MAXIMUM)) {
              mErrorMessages.add(messages.getString(MESSAGES_TOO_LONG));
            }
          }

//                TODO: uncomment for validate blacklist, currently no error messages on attributes
//                if (Objects.equals(validation.getString(VALIDATE_KIND), VALIDATE_BLACKLIST)) {
//                    JSONArray formats = options.getJSONArray(OPTIONS_FORMATS);
//                    for(int i = 0; i < formats.length(); i++) {
//                        Pattern r = Pattern.compile(formats.getString(i));
//                        Matcher m = r.matcher(value);
//
//                        if (!m.find()) {
//                            mErrorMessages.add(messages);
//                        }
//                    }
//                }
        }
      } catch (JSONException e) {
        e.printStackTrace();
      }
    }
  }
}
