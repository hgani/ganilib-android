package com.gani.lib.screen;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.gani.lib.R;
import com.gani.lib.analytics.Tracker;
import com.gani.lib.analytics.TrackingSpec;
import com.gani.lib.logging.GLog;
import com.gani.lib.model.GBundle;
import com.gani.lib.ui.ProgressIndicator;
import com.gani.lib.ui.Ui;
import com.gani.lib.utils.LocationManager;

import java.io.Serializable;
import java.util.List;

public class GActivity extends AppCompatActivity implements RichContainer {
  protected Tracker tracker;
  private IScreenView container;
  private Boolean topNavigation;
  private Bundle arguments;

  // Create two instances so that we can safely unregister them separately. Sharing the same getInstance will cause
  // problem because calling unregisterContentObserver() will only unregister one of them.
//  private ScreenView.UserInfoUpdater navMenuUpdater1;
//  private ScreenView.NavigationMenuUpdater navMenuUpdater2;
//
//  private ScreenView.NudgeUpdater nudgeUpdater;

//
//  @Override
//  protected void onCreateForScreen(Bundle savedInstanceState) {
//    super.onCreateForScreen(savedInstanceState);
//
//    this.arguments = new GBundle(getIntent());
//    this.tracker = new Tracker(savedInstanceState);
//    this.container = new GScreenView(this);
////    this.navMenuUpdater1 = container.new UserInfoUpdater();
////    this.navMenuUpdater2 = container.new NavigationMenuUpdater();
////    this.nudgeUpdater = container.new NudgeUpdater();
//
//    super.setContentView(container);
//    setSupportActionBar(container.getToolbar());
//
////    DbUtils.registerObserver(GDataProvider.User.URIS, navMenuUpdater1);
////    DbUtils.registerObserver(GDataProvider.NotificationSummary.URIS, navMenuUpdater2);
////
////    DbUtils.registerObserver(GDataProvider.KeyValue.URIS, nudgeUpdater);
//  }

  private void initOnCreate(Bundle savedInstanceState) {
    Intent intent = getIntent();
    this.arguments = (intent.getExtras() == null) ? new Bundle() : intent.getExtras();  // Intent may not contain extras
    this.tracker = new Tracker(savedInstanceState);
  }

  protected final void onCreateForScreen(Bundle savedInstanceState, GScreenView container) {
    super.onCreate(savedInstanceState);
    initOnCreate(savedInstanceState);
    this.container = container;

    super.setContentView(container);
    setSupportActionBar(container.getToolbar());
  }

  protected final void onCreateForDialog(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    initOnCreate(savedInstanceState);
//    this.container = new GScreenView(this) {
//      @Override
//      protected void initNavigation(boolean topNavigation, ActionBar actionBar) {
//        // Not relevant for dialog.
//      }
//    };
    this.container = new IScreenView(this) {
      @Override
      protected void initNavigation(boolean topNavigation, ActionBar actionBar) {
        // Not applicable to dialog
      }

      @Override
      public void setBody(int resId) {
        LayoutInflater.from(getContext()).inflate(resId, this);
      }

      @Override
      public Toolbar getToolbar() {
        return null;  // Not applicable to dialog
      }

      @Override
      public void openDrawer() {
        // Not applicable to dialog
      }
    };
    super.setContentView(container);
  }

  public final ActionBar getNavBar() {
    return getSupportActionBar();
  }

