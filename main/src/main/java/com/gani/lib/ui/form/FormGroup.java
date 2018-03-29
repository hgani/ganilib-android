package com.gani.lib.ui.form;

import android.content.Context;

import com.gani.lib.ui.layout.VerticalLayout;

public class FormGroup extends VerticalLayout {
  private Float labelFontSize;

  public FormGroup(Context context) {
    super(context);
    size(LayoutParams.MATCH_PARENT, null);
  }

  public FormGroup labelFontSize(float labelFontSize) {
    this.labelFontSize = labelFontSize;
    return this;
  }

  public void add(FormField field) {
    if (labelFontSize != null) {
      field.getLabel().setTextSize(labelFontSize);
    }
    addView(field);
  }
}
