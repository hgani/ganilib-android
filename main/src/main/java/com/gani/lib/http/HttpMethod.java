package com.gani.lib.http;

public enum HttpMethod {
  POST {
    @Override
    public HttpAsync async(String url, GImmutableParams params, GHttpCallback callback) {
      return new HttpAsyncPost(url, params, HttpHook.DUMMY, POST, callback);
    }
  },
  PATCH {
    @Override
    public HttpAsync async(String url, GImmutableParams params, GHttpCallback callback) {
      return new HttpAsyncPost(url, params, HttpHook.DUMMY, PATCH, callback);
    }
  },
  DELETE {
    @Override
    public HttpAsync async(String url, GImmutableParams params, GHttpCallback callback) {
      return new HttpAsyncPost(url, params, HttpHook.DUMMY, DELETE, callback);
    }
  },
  GET {
    @Override
    public HttpAsync async(String url, GImmutableParams params, GHttpCallback callback) {
      return new HttpAsyncGet(url, params, HttpHook.DUMMY, callback);
    }
  },
  PUT {
    @Override
    public HttpAsync async(String url, GImmutableParams params, GHttpCallback callback) {
      return new HttpAsyncPost(url, params, HttpHook.DUMMY, PUT, callback);
    }
  };

  public abstract HttpAsync async(String url, GImmutableParams params, GHttpCallback callback);

  public static HttpMethod from(String method) {
    switch (method) {
      case "patch":
        return PATCH;
      case "put":
        return PUT;
      case "delete":
        return DELETE;
      default:
        return POST;
    }
  }

  public static HttpMethod from(GParams params) {
    String method = (String) params.get("_method");
    if (method == null) {
      return POST;
    }
    return from(method);
  }


}
