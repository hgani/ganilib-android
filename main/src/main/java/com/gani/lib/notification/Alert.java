package com.gani.lib.notification;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;

import com.gani.lib.R;
import com.gani.lib.ui.Ui;

import java.io.Serializable;

// NOTE: Extends from native Activity instead of RichActivity or else we'll get errors due to font icon not initialized
// at the time this activity is launched.
public class Alert extends Activity {
  private static final String EXTRA_DISPLAYER = "strategy";

  public static Intent intent(DisplayStrategy displayer) {
    Intent intent = new Intent(Ui.context(), Alert.class);
    intent.putExtra(EXTRA_DISPLAYER, displayer);
    return intent;
  }

  public static Intent intent(int messageResId) {
    return Alert.intent(new DefaultDisplayStrategy(Ui.str(messageResId)));
  }

  public static Intent intent(String message) {
    return Alert.intent(new DefaultDisplayStrategy(message));
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getDisplayer().setAlertContent(this);
  }

  private DisplayStrategy getDisplayer() {
    return ((DisplayStrategy) getIntent().getSerializableExtra(EXTRA_DISPLAYER));
  }

  public static void display(Context context, int messageResId) {
    context.startActivity(Alert.intent(new DefaultDisplayStrategy(Ui.str(messageResId))));
  }

  public static void display(Context context, String message) {
    context.startActivity(Alert.intent(new DefaultDisplayStrategy(message)));
  }

  public static void display(Context context, DisplayStrategy displayer) {
    context.startActivity(Alert.intent(displayer));
  }

  public interface DisplayStrategy extends Serializable {
    void setAlertContent(Activity activity);
  }

  public static class DefaultDisplayStrategy implements DisplayStrategy {
    private static final long serialVersionUID = 1L;

    private String message;

    private DefaultDisplayStrategy(String message) {
      this.message = message;
    }

    public void setAlertContent(final Activity activity) {
      activity.setContentView(R.layout.alert);
      ((TextView) activity.findViewById(R.id.message)).setText(message);
      activity.findViewById(R.id.button_ok).setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          activity.finish();
        }
      });
    }
  }
}
