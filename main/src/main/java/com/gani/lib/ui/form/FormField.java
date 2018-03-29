package com.gani.lib.ui.form;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

//import com.clsreview.clsreview.R;
import com.gani.lib.ui.layout.AbstractHorizontalLayout;
import com.gani.lib.ui.layout.VerticalLayout;
import com.gani.lib.ui.view.GTextView;

public abstract class FormField extends AbstractHorizontalLayout<FormField> {
  private TextView label;
  private View editView;

  public FormField(Context context, String labelText, GTextView.Spec labelSpec) {
    super(context);

    setGravity(Gravity.CENTER_VERTICAL);
    size(ViewGroup.LayoutParams.MATCH_PARENT, null).padding(0, 10, 0, 10);

    this.label = createLabel(context).spec(labelSpec);
    this.editView = createEditView(context);

    VerticalLayout editLayout = new VerticalLayout(context);
    editLayout.addView(editView);

    setWeightOf(label, 10);
    setWeightOf(editLayout, 5);

//    label.setBackgroundColor(Color.RED);
//    editLayout.setBackgroundColor(Color.BLUE);

    addView(label);
    addView(editLayout);

//    setBackground(Ui.drawable(R.drawable.border_set));

    label(labelText);
  }

  public FormField label(String text) {
    label.setText(text);
    return this;
  }

  private GTextView createLabel(Context context) {
    return new GTextView(context);
  }

  protected abstract View createEditView(Context context);

  TextView getLabel() {
    return label;
  }
}
