package com.gani.lib.screen;

public interface RichContainer {
  // Apparently this may return null (may be when the screen has been closed or is not attached yet)
  // See: http://stackoverflow.com/questions/8215308/using-context-in-fragment
  // There are cases where null may not happen at all, so you don't always have to check.
  GActivity getGActivity();
}
