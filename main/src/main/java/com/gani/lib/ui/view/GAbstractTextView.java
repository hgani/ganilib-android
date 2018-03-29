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

public class GAbstractTextView<T extends GAbstractTextView> extends AppCompatTextView implements GView {
  private ViewHelper helper;

  public GAbstractTextView(Context context) {
    super(context);
    init();
  }

  public GAbstractTextView(Context context, AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  private void init() {
    this.helper = new ViewHelper(this);
  }

  private T self() {
    return (T) this;
  }

  public GRelativeLayoutParams<T> relative() {
    return helper.relative(self());
  }

  public GAbstractTextView spec(Spec spec) {
    spec.init(this);
    return this;
  }

  public T size(Integer width, Integer height) {
    helper.size(width, height);
    return self();
  }

  public T width(Integer width) {
    helper.width(width);
    return self();
  }

  public T height(Integer height) {
    helper.height(height);
    return self();
  }

  public GAbstractTextView processBold() {
    List<String> matchers = new ArrayList<>();
    String text = getText().toString();
    SpannableStringBuilder builder = new SpannableStringBuilder(text);
    Pattern boldPattern = Pattern.compile("\\*([A-z0-9 ]+)\\*");
    Matcher matcher = boldPattern.matcher(builder);

    while(matcher.find()) {
      matchers.add(matcher.group());
    }

    for (int i = matchers.size() - 1; i >= 0; i--) {
      String m = matchers.get(i);
      int startIndex = text.indexOf(m);
      int endIndex = startIndex + m.length();

      SpannableString str = new SpannableString(m.substring(1, m.length() - 1));
      str.setSpan(new StyleSpan(Typeface.BOLD), 0, str.length(), 0);
      builder.replace(startIndex, endIndex, str);
    }

    setText(builder);

    return this;
  }

  public T bgColor(String code) {
    bgColor(Ui.color(code));
    return self();
  }

  public T bgColor(int color) {
    setBackgroundColor(color);
    return self();
  }

  public T color(String code) {
    return color(Ui.color(code));
  }

  public T color(int color) {
    setTextColor(color);
    return self();
  }

  @Override
  public T padding(Integer left, Integer top, Integer right, Integer bottom) {
    helper.padding(left, top, right, bottom);
    return self();
  }

  @Override
  public T margin(Integer left, Integer top, Integer right, Integer bottom) {
    helper.margin(left, top, right, bottom);
    return self();
  }

  public GAbstractTextView bold() {
    return typeface(Typeface.DEFAULT_BOLD);
  }

  public GAbstractTextView typeface(Typeface typeface) {
    setTypeface(typeface);
    return this;
  }

  public GAbstractTextView text(String text) {
    setText(text);
    return this;
  }

  public GAbstractTextView textSize(float textSize) {
    setTextSize(textSize);
//    setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
    return this;
  }

//  public GTextView textSize(int unit, float textSize) {
//    setTextSize(unit, textSize);
//    return this;
//  }

  public GAbstractTextView gravity(int alignment) {
    setGravity(alignment);
    return this;
  }

  public GAbstractTextView onClick(OnClickListener listener) {
    helper.click(listener);
    return this;
  }

  // NOTE: Deprecated
  public GAbstractTextView click(OnClickListener listener) {
    helper.click(listener);
    return this;
  }

  // NOTE: Has to be called before setting the text.
  // See https://stackoverflow.com/questions/27927930/android-linkify-clickable-telephone-numbers
  public GAbstractTextView linkify(int mask) {
    setAutoLinkMask(mask);
    setLinksClickable(true);
    return this;
  }




//  EnumSet<FileAccess> readWrite = EnumSet.of(FileAccess.Read, FileAccess.Write);



  public static class Spec {
    public void init(GAbstractTextView textView) {
      Integer backgroundColor = backgroundColor();
      if (backgroundColor != null) {
        textView.bgColor(backgroundColor);
      }

      Integer color = color();
      if (color != null) {
        textView.color(color);
      }

      Integer textSize = textSize();
      if (textSize != null) {
        textView.textSize(textSize);
      }

      Typeface typeface = typeface();
      if (typeface != null) {
        textView.typeface(typeface);
      }
    }

    public Integer backgroundColor() {
      return null;
    }

    public Integer color() {
      return null;
    }

    public Integer textSize() {
      return null;
    }

    public Typeface typeface() { return null; }
  }
}