  public GBundle args() {
    return new GBundle(arguments);
  }

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    throw new UnsupportedOperationException("Should be overridden in child class");
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();

//    App.nullSafeUnregisterContentObserver(navMenuUpdater1);
//    App.nullSafeUnregisterContentObserver(navMenuUpdater2);
//    App.nullSafeUnregisterContentObserver(nudgeUpdater);
  }

  @Override
  protected void onStart() {
    super.onStart();
    tracker.start(this);
    tracker.track(getTrackingSpec());
  }
  
  @Override
  protected void onStop() {
    tracker.stop(this);
    super.onStop();
  }
  
  protected TrackingSpec getTrackingSpec() {
    return TrackingSpec.DO_NOTHING;
  }

  public final void setOkResult(String resultKey, Serializable resultValue) {
    GLog.t(getClass(), "setOkResult: " + RESULT_OK);

    Intent extras = new Intent();
    extras.putExtra(resultKey, resultValue);
    setResult(RESULT_OK, extras);


//    Intent data = new Intent();
//    data.putExtra(RESULT_DATA, result);
//    setResult(RESULT_OK,data);
//    finish();
  }
  
  public final void finish(String resultKey, Serializable resultValue) {
    setOkResult(resultKey, resultValue);
    finish();
  }
  
  @Override
  public final GActivity getGActivity() {
    return this;
  }

  public final Context getContext() {
    return this;
  }

  public void updateBadge(int count) {
    if (container instanceof GScreenView) {
      ((GScreenView) container).updateBadge(count);
    }
  }
  
  public final TextView getLabel(int resId) {
    return (TextView) findViewById(resId);
  }

  public final Button getButton(int resId) {
    return (Button) findViewById(resId);
  }

  private void setContent(int resId) {
    container.initNavigation(topNavigation, getSupportActionBar());
    container.setBody(resId);
  }

  public void setContentWithToolbar(int resId, boolean topNavigation) {
    this.topNavigation = topNavigation;
    setContent(resId);
    container.getToolbar().setVisibility(View.VISIBLE);
  }

  public void setContentWithoutToolbar(int resId) {
    this.topNavigation = false;
    setContent(resId);
  }

  @Override
  public final void setContentView(View view) {
    throw new UnsupportedOperationException("Use either setContentWithToolbar() or setContentWithoutToolbar()");
  }

  @Override
  public final void setContentView(View view, ViewGroup.LayoutParams params) {
    setContentView(view);
  }

  @Override
  public final void setContentView(@LayoutRes int layoutResID) {
    setContentView(null);
  }


  // Unfortunately when setting theme programatically, the background won't be transparent. See http://stackoverflow.com/questions/15455979/translucent-theme-does-not-work-when-set-programmatically-on-android-2-3-3-or-4
  // So we'll stick to setting theme in manifest and calling the right dialog method here. Ideally both can be done in this method.
  //
  // In the future, if we want to look into this again, beware that theming is time consuming and causes weird errors (with useless stacktraces).
  //
  // Put this in themes.xml:
  // <style name="FakeDialog" parent="Theme.AppCompat.Light.Dialog">
  //   <item name="windowNoTitle">true</item>
  // </style>
  public void setContentForDialog(int resId) {
    this.topNavigation = false;
    setContent(resId);
  }

  protected final void setSubtitle(String subtitle) {
    getSupportActionBar().setSubtitle(subtitle);
  }


