package com.gani.lib.utils;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.gani.lib.logging.GLog;
import com.gani.lib.screen.GActivity;
import com.gani.lib.ui.Ui;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

public class LocationManager {
  private static final String PERMISSION = Manifest.permission.ACCESS_FINE_LOCATION;

  private static final LocationFetchingListener FETCH_LISTENER = new LocationFetchingListener() {
    @Override
    public void onSuccess(Location location) {
      GLog.t(getClass(), "Updated location: " + location);
      LocationManager.instance().location = location;
    }

    @Override
    public void onFailure() {
      // Nothing to do
    }
  };

  private Location location;

  private static final LocationManager INSTANCE = new LocationManager();

  public static LocationManager instance() {
    return INSTANCE;
  }

  public Location getLocation() {
    return location;
  }

  public Double getLatitude() {
    if (location == null) {
      return null;
    }
    return location.getLatitude();
  }

  public Double getLongitude() {
    if (location == null) {
      return null;
    }
    return location.getLongitude();
  }

  public void updateLocation(GActivity activity) {
    if (!updateLocationSilently(activity)) {
      ActivityCompat.requestPermissions(activity, new String[] { PERMISSION }, GActivity.PERMISSION_LOCATION);
    }
  }

  public boolean updateLocationSilently(GActivity activity) {
    GLog.t(getClass(), "UPDATE LOC1");
    if (LocationManager.instance().isAccessPermitted()) {
      GLog.t(getClass(), "UPDATE LOC2");
      fetchFusedLocation(activity);
      return true;
    }
    return false;
  }

  public boolean isAccessPermitted() {
    return ContextCompat.checkSelfPermission(Ui.context(), PERMISSION) == PackageManager.PERMISSION_GRANTED;
  }

  private void fetchFusedLocation(GActivity activity) {
    GLog.t(getClass(), "fetchFusedLocation3");

    FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(activity);

    client.getLastLocation()
        .addOnSuccessListener(activity, new OnSuccessListener<Location>() {
          @Override
          public void onSuccess(Location location) {
            GLog.t(getClass(), "fetchFusedLocation5");
            // Got last known location. In some rare situations this can be null.
            if (location != null) {
              GLog.t(getClass(), "fetchFusedLocation6");
              FETCH_LISTENER.onSuccess(location);
            }
            else {
              GLog.t(getClass(), "fetchFusedLocation7");
              FETCH_LISTENER.onFailure();
            }
          }
        })
        .addOnFailureListener(activity, new OnFailureListener() {
          @Override
          public void onFailure(@NonNull Exception e) {
            GLog.t(getClass(), "fetchFusedLocation8");
            FETCH_LISTENER.onFailure();
          }
        });
  }


  public interface LocationFetchingListener {
    void onSuccess(Location location);
    void onFailure();
  }
}
