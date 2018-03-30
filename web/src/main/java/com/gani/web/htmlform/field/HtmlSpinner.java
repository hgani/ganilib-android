package com.gani.web.htmlform.field;


import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.gani.lib.ui.view.GSpinner;
import com.gani.web.R;
import com.gani.web.htmlform.HtmlFieldValidator;
import com.gani.web.htmlform.HtmlForm;
import com.gani.web.htmlform.HtmlFormValidatable;

import org.jsoup.nodes.Element;

import java.util.ArrayList;

public class HtmlSpinner extends GSpinner implements HtmlFormValidatable {
  private static final String ATTR_NAME = "name";
  private static final String SELECTED_ATTR = "selected";

  private final Element mField;
  private Validator validator;

//  public HTMLSpinner(Context context, Element field, RelativeLayout.LayoutParams params) {
//    super(context);
//
//    this.mField = field;
//    this.validator = new Validator(field);
//
//    setLayoutParams(params);
//    setDefaultListeners();
//  }

  public HtmlSpinner(Context context, HtmlForm form, Element field) {
    super(context);

    this.mField = field;
    this.validator = new Validator(form, field);

    size(LayoutParams.MATCH_PARENT, null);

    setDefaultListeners();
  }

  private void setDefaultListeners() {
    ArrayList<String> spinnerArray = new ArrayList<>();

//        spinnerArray.add("");
    for (Element option : mField.children()) {
      spinnerArray.add(option.text());
    }

    ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(
        getContext(),
        android.R.layout.simple_spinner_dropdown_item,
        spinnerArray
    );

    String selectedItem = mField.getElementsByAttributeValue(SELECTED_ATTR, SELECTED_ATTR).text();
    int selectedIndex = spinnerAdapter.getPosition(selectedItem);
    if (selectedIndex < 0) {
      selectedIndex = 0;
    }

//    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
//        ViewGroup.LayoutParams.MATCH_PARENT,
//        ViewGroup.LayoutParams.WRAP_CONTENT
//    );
//    params.setMargins(0, 0, 0, 25);
//
//    setLayoutParams(params);
    setBackgroundResource(R.drawable.spinner_default);
    setAdapter(spinnerAdapter);
    setSelection(selectedIndex);
    setTag(mField.attr(ATTR_NAME));

//    setOnItemSelectedListener(new HTMLFieldValidation(mField, this));
    setOnItemSelectedListener(validator);
  }


  ///// Validation /////

  @Override
  public boolean validate() {
    View view = getSelectedView();
    if (view instanceof TextView) {  // This should hold true unless internal implementation is changed in newer API levels.
      TextView textView = (TextView) view;
      return validator.run(textView.getText().toString(), textView);
    }
    return true;
  }

  @Override
  public boolean isValid() {
    return validator.isValid();
  }

  private class Validator extends HtmlFieldValidator implements Spinner.OnItemSelectedListener {
    Validator(HtmlForm form, Element field) {
      super(form, field);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
      validate();

//      mErrorMessages.clear();
//      String value = view.getSelectedItem().toString();
//      validateField(value);
//
//      TextView selectedTextView = (TextView) view;
//      if (mErrorMessages.size() > 0) {
//        selectedTextView.setError(readableErrorMessages());
//      } else {
//        selectedTextView.setError(null);
//      }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
      // Do nothing
    }
  }

  /////
}
