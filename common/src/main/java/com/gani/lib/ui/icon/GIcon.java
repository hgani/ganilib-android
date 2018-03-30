package com.gani.lib.ui.icon;

import com.joanzapata.iconify.Icon;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.IconFontDescriptor;
import com.joanzapata.iconify.Iconify;

public interface GIcon extends IconSpec {
  String text();
  GIconHelper.Spec color(int color);
}
