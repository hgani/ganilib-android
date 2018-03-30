package com.gani.lib.http;

public interface HttpAsync {
  String getUrl();
  HttpAsync execute();
  void cancel();
}
