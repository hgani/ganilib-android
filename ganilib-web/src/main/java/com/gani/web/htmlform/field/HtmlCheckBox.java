package com.gani.web.htmlform.field;

import android.content.Context;
import android.support.v7.widget.AppCompatCheckBox;
import android.widget.RelativeLayout;

import org.jsoup.nodes.Element;

public class HtmlCheckBox extends AppCompatCheckBox {

    private static final String ATTR_NAME = "name";
    private static final String CHECKED_ATTR  = "checked";
    private static final String VALUE_ATTR = "value";

    private final Element mField;

    private String value;

    public HtmlCheckBox(Context context, Element field, RelativeLayout.LayoutParams params) {
        super(context);

        this.mField = field;
        setLayoutParams(params);
        setDefaultListeners();
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public HtmlCheckBox(Context context, Element field) {
        super(context);

        this.mField = field;
        setDefaultListeners();
    }

    private void setDefaultListeners() {
        setValue(mField.attr(VALUE_ATTR));
        setChecked(mField.hasAttr(CHECKED_ATTR));
        setTag(mField.attr(ATTR_NAME));
        setText(mField.parent().text());

//        setOnCheckedChangeListener(new HTMLFieldValidation(mField, this));
    }
}
