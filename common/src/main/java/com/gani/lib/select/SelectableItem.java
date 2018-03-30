package com.gani.lib.select;

import android.view.View;
import android.widget.TextView;

import com.gani.lib.utils.string.StringUtils;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public interface SelectableItem extends Serializable {
  String getName();

  

  public class Utils {
    private static List<String> collectNames(List<? extends SelectableItem> items) {
      List<String> itemNames = new LinkedList<String>();
      for (SelectableItem item : items) {
        itemNames.add(item.getName());
      }
      return itemNames;
    }

    public static String toLineSeparatedNames(List<? extends SelectableItem> items) {
      return StringUtils.join(collectNames(items), "\n");
    }

    public static String toSpaceSeparatedNames(List<? extends SelectableItem> items) {
      return StringUtils.join(collectNames(items), " ");
    }

    // Set to empty when there is no error. This is important for determining form validity (e.g. when enabling submit button).
    public static void initLayout(TextView selected, TextView error, List<? extends SelectableItem> items) {
      String names = toLineSeparatedNames(items);
      selected.setText(names);
      selected.setVisibility((names.isEmpty())? View.GONE : View.VISIBLE);
      
      if (names.isEmpty()) {
//        error.setText(R.string.error_empty);
        error.setText("Cannot be empty");
      }
      else {
        error.setText("");
      }
    }
  }
}
