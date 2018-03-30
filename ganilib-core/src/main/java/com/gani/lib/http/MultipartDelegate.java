package com.gani.lib.http;

import com.gani.lib.logging.GLog;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

final class MultipartDelegate extends HttpDelegate {
  private static final String MESSAGE_BOUNDARY = "----------V2ymHFg03ehbqgZCaKO6jy";
  private static final long serialVersionUID = 1L;

  private GImmutableParams params;
  private Map<String, HttpAsyncMultipart.Uploadable> uploads;

  MultipartDelegate(String nakedUrl, GImmutableParams params, Map<String, HttpAsyncMultipart.Uploadable> uploads, HttpHook hook) {
    super(nakedUrl, hook);
    this.params = GImmutableParams.fromNullable(params);
    this.uploads = nonNullImmutable(uploads);
  }
  
  private Map<String, HttpAsyncMultipart.Uploadable> nonNullImmutable(Map<String, HttpAsyncMultipart.Uploadable> uploads) {
    if (uploads == null) {
    	return Collections.<String, HttpAsyncMultipart.Uploadable>emptyMap();
    }
    return uploads;
  }
  
  @Override
  protected String getMethod() {
  	return HttpMethod.POST.name();
  }
  
  @Override
  protected HttpURLConnection makeConnection() throws IOException {
    HttpURLConnection connection = GHttp.instance().openConnection(getFullUrl(), params, HttpMethod.POST);
  	connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + MESSAGE_BOUNDARY);
    connection.setDoOutput(true);

    byte[] data = createMultipartData(params, uploads);
    GLog.d(getClass(), "Multipart data: " + data.length);
    // NOTE: Not sure whether using setFixedLengthStreamingMode() will result in error 400 if the server tells the client to
    // redirect (See relevant comment on PostDelegate).
    connection.setFixedLengthStreamingMode(data.length);
    connection.getOutputStream().write(data);
    return connection;
  }

  private static byte[] createMultipartData(GImmutableParams params, Map<String, HttpAsyncMultipart.Uploadable> uploads) throws IOException {
    String endBoundary = "\r\n--" + MESSAGE_BOUNDARY + "--\r\n";

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    OutputStreamWriter osw = new OutputStreamWriter(baos);

    osw.write("--" + MESSAGE_BOUNDARY + "\r\n");
    // Not sure why casting is required.
    for (Map.Entry<String, Object> entry : (Set<Map.Entry<String, Object>>) params.entrySet()) {
      // NOTE: For now we just assume all values are String. We don't support String[].
      osw.write("Content-Disposition: form-data; name=\"" + entry.getKey() + 
          "\"\r\n\r\n" + entry.getValue() + "\r\n--" + MESSAGE_BOUNDARY + "\r\n");
    }

    for (Map.Entry<String, HttpAsyncMultipart.Uploadable> entry : uploads.entrySet()) {
      String fileField = entry.getKey();
      HttpAsyncMultipart.Uploadable upload = entry.getValue();

      osw.write("Content-Disposition: form-data; name=\"" + fileField +
        "\"; filename=\"" + upload.getFileName() + "\"\r\nContent-Type: " + upload.getMimeType() + "\r\n\r\n");
      osw.flush();
      baos.write(upload.getData());
      osw.write(endBoundary);
    }

    osw.flush();
    return baos.toByteArray();
  }

  @Override
  protected String getFullUrl() {
    return getUrl();
  }
}
