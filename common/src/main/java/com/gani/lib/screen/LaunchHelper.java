package com.gani.lib.screen;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.telephony.TelephonyManager;

import com.gani.lib.logging.GLog;
import com.gani.lib.notification.Alert;

public class LaunchHelper {
  private Context context;

  public LaunchHelper(Context context) {
    this.context = context;
  }

  public void map(String address) {
    String uri = "http://maps.google.com/maps?q=" + address;
    // String uri = "geo:0,0?q=" + object.getNullableString("location");
    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
    // intent.setPackage("com.google.android.apps.maps");
    context.startActivity(intent);
  }

  public void call(String number) {
//    Intent i = new Intent(Intent.ACTION_CALL);
//    i.setData(Uri.parse("tel:" + number));
//    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
//      Toast.makeText(context, "Please grant the  permission to call", Toast.LENGTH_SHORT).show();
//      ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CALL_PHONE}, 1);
//    } else {
//      context.startActivity(i);
//    }

    if (((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getPhoneType() != TelephonyManager.PHONE_TYPE_NONE) {
      Intent i = new Intent(Intent.ACTION_DIAL);
      i.setData(Uri.parse("tel:" + number));
      context.startActivity(i);
    }
    else {
      Alert.display(context, "This device doesn't support phone calls");
    }
  }

  public void mail(String to, String subject, String message) {
    Intent i = new Intent(Intent.ACTION_SEND);
    String[] s = {to};
    i.putExtra(Intent.EXTRA_EMAIL, s);
    i.putExtra(Intent.EXTRA_SUBJECT, subject);
    i.putExtra(Intent.EXTRA_TEXT, message);
    i.setType("message/rfc822");
    Intent chooser = Intent.createChooser(i, "Launch Email");
    context.startActivity(chooser);
  }

  public void browser(String url) {
    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
    try {
      context.startActivity(browserIntent);
    }
    catch (ActivityNotFoundException e) {
      GLog.e(getClass(), "Invalid URL: " + url, e);
    }
  }
}
