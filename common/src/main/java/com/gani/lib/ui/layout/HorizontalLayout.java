package com.gani.lib.ui.layout;

import android.content.Context;
import android.widget.LinearLayout;

// TODO: Remove. Deprecated
public class HorizontalLayout extends AbstractLinearLayout {
    public HorizontalLayout(Context context){
        super(context);
        this.setOrientation(LinearLayout.HORIZONTAL);
    }
}
