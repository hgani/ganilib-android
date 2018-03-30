package com.gani.lib.http;

import java.util.Map;


public class HttpAsyncMultipart implements HttpAsync {
  private MultipartDelegate delegate;
  private AsyncHttpTask asyncTask;

  public HttpAsyncMultipart(String nakedUrl, GImmutableParams params, Map<String, Uploadable> uploads, HttpHook hook, GHttpCallback callback) {
    this.delegate = new MultipartDelegate(nakedUrl, params, uploads, hook);
    this.asyncTask = new AsyncHttpTask(callback, delegate);
  }

	@Override
  public String getUrl() {
    return delegate.getUrl();
  }

	@Override
  public HttpAsync execute() {
    delegate.launch(asyncTask);
    return this;
  }

	@Override
  public void cancel() {
		delegate.cancel();
    asyncTask.safeCancel();
  }
	

	

  public static class Uploadable {
    private String fileName;
    private String mimeType;
    private byte[] data;
    
    public Uploadable(String fileName, String mimeType, byte[] data) {
      this.fileName = fileName;
      this.mimeType = mimeType;
      this.data = data;
    }
    
    public String getFileName() {
      return fileName;
    }

    public String getMimeType() {
      return mimeType;
    }

    public byte[] getData() {
      return data;
    }
  }
}
