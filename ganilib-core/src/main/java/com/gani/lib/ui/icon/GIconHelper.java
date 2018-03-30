package com.gani.lib.ui.icon;

import com.gani.lib.logging.GLog;
import com.gani.lib.ui.Ui;
import com.joanzapata.iconify.Icon;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.IconFontDescriptor;
import com.joanzapata.iconify.Iconify;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

//public enum CvIcon implements Icon {
public abstract class GIconHelper implements GIcon {
  public class Spec implements IconSpec {
    private GIconHelper origIcon;
    private int color;

    private Spec(GIconHelper origIcon, int color) {
      this.origIcon = origIcon;
      this.color = color;
    }

    public IconDrawable drawable() {
      return origIcon.drawable().colorRes(color);
    }

    @Override
    public String key() {
      return origIcon.key();
    }

    @Override
    public char character() {
      return origIcon.character();
    }
  }

  private Icon delegate;

  public GIconHelper(Icon delegate) {
    this.delegate = delegate;
    GLog.t(getClass(), "ICON HELPER: " + this);
    AbstractModule.register(this);
  }

  @Override
  public GIconHelper.Spec color(int color) {
    return new Spec(this, color);
  }

  @Override
  public String key() {
    return name().replace('_', '-');
  }

  protected abstract String name();

  @Override
  public char character() {
    return delegate.character();
  }

  @Override
  public String text() {
    return "{" + key() + "}";
  }

  public IconDrawable drawable() {
    return new IconDrawable(Ui.context(), this);

//    return new IconDrawable(App.context(), new Icon() {
//      @Override
//      public String key() {
//        return CvIcon.this.key();
//      }
//
//      @Override
//      public char character() {
//        return delegate.character();
//      }
//    });
  }



  public static class AbstractModule implements IconFontDescriptor {
    private static Map<Class<? extends Icon>, List<GIconHelper>> iconMap = new HashMap<>();

    static void register(GIconHelper icon) {
      Class<? extends Icon> iconClass = icon.delegate.getClass();
      List<GIconHelper> icons = iconMap.get(iconClass);
      GLog.t(AbstractModule.class, "ICON CLASS: " + iconClass);
      if (icons == null) {
        icons = new LinkedList<>();
        iconMap.put(iconClass, icons);
      }
      icons.add(icon);
    }

    private IconFontDescriptor delegate;
    private Class<? extends Icon> iconClass;

    public AbstractModule(IconFontDescriptor delegate, Class<? extends Icon> iconClass) {
      this.delegate = delegate;
      this.iconClass = iconClass;
    }

    @Override
    public String ttfFileName() {
      return delegate.ttfFileName();
    }

    @Override
    public Icon[] characters() {
      List<GIconHelper> icons = iconMap.get(iconClass);
      if (icons == null) {
        throw new IllegalStateException("Make sure that the enums include icons from this fontset: " + iconClass);
      }
//      List<Icon> result = new ArrayList<>(icons.size());
////      Icon[] arr = new Icon[icons.size()];
//      for (CvIcon icon : icons) {
//        result.add(icon.delegate);
//      }
      return icons.toArray(new GIconHelper[icons.size()]);
//      return result.toArray(new Icon[result.size()]);
    }
  }
}
