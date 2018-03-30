package com.gani.web.turbolinks;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.gani.lib.logging.GLog;
import com.gani.lib.screen.GActivity;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

class FileUploadSupport {
  private GActivity activity;
  private WebView webView;

  private String mCM;
  private ValueCallback<Uri> mUploadMessage;
  private ValueCallback<Uri[]> mUploadMessageArray;
  private final static int FCR = 1;

  FileUploadSupport(GActivity activity, WebView webView) {
    this.activity = activity;
    this.webView = webView;

    // See https://github.com/turbolinks/turbolinks-android/issues/53
    WebSettings settings = webView.getSettings();
    settings.setAllowFileAccess(true);

    if (Build.VERSION.SDK_INT >= 21) {
      settings.setMixedContentMode(0);
      webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
    } else if (Build.VERSION.SDK_INT >= 19) {
      webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
    }
  }

  void register() {
    webView.setWebChromeClient(new WebChromeClient() {
      @Override
      public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
        // See http://stackoverflow.com/questions/2726377/how-to-handle-a-webview-confirm-dialog
        // Originally, we implement this only as a workaround for certain API levels which fail to show a dialog
        // with the error "Cannot create a dialog, the WebView context is not an Activity". This should be
        // Android's bug because it still doesn't work even after updating TL's webview's MutableContextWrapper's
        // base context.
        //
        // But as it turns out using a custom dialog looks better because we can prevent it from showing our website's
        // URL in the title.
        new AlertDialog.Builder(activity)
            .setMessage(message)
            .setPositiveButton(android.R.string.ok,
                new DialogInterface.OnClickListener()
                {
                  public void onClick(DialogInterface dialog, int which)
                  {
                    result.confirm();
                  }
                })
            .setNegativeButton(android.R.string.cancel,
                new DialogInterface.OnClickListener()
                {
                  public void onClick(DialogInterface dialog, int which)
                  {
                    result.cancel();
                  }
                })
            .create()
            .show();

        return true;
      }

      //For Android 4.1+
      public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
        mUploadMessage = uploadMsg;
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("image/*");
        activity.startActivityForResult(
            Intent.createChooser(i, "Choose your file"),
            FCR
        );
      }

      //For Android 5.0+
      public boolean onShowFileChooser(
          WebView webView, ValueCallback<Uri[]> filePathCallback,
          FileChooserParams fileChooserParams) {
        if (mUploadMessageArray != null) {
          mUploadMessageArray.onReceiveValue(null);
        }
        mUploadMessageArray = filePathCallback;
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {
          File photoFile = null;
          try {
            photoFile = createImageFile();
            takePictureIntent.putExtra("PhotoPath", mCM);
          } catch (IOException ex) {
            GLog.e(getClass(), "Image file creation failed", ex);
          }
          if (photoFile != null) {
            mCM = "file:" + photoFile.getAbsolutePath();
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
          } else {
            takePictureIntent = null;
          }
        }
        Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
        contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
        contentSelectionIntent.setType("image/*");
        Intent[] intentArray;
        if (takePictureIntent != null) {
          intentArray = new Intent[]{takePictureIntent};
        } else {
          intentArray = new Intent[0];
        }

        Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
        chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
        chooserIntent.putExtra(Intent.EXTRA_TITLE, "Chosse your file");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);
        activity.startActivityForResult(chooserIntent, FCR);
        return true;
      }
    });
  }

//  void onActivityCreate(WebView webView) {
//  }

  void onActivityResult(int requestCode, int resultCode, Intent intent) {
    if (Build.VERSION.SDK_INT >= 21) {
      Uri[] results = null;
      //Check if response is positive
      if (resultCode == Activity.RESULT_OK) {
        if (requestCode == FCR) {
          if (null == mUploadMessageArray) {
            return;
          }

          if (intent == null) {
            //Capture Photo if no image available
            if (mCM != null) {
              results = new Uri[]{Uri.parse(mCM)};
            }
          } else {
            String dataString = intent.getDataString();
            if (dataString != null) {
              results = new Uri[]{Uri.parse(dataString)};
            }
          }
        }
      }
      mUploadMessageArray.onReceiveValue(results);
      mUploadMessageArray = null;
    } else {
      if (requestCode == FCR) {
        if (null == mUploadMessage) return;
        Uri result = intent == null || resultCode != Activity.RESULT_OK ? null : intent.getData();
        mUploadMessage.onReceiveValue(result);
        mUploadMessage = null;
      }
    }
  }

  // Create an image file
  private File createImageFile() throws IOException {
    @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
    String imageFileName = "img_" + timeStamp + "_";
    File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
    return File.createTempFile(imageFileName, ".jpg", storageDir);
  }
}
