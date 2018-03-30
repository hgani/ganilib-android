package com.gani.lib.http;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class UrlUtils {
//  public static String paramMapToString(Map<String, Object> paramMap) {
//    StringBuilder buffer = new StringBuilder();
//
//    for (Map.Entry<String, Object> entry : paramMap.entrySet()) {
//      if (entry.getValue() == null) {
//        GLog.e(UrlUtils.class, "Null param value for key " + entry.getKey());
//      }
//      else {
//        if (entry.getValue() instanceof String) {
//          buffer.append("&").append(entry.getKey()).append("=").append(encodeUrl((String) entry.getValue()));
//        }
//        else {
////          // Note that when the array is empty, this is skipped entirely or else it will pass an array with blank string to the server
////          for (String value : (String[]) entry.getValue()) {
////            buffer.append("&").append(entry.getKey()).append("=").append(encodeUrl(value));
////          }
//          String[] values = (String[]) entry.getValue();
//          if (values.length > 0) {
//            for (String value : values) {
//              buffer.append("&").append(entry.getKey()).append("=").append(encodeUrl(value));
//            }
//          }
//          else {
//            // This solves 2 issues:
//            // - Allow server implementation to be more predictable as the param key (e.g. params[handshake][key]) always exists.
//            // - Even more important is that if this is the only param key (e.g. params[handshake][key]), not passing this will make the whole param (params[handshake]) to be missing.
//            //
//            // The only drawback is that in Rails, this will be received as `[""]` (an array with one empty string), but this can be consistently solved as it also applies to web form.
//            buffer.append("&").append(entry.getKey()).append("=");
//          }
//        }
//      }
//    }
//
//    return (buffer.length() == 0)? "" : buffer.substring(1);
//  }

  public static String encodeUrl(String url) {
    try {
      return URLEncoder.encode(url, "UTF-8");
    }
    catch (UnsupportedEncodingException uee) {
      throw new IllegalArgumentException(uee);
    }
  }

  public static String extractHostOnly(String url) throws MalformedURLException {
    return (url == null)? null : new URL(url).getHost();
  }
  
  public static String prefixWithHttpIfNotAlready(String url) {
    return (url.startsWith("http"))? url : "http://" + url;
  }

  /**
   * Simple validation method good enough to prevent common mistakes, but not suitable for cases where strict URL conformity is required
   */
  public static boolean isValid(String url) {
    return (url.indexOf(" ") < 0) && // contains no space
        (url.indexOf("://") == url.lastIndexOf("://")) && // contains protocol only once
        url.matches("(ftp|http|https):\\/\\/(\\w+:{0,1}\\w*@)?(\\S+)(:[0-9]+)?(\\/|\\/([\\w#!:.?+=&%@!\\-\\/]))?");
  }

  public static String campaignify(String original, String source, String medium, String campaign) {
    String url = addParameter(original, "utm_source", source);
    url = addParameter(url, "utm_medium", medium);
    url = addParameter(url, "utm_campaign", campaign);
    return url;
  }
  
  public static String addParameter(String URL, String name, String value) {
    int qpos = URL.indexOf('?');
    int hpos = URL.indexOf('#');
    char sep = qpos == -1 ? '?' : '&';
    String seg = sep + encodeUrl(name) + '=' + encodeUrl(value);
    return hpos == -1 ? URL + seg : URL.substring(0, hpos) + seg + URL.substring(hpos);
  }
}
