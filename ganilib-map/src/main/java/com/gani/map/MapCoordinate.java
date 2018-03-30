package com.gani.map;

import android.location.Location;

import com.gani.lib.model.Coordinate;
import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

// A Serializable alternative to LatLng
public class MapCoordinate extends Coordinate {
  public MapCoordinate(double latitude, double longitude) {
    super(latitude, longitude);
  }

  public LatLng toLatLng() {
    return new LatLng(getLatitude(), getLongitude());
  }
}
