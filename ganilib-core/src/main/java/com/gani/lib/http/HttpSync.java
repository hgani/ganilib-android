package com.gani.lib.http;



public interface HttpSync<HR extends GHttpResponse> {
  String getUrl();
  HR execute() throws HttpSyncException, HttpCanceledException;
  void cancel();
  
  
  
  class HttpSyncException extends Exception {
    private static final long serialVersionUID = 1L;
    private GHttpError error;
    
    HttpSyncException(GHttpError error) {
      this.error = error;
    }

    public GHttpError getError() {
      return error;
    }

    @Override
    public String getMessage() {
      return error.getMessage();
    }
  }

  // Just a tagging exception.
  class HttpCanceledException extends Exception {
    private static final long serialVersionUID = 1L;
  }
}
