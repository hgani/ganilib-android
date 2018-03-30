package com.gani.web.htmlform;

import com.gani.lib.json.GJsonObject;
import com.gani.lib.screen.GActivity;

import org.json.JSONException;

import java.lang.reflect.InvocationTargetException;

import static com.gani.lib.utils.string.StringUtils.capitalize;

public abstract class HtmlFormHandler {
  private static HtmlFormHandler instantiate(String prefix, String type) throws NotFoundException {
    try {
      Class cls = Class.forName(prefix + "." + capitalize(type));
      return (HtmlFormHandler) cls.getConstructor().newInstance();
    } catch (InstantiationException e) {
      throw new NotFoundException(e);
    } catch (IllegalAccessException e) {
      throw new NotFoundException(e);
    } catch (ClassNotFoundException e) {
      throw new NotFoundException(e);
    } catch (NoSuchMethodException e) {
      throw new NotFoundException(e);
    } catch (InvocationTargetException e) {
      throw new NotFoundException(e);
    }
  }

  public static HtmlFormHandler create(String packageName, String key) throws NotFoundException {
//    String[] keys = key.split("/");
//    if (keys.length >= 2) {
//      String prefix = keys[0];
//      String name = keys[1];
//      return instantiate(prefix, name);
//    }
//    throw new NotFoundException();

    return instantiate(packageName, key);
  }

  public static class NotFoundException extends Exception {
    NotFoundException() {
      super();
    }

    NotFoundException(Exception e) {
      super(e);
    }
  }

  public abstract void execute(GActivity activity, GJsonObject params) throws JSONException;
}
