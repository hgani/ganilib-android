package com.gani.web.htmlform.field;

import android.content.Context;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

import com.gani.lib.ui.view.GAutoCompleteTextView;
import com.gani.web.R;

import org.jsoup.nodes.Element;

public class HtmlDataList extends GAutoCompleteTextView {
  private static final String OPTION_MANUAL = "Other (Please specify)";

  private final Element mField;

  private boolean showAlways;

  public HtmlDataList(Context context, Element field) {
    super(context);

    this.mField = field;
    setDefaultListeners();

    final GestureDetector gestureDetector = new GestureDetector(getContext(), new SingleTapConfirm());
    setOnTouchListener(new OnTouchListener() {
      @Override
      public boolean onTouch(View view, MotionEvent motionEvent) {
        if (gestureDetector.onTouchEvent(motionEvent)) {
          showDropDown();
          return true;  // Prevent keyboard from showing up
        }
        return false;
      }
    });

    setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (adapterView.getItemAtPosition(i).equals(OPTION_MANUAL)) {
          setText("");
        }
        requestFocusWithKeyboard();
      }
    });
  }

  private class SingleTapConfirm extends GestureDetector.SimpleOnGestureListener {
    @Override
    public boolean onSingleTapUp(MotionEvent event) {
      return true;
    }
  }

  private void requestFocusWithKeyboard() {
    requestFocus();

    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
    imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT);
  }

  public void setShowAlways(boolean showAlways) {
    this.showAlways = showAlways;
  }



//  @Override
//  protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
//    super.onFocusChanged(focused, direction, previouslyFocusedRect);
////    showDropDownIfFocused();
//    performFiltering("m", 0);
//  }
//
//  private void showDropDownIfFocused() {
//    if (enoughToFilter() && isFocused() && getWindowVisibility() == View.VISIBLE) {
//      showDropDown();
//      requestFocus();
//    }
//  }
//
//  @Override
//  protected void onAttachedToWindow() {
//    super.onAttachedToWindow();
//    showDropDownIfFocused();
//  }

  @Override
  public boolean enoughToFilter() {
    return showAlways || super.enoughToFilter();
  }

  private void setDefaultListeners() {
    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
    );
    params.setMargins(0, 0, 0, 25);

    setLayoutParams(params);
    setPadding(20, 20, 20, 20);
    setBackgroundResource(R.drawable.edit_text_state);

    // Not yet needed
//    setCompoundDrawablesWithIntrinsicBounds(0, 0, android.R.drawable.arrow_down_float, 0);

    setSingleLine(true);

    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this.getContext(), android.R.layout.simple_dropdown_item_1line);
    Element datalist = mField.parent().getElementsByTag("datalist").first();

    for(Element option : datalist.getElementsByTag("option")) {
      arrayAdapter.add(option.text());
    }
    arrayAdapter.add(OPTION_MANUAL);

    setTag(mField.attr("name"));
    setShowAlways(true);
    setAdapter(arrayAdapter);
  }
}
