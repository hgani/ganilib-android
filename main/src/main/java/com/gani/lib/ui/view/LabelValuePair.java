package com.gani.lib.ui.view;

public class LabelValuePair<T> {
  private String label;
  private T value;

  public LabelValuePair(String label, T value) {
    this.label = label;
    this.value = value;
  }

  public String getLabel() {
    return label;
  }

  public T getValue() {
    return value;
  }

  @Override
  public String toString() {
    return label;
  }
}
