package com.gani.lib.json;

import com.gani.lib.collection.SelfTruncatingSet;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GJson {
  private static Gson GSON = new GsonBuilder().registerTypeAdapter(SelfTruncatingSet.class, new SelfTruncatingSet.GsonSerializer()).create();
  private static Gson DEFAULT_GSON = new Gson();

  public static Gson gson() {
    return GSON;
  }

  public static Gson defaultGson() {
    return DEFAULT_GSON;
  }

}
