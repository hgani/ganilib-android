package com.gani.lib.model;

import android.location.Location;

import java.io.Serializable;

// A Serializable alternative to LatLng
public class Coordinate implements Serializable {
  private final double latitude;
  private final double longitude;

  public Coordinate(double latitude, double longitude) {
    this.latitude = latitude;
    this.longitude = longitude;
  }

  public double getLatitude() {
    return latitude;
  }

  public double getLongitude() {
    return longitude;
  }

  public Location toLocation() {
    Location location = new Location("");
    location.setLatitude(latitude);
    location.setLongitude(longitude);
    return location;
  }
}
