package com.gani.lib.ui;

public interface ProgressIndicator {
  void showProgress();
  void hideProgress();


  public static final ProgressIndicator NULL = new ProgressIndicator() {
    @Override
    public void showProgress() {
      // Do nothing
    }

    @Override
    public void hideProgress() {
      // Do nothing
    }
  };
}
