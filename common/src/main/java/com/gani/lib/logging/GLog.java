package com.gani.lib.logging;

import android.util.Log;

import com.gani.lib.GApp;
import com.gani.lib.collection.SelfTruncatingSet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class GLog {
  // See http://stackoverflow.com/questions/4655861/displaying-more-string-on-logcat
//  private static int LOGCAT_CHAR_LIMIT = 4000;
  // Turn if off now because it makes debugging harder in normal circumstances.
  private static int LOGCAT_CHAR_LIMIT = Integer.MAX_VALUE;

  public static void w(Class<?> cls, String msg, Throwable t) {
    Log.w(cls.getName(), msg, t);
  }

  public static void w(Class<?> cls, String msg) {
    Log.w(cls.getName(), msg);
  }

  public static void d(Class<?> cls, String msg) {
    if (msg.length() > LOGCAT_CHAR_LIMIT) {
      Log.d(cls.getName(), msg.substring(0, LOGCAT_CHAR_LIMIT));
      d(cls, msg.substring(LOGCAT_CHAR_LIMIT));
    }
    else {
      Log.d(cls.getName(), msg);
    }
  }

  public static void i(Class<?> cls, String msg) {
    if (msg.length() > LOGCAT_CHAR_LIMIT) {
      Log.i(cls.getName(), msg.substring(0, LOGCAT_CHAR_LIMIT));
      i(cls, msg.substring(LOGCAT_CHAR_LIMIT));
    }
    else {
      Log.i(cls.getName(), msg);
    }
  }

  // Prominent logging to accomodate temporary testing.
  public static void t(Class<?> cls, String msg) {
    i(cls, "********** " + msg);
  }

  public static void e(Class<?> cls, String msg) {
    Log.e(cls.getName(), msg);
  }

  public static void e(Class<?> cls, String msg, Throwable t) {
    Log.e(cls.getName(), msg, t);
  }



  // Adopted from http://stackoverflow.com/questions/27957300/read-logcat-programmatically-for-an-application
  public static final class Reader {
    private static final SelfTruncatingSet<String> processIds = new SelfTruncatingSet<String>(3);
    private static final String[] BLACKLISTED_STRINGS = new String[] {
      " D/TextLayoutCache",
      " D/FlurryAgent",
      " D/dalvikvm"
    };

    public static void registerProcessId() {
      processIds.add(Integer.toString(android.os.Process.myPid()));
    }
    
    //    private static final String TAG = Reader.class.getCanonicalName();
//    private static final String processId = Integer.toString(android.os.Process.myPid());
    
    private static boolean belongsToApp(String line) {
      for (String processId : processIds) {
        if (line.contains(processId)) {
          return true;
        }
      }
      return false;
    }
    
    private static boolean shouldLog(String line) {
      if (belongsToApp(line)) {
        for (String blacklistedString: BLACKLISTED_STRINGS) {
          if (line.contains(blacklistedString)) {
            return false;
          }
        }
        return true;
      }
      return false;
    }
    
    public static String getLog() {
//      StringBuilder builder = new StringBuilder();

      try {
        // See http://www.helloandroid.com/tutorials/reading-logs-programatically
        // See http://developer.android.com/tools/debugging/debugging-log.html#outputFormat
        Process process = Runtime.getRuntime().exec("logcat -d -v time");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                         
        StringBuilder log = new StringBuilder("App version: " + GApp.getCodeName() + "-" + GApp.getVersionName());
        String line;
        while ((line = bufferedReader.readLine()) != null) {
//          for (String processId : processIds) {
//            if (line.contains(processId)) {
          if (shouldLog(line)) {
            log.append(line + "\n\n");
          }
        }
        
//        String[] command = new String[] { "logcat", "-v", "threadtime" };
//
//        Process process = Runtime.getRuntime().exec(command);
//
//        BufferedReader bufferedReader = new BufferedReader(
//                new InputStreamReader(process.getInputStream()));
//
//        String line;
//        while ((line = bufferedReader.readLine()) != null) {
//          if (line.contains(processId)) {
//            builder.append(line);
//          }
//        }
        return log.toString();
      } catch (IOException ex) {
        throw new RuntimeException(ex);
//          Log.e(TAG, "getLog failed", ex);
      }

//      return builder.toString();
    }
  }
}
