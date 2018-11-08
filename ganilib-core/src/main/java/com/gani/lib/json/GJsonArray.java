package com.gani.lib.json;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public abstract class GJsonArray<JO extends GJsonObject> implements Iterable<JO> {
  private JO[] elements;

  protected GJsonArray(String str) throws JSONException {
    this(new JSONArray(str));
  }

  public GJsonArray(JO[] elements) {
    this.elements = elements;
  }

  public GJsonArray(JSONArray backend) {
    List<JSONObject> temp = new ArrayList<>();
    for (int i = 0; i < backend.length(); ++i) {
      try {
        temp.add(backend.getJSONObject(i));
      }
      catch (JSONException e) {
        // This shouldn't happen.
      }
    }

    this.elements = createArray(temp.size());
    for (int i = 0; i < temp.size(); ++i) {
      elements[i] = createObject(temp.get(i));
    }

//    for (int i = 0; i < elements.length; ++i) {
////      elements[i] = new GJsonObject(backend.getJSONObject(i));
//      elements[i] = createObject(backend.getJSONObject(i));
//    }
  }

  protected abstract JO[] createArray(int length);
  protected abstract JO createObject(JSONObject object);

  public int size() {
    return elements.length;
  }

  public boolean isEmpty() {
    return elements.length == 0;
  }

  @Override
  public String toString() {
    return Arrays.toString(elements);
  }

  public List<JO> toList() {
    return new LinkedList<>(Arrays.asList(elements));
  }

  public Iterator<JO> iterator() {
    return new Iterator<JO>() {
      private int index = 0;

      @Override
      public boolean hasNext() {
        return index < elements.length;
      }

      @Override
      public JO next() {
        return elements[index++];
      }

      @Override
      public void remove() {
        throw new UnsupportedOperationException();
      }
    };
  }

//  public JArray ungroup() throws JSONException {
//    List<JObject> objects = new LinkedList<>();
//    for (JObject group : this) {
//      JArray arr = group.getArray(ParamKeys.LIST);
//
//        for (JObject obj : arr) {
//          objects.add(obj);
//        }
//    }
//
//    return new JArray(objects.toArray(new JObject[objects.size()]));
////    return insertRows(URIS, rows);
//  }


  public static class Default extends GJsonArray<GJsonObject.Default> {
    public Default(String str) throws JSONException {
      super(str);
    }

    public Default(JSONArray array) {
      super(array);
    }

    @Override
    protected GJsonObject.Default[] createArray(int length) {
      return new GJsonObject.Default[length];
    }

    @Override
    protected GJsonObject.Default createObject(JSONObject object) {
      return new GJsonObject.Default(object);
    }
  }
}
