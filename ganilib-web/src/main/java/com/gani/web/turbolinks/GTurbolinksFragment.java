package com.gani.web.turbolinks;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.basecamp.turbolinks.TurbolinksView;
import com.gani.lib.http.GImmutableParams;
import com.gani.lib.screen.GFragment;
import com.gani.web.PathSpec;
import com.gani.web.R;

public abstract class GTurbolinksFragment extends GFragment {
  public static final String EXTRA_PATH_SPEC = "specPath";
  public static final String EXTRA_PARAMS = "params";

//  public static final String ARG_URL = "url";

  private GTurbolinks turbolinks;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//    View fragmentLayout = inflater.inflate(R.layout.common_fragment, null);
//
//    LinearLayout matchParentLayout = (LinearLayout) fragmentLayout.findViewById(R.id.container);
//
//    matchParentLayout.addView(new GWebView(getContext(), this).load(args().getString(ARG_URL)));

//    PathSpec pathSpec = (PathSpec) activity.getIntent().getSerializableExtra(EXTRA_PATH_SPEC);
    PathSpec pathSpec = (PathSpec) args().getSerializable(EXTRA_PATH_SPEC);
    String title = pathSpec.getTitle();
    if (title != null) {
      // For some reason, for dialog activity, this has to be before super.onCreateForScreen()
      getActivity().setTitle(title);
    }

    View fragmentLayout = inflater.inflate(R.layout.screen_turbolinks, null);

    GImmutableParams params = args().getParams(EXTRA_PARAMS);

    this.turbolinks = createTurbolinks((GTurbolinksSupportActivity) getActivity(), ((TurbolinksView) fragmentLayout.findViewById(R.id.turbolinks_view)),
        pathSpec.getPath() + ((params == null) ? "" : "?" + params.asQueryString()));
    turbolinks.visit();

    return fragmentLayout;
  }

//  @Override
//  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//    super.onActivityCreated(savedInstanceState);
//    disableRefreshPull();
//  }

  protected abstract GTurbolinks createTurbolinks(GTurbolinksSupportActivity activity, TurbolinksView view, String path);

  @Override
  protected void onRestart() {
    super.onRestart();
    turbolinks.restore();
  }
}
