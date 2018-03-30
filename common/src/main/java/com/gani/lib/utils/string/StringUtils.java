package com.gani.lib.utils.string;

import com.gani.lib.io.ResourceCloser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Collection;

public class StringUtils {
  public static String capitalize(String input) {
    return input.substring(0, 1).toUpperCase() + input.substring(1);
  }

  public static String toString(InputStream inputStream) {
    BufferedReader inputBuffer = new BufferedReader(new InputStreamReader(inputStream));
    StringBuilder outputBuffer = new StringBuilder();

    try {
      String line = null;
      while ((line = inputBuffer.readLine()) != null) {
        outputBuffer.append(line).append("\n");
      }
    }
    catch (IOException e) {
      throw new RuntimeException(e);
    }
    finally {
      ResourceCloser.close(inputBuffer);
    }

    return outputBuffer.toString();
  }

  public static void writeString(String value, OutputStream outputStream) {
    try {
      outputStream.write(value.getBytes("UTF-8"));
    }
    catch (IOException e) {
      throw new RuntimeException(e);
    }
    finally {
      ResourceCloser.close(outputStream);
    }
  }

  public static String ellipsize(String text, int maxLength) {
    if(text.length() > maxLength) {
      return text.substring(0, maxLength - 3) + "...";
    }
    else {
      return text;
    }
  }

  public static String textNoMoreThan(String text, int maxLength) {
    return (text.length() > maxLength)? text.substring(0, maxLength) : text;
  }

  // Copied from Spring Framework
  public static boolean hasLength(CharSequence str) {
    return (str != null && str.length() > 0);
  }

  // Copied from Spring Framework
  public static boolean hasText(CharSequence str) {
    if (!hasLength(str)) {
      return false;
    }
    int strLen = str.length();
    for (int i = 0; i < strLen; i++) {
      if (!Character.isWhitespace(str.charAt(i))) {
        return true;
      }
    }
    return false;
  }

  public static String join(Collection<?> collection, String separator) {
    return join(collection.toArray(), separator);
  }



  ///// Copied from org.apache.commons.lang.StringUtils /////

  public static final String EMPTY = "";

  /**
   * <p>Joins the elements of the provided array into a single String
   * containing the provided list of elements.</p>
   *
   * <p>No delimiter is added before or after the list.
   * Null objects or empty strings within the array are represented by
   * empty strings.</p>
   *
   * <pre>
   * StringUtils.join(null, *)               = null
   * StringUtils.join([], *)                 = ""
   * StringUtils.join([null], *)             = ""
   * StringUtils.join(["a", "b", "c"], ';')  = "a;b;c"
   * StringUtils.join(["a", "b", "c"], null) = "abc"
   * StringUtils.join([null, "", "a"], ';')  = ";;a"
   * </pre>
   *
   * @param array  the array of values to join together, may be null
   * @param separator  the separator character to use
   * @return the joined String, <code>null</code> if null array input
   * @since 2.0
   */
  public static String join(Object[] array, String separator) {
    if (array == null) {
      return null;
    }

    return join(array, separator, 0, array.length);
  }

  /**
   * <p>Joins the elements of the provided array into a single String
   * containing the provided list of elements.</p>
   *
   * <p>No delimiter is added before or after the list.
   * Null objects or empty strings within the array are represented by
   * empty strings.</p>
   *
   * <pre>
   * StringUtils.join(null, *)               = null
   * StringUtils.join([], *)                 = ""
   * StringUtils.join([null], *)             = ""
   * StringUtils.join(["a", "b", "c"], ';')  = "a;b;c"
   * StringUtils.join(["a", "b", "c"], null) = "abc"
   * StringUtils.join([null, "", "a"], ';')  = ";;a"
   * </pre>
   *
   * @param array  the array of values to join together, may be null
   * @param separator  the separator character to use
   * @param startIndex the first index to start joining from.  It is
   * an error to pass in an end index past the end of the array
   * @param endIndex the index to stop joining from (exclusive). It is
   * an error to pass in an end index past the end of the array
   * @return the joined String, <code>null</code> if null array input
   * @since 2.0
   */
  public static String join(Object[] array, String separator, int startIndex, int endIndex) {
    if (array == null) {
      return null;
    }
    int bufSize = (endIndex - startIndex);
    if (bufSize <= 0) {
      return EMPTY;
    }

    bufSize *= ((array[startIndex] == null ? 16 : array[startIndex].toString().length()) + 1);
    StringBuffer buf = new StringBuffer(bufSize);

    for (int i = startIndex; i < endIndex; i++) {
      if (i > startIndex) {
        buf.append(separator);
      }
      if (array[i] != null) {
        buf.append(array[i]);
      }
    }
    return buf.toString();
  }
}
