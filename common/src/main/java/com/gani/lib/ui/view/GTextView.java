package com.gani.lib.ui.view;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.util.AttributeSet;

import com.gani.lib.ui.Ui;
import com.gani.lib.ui.layout.GRelativeLayoutParams;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.R.attr.width;

public final class GTextView extends GAbstractTextView<GTextView> {
  public GTextView(Context context) {
    super(context);
  }

  public GTextView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }
}
