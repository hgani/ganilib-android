package com.gani.lib.http;

import com.gani.lib.R;
import com.gani.lib.json.GJsonObject;
import com.gani.lib.logging.GLog;
import com.gani.lib.ui.Ui;
import com.gani.lib.ui.alert.ToastUtils;

import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GHttpResponse<RR extends GRestResponse> implements Serializable {
  private static final long serialVersionUID = 1L;

  private Set<Map.Entry<String, List<String>>> headers;
  private byte[] binary;
  private String string;
  private RR restReponse;
  private String url;
  private GHttpError error;
  private Integer code;  // Could be null, e.g. if network times out

  protected GHttpResponse(String url) {
    this.url = url;
    this.error = createError();
  }
  
  public String getUrl() {
    return url;
  }
  
  public GHttpError getError() {
    return error;
  }
  
  void setError(GHttpError error) {
    this.error = error;
  }

  public boolean hasError() {
    return error.hasError();
  }

  public void setCode(int code) {
    this.code = code;
  }

  public int getCode() {
    return code;
  }

  public byte[] asBinary() {
    return binary;
  }
  
  public String asString() {
    return string;
  }
  
  public RR asRestResponse() {
    if (restReponse == null) {
      restReponse = createRestResponse(string);
    }
    return restReponse;
  }

  // To be overridden
  protected RR createRestResponse(String jsonString) {
    return (RR) new GRestResponse(jsonString, this);
  }

  // To be overridden
  protected GHttpError createError() {
    return new GHttpError.Default(this);
  }

  void extractFrom(HttpURLConnection connection) throws IOException {
    int code = connection.getResponseCode();
    setCode(code);

    this.headers = connection.getHeaderFields().entrySet();

    // Redirection (e.g. 300) should have been handled prior. 304 should not be considered an error.
    if (code > 400) {
      GLog.t(getClass(), "HTTP Code: " + code);
      error.markForCode(code);
    }

    InputStream stream = null;
    try {
      stream = connection.getInputStream();
    }
    catch (IOException e) {
      // The var `stream` will be null and handled in subsequent code.
    }

    if (stream == null) {
      stream = connection.getErrorStream();
    }

    if (stream == null) {  // Not sure if this will happen ever, but just a safe guard especially since we're dealing with API.
      this.binary = new byte[0];
    }
    else {
      this.binary = readByteArray(stream, getContentLengthForBufferring(connection));
    }
    this.string = new String(binary);

    GLog.i(getClass(), "[" + code + "]: " + string);
  }

  public Set<Map.Entry<String, List<String>>> getHeaders() {
    return headers;
  }

  //  void handle(GHttpCallback callback) {
//    if (error.hasError()) {
//      callback.onHttpFailure(error);
//      if (!displayMessage()) {
//        ToastUtils.showNormal(error.getMessage());
//      }
//    }
//    else {
//      callback.onHttpSuccess(this);
//    }
//  }

  private static int getContentLengthForBufferring(HttpURLConnection connection) {
    try {
      return Integer.parseInt(connection.getHeaderField("Content-Length"));
    }
    catch (Exception e) {
      GLog.i(GHttpResponse.class, "Using default buffer length, because we cant get content length from header");
      return Ui.integer(R.integer.data_buffer);
    }
  }

  private static byte[] readByteArray(InputStream in, int bufLen) throws IOException {
    byte[] buffer = new byte[Ui.integer(R.integer.data_buffer)];
    ByteArrayOutputStream bos = new ByteArrayOutputStream(bufLen);
    int read = in.read(buffer);
    while (read > -1) {
      bos.write(buffer, 0, read);
      try {
        read = in.read(buffer);
      }
      catch (IllegalStateException e) {
        // There has been ANRs on "java.lang.IllegalStateException: attempt to use Inflater after calling end"
        // It's not good to just let the app crash, so we should treat it like a normal IOException.
        throw new IOException(e.getMessage());
      }
    }

    bos.flush();
    byte[] data = bos.toByteArray();
    GLog.d(GHttpResponse.class, "Actual HTTP result size (in bytes): " + data.length);
    return data;
  }

  @Override
  public String toString() {
    return asString();
  }
}
