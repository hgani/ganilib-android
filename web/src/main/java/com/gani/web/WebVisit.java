package com.gani.web;

import com.gani.lib.http.GImmutableParams;
import com.gani.lib.logging.GLog;
import com.gani.web.turbolinks.GTurbolinksSupportActivity;

public class WebVisit {
  public enum Action {
    ADVANCE {
      @Override
      public boolean openGeneric(GTurbolinksSupportActivity activity, String path, GImmutableParams params) {
        GLog.t(getClass(), "OPEN GENERIC: ADVANCE");

        activity.startTurbolinksScreen(new PathSpec(path), params);
//        activity.startActivity(GScreenTurbolinks.intent(new PathSpec(path), params));
        return true;
      }

      @Override
      public void openSpecific(GTurbolinksSupportActivity activity, Runnable command) {
        command.run();
      }
    },
    REPLACE {
      @Override
      public boolean openGeneric(GTurbolinksSupportActivity activity, String path, GImmutableParams params) {
        return false;
      }

      @Override
      public void openSpecific(GTurbolinksSupportActivity activity, Runnable command) {
        command.run();
        activity.finish();
      }
    };

    public abstract boolean openGeneric(GTurbolinksSupportActivity activity, String path, GImmutableParams params);
    public abstract void openSpecific(GTurbolinksSupportActivity activity, Runnable command);
  }
}