//  protected final Bundle rawArguments() {
//    return arguments;
//  }



  ///// Fragment management /////

  private void setFragment(GFragment fragment, Bundle savedInstanceState) {
    container.initNavigation(topNavigation, getSupportActionBar());

    if (savedInstanceState == null) {  // During initial setup, plug in the fragment
      fragment.setArguments(getIntent().getExtras());
      // R.id.screen_body has to be unique or else we might be attaching the fragment to the wrong view
      getSupportFragmentManager().beginTransaction().add(R.id.screen_body, fragment).commit();
    }
  }

  public void setFragmentWithoutToolbar(GFragment fragment, Bundle savedInstanceState) {
    this.topNavigation = false;
    setFragment(fragment, savedInstanceState);
  }

  public void setFragmentWithToolbar(GFragment fragment, boolean topNavigation, Bundle savedInstanceState) {
    this.topNavigation = topNavigation;
    setFragment(fragment, savedInstanceState);
    container.getToolbar().setVisibility(View.VISIBLE);
  }

  public void replaceFragment(GFragment fragment) {
    fragment.setArguments(getIntent().getExtras());
    // R.id.screen_body has to be unique or else we might be attaching the fragment to the wrong view
    getSupportFragmentManager().beginTransaction().replace(R.id.screen_body, fragment).commit();
  }

  public GFragment getMainFragment() {
    return (GFragment) getSupportFragmentManager().findFragmentById(R.id.screen_body);
  }

  private boolean shouldRecreateFragmentOnNewIntent = false;

  protected GFragment createNewIntentFragment() {
    return null;
  }

  @Override
  protected void onResume() {
    super.onResume();
    if (shouldRecreateFragmentOnNewIntent) {
      GFragment fragment = createNewIntentFragment();
      if (fragment != null) {
        replaceFragment(fragment);
      }
      shouldRecreateFragmentOnNewIntent = false; // Ensure single execution
    }
  }

  @Override
  protected void onNewIntent(Intent intent) {
    super.onNewIntent(intent);

    setIntent(intent);
    shouldRecreateFragmentOnNewIntent = true;
  }

  /////



  ///// Permission /////

  public static final int PERMISSION_LOCATION = 40000;

  // NOTE: This method also gets called when the user says no.
  public void onRequestPermissionsResult(int reqCode, String permissions[], int[] results) {
    switch (reqCode) {
      case PERMISSION_LOCATION: {
        LocationManager.instance().updateLocationSilently(this);
        return;
      }
      default:
        // Nothing to do
    }
  }

  /////

  public ProgressIndicator getCircularProgressIndicator() {
    final View indicator = findViewById(R.id.circular_progress);
    return new ProgressIndicator() {
      @Override
      public void showProgress() {
        indicator.setVisibility(View.VISIBLE);
      }

      @Override
      public void hideProgress() {
        indicator.setVisibility(View.GONE);
      }
    };
  }

  // This is a last resort indicator that should be used only when at the time, there's no better simple solution.
  public ProgressIndicator getGenericProgressIndicator() {
    final View indicator = findViewById(R.id.progress_common);
    return new ProgressIndicator() {
      @Override
      public void showProgress() {
        indicator.setVisibility(View.VISIBLE);
      }

      @Override
      public void hideProgress() {
        indicator.setVisibility(View.GONE);
      }
    };
  }


  ///// Menu /////

  @Override
  public boolean onOptionsItemSelected(MenuItem item){
    switch (item.getItemId()) {
      case android.R.id.home:
        if (topNavigation) {
          container.openDrawer();
        }
        else {
          onBackPressed();  // Going up is more similar to onBackPressed() than finish(), esp becoz the former can have pre-finish check
        }
        return true;
    }

    return super.onOptionsItemSelected(item);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
//    if (Build.INSTANCE.shouldShowTestFeatures()) {
//      new GMenu(menu, this).addSecondary(R.string.menu_nav_log, GMenu.ORDER_COMMON, new GMenu.OnClickListener() {
//        @Override
//        protected void onClick(MenuItem menuItem) {
//          startActivity(ScreenReadOnly.intent("Log", GLog.Reader.getLog()));
//        }
//      });
//    }

    return super.onCreateOptionsMenu(menu);
  }

  /////


  // TODO: Could in theory use MethodHandles.lookup().lookupClass() in Java 1.7 but MethodHandles doesn't exist in Android
//  public final static Intent intent() {
//    return new Intent(App.context(), ScreenVoteInfo.class);
//  }
//  
//  
//

  public static IntentBuilder intentBuilder(Class<? extends GActivity> cls) {
    return new IntentBuilder(cls);
  }

  public static class IntentBuilder {
    private Intent intent;

    IntentBuilder(Class<? extends GActivity> cls) {
      this.intent = new Intent(Ui.context(), cls);
    }

    public IntentBuilder withFlags(int flags) {
      intent.addFlags(flags);
      return this;
    }

    public <T> IntentBuilder withArg(String key, T[] value) {
      intent.putExtra(key, new GBundle.ArrayWrapper(value));
      return this;
    }

    public IntentBuilder withArg(String key, Serializable value) {
      intent.putExtra(key, value);
      return this;
    }

    public IntentBuilder withArg(String key, List value) {
      intent.putExtra(key, (Serializable) value);
      return this;
    }

    public Intent getIntent() {
      return intent;
    }
  }
}
