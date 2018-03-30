package com.gani.lib.screen;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.gani.lib.R;
import com.gani.lib.analytics.TrackingSpec;
import com.gani.lib.model.GBundle;
import com.gani.lib.ui.ProgressIndicator;
import com.gani.lib.ui.menu.GMenu;

public class GFragment extends Fragment implements RichContainer, ProgressIndicator {
  private Bundle arguments;
  private boolean firstVisit;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setHasOptionsMenu(true);

    // Arguments may be null if the containing screen doesn't have any extras to pass on.
    this.arguments = (getArguments() == null) ? new Bundle() : getArguments();
//    this.arguments = new GBundle((getArguments() == null) ? new Bundle() : getArguments());
  }

//  protected final Bundle rawArguments() {
//    return arguments;
//  }

  protected GBundle args() {
    return new GBundle(arguments);
  }

  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    firstVisit = true;

    initRefreshView(new SwipeRefreshLayout.OnRefreshListener() {
      @Override
      public void onRefresh() {
        GFragment.this.onRefresh();
      }
    });
  }

  @Override
  public void onStart() {
    super.onStart();
  }
  
  @Override
  public void onStop() {
    super.onStop();
  }

  @Override
  public void onResume() {
    super.onResume();

    // Mimic activity's onRestart(). See http://stackoverflow.com/questions/35039512/android-what-to-use-instead-of-onrestart-in-a-fragment
    if (firstVisit) {
      firstVisit = false;
    }
    else {
      onRestart();
    }
  }

  protected void onRestart() {
    // To be overridden.
  }

  protected TrackingSpec getTrackingSpec() {
    return TrackingSpec.DO_NOTHING;
  }

  @Override
  public GActivity getGActivity() {
    return (GActivity) getActivity();
  }



  ///// Pull to refresh /////
  // NOTE:
  // - Implement this in Fragment instead of Activity to ensure it works well on dual panel
  // - These methods can only be called when view has been initialized, e.g. in onActivityCreated()

  public void showProgress() {
    // Use post() so that this works when called from onCreateForScreen(), which is a common scenario (i.e. auto-populating list view)
    // See http://www.androidhive.info/2015/05/android-swipe-down-to-refresh-listview-tutorial/
    if(getRefreshView() != null) {
      getRefreshView().post(new Runnable() {
        @Override
        public void run() {
          if (getView() != null) {  // Still on-screen
            getRefreshView().setRefreshing(true);
          }
        }
      });
    }
  }

  public void hideProgress() {
    try {
      getRefreshView().setRefreshing(false);
    }
    catch (NullPointerException e) {
      // Might happen when screen has been closed.
    }
  }

  protected final void disableRefreshPull() {
    getRefreshView().setEnabled(false);
  }

  private SwipeRefreshLayout getRefreshView() {
    try {
      return ((SwipeRefreshLayout) getView().findViewById(R.id.layout_refresh));
    }
    catch (NullPointerException e){
      // Might happen when screen has been closed.
    }
    return null;
  }

  public void initRefreshView(SwipeRefreshLayout.OnRefreshListener listener) {
    SwipeRefreshLayout refreshLayout = getRefreshView();
    if (refreshLayout != null) {
      refreshLayout.setOnRefreshListener(listener);
    }
  }

  protected void onRefresh() {
    // To be overridden
  }

  private static final int RESOURCE_INVALID = 0;

  protected int getRefreshStringId() {
    return RESOURCE_INVALID;  // Override to show a refresh menu item
  }

  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater){
    int strId = getRefreshStringId();
    if (strId != RESOURCE_INVALID) {
      new GMenu(menu).addSecondary(strId, GMenu.ORDER_SPECIFIC, new GMenu.OnClickListener() {
        @Override
        protected void onClick(MenuItem menuItem) {
          onRefresh();
        }
      });
    }
  }

  /////
}
