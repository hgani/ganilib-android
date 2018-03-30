package com.gani.lib.http;

import android.os.AsyncTask;

import com.gani.lib.io.ResourceCloser;
import com.gani.lib.logging.GLog;

import java.io.IOException;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;

abstract class HttpDelegate implements Serializable {
  private static final long serialVersionUID = 1L;

  private boolean canceled;
  private String nakedUrl;
  private HttpHook hook;
  private HttpURLConnection connection;

  HttpDelegate(String nakedUrl, HttpHook hook) {
    this.nakedUrl = nakedUrl;
    this.hook = nonNull(hook);
    this.canceled = false;
  }

  private static HttpHook nonNull(HttpHook hook) {
    if (hook == null) {
      return HttpHook.DUMMY;
    }
    return hook;
  }

  final String getUrl() {
    return nakedUrl;
  }

  protected abstract String getFullUrl();

  protected abstract HttpURLConnection makeConnection() throws MalformedURLException, IOException;

  protected abstract String getMethod();

  final GHttpResponse launchIfNotCanceled() throws HttpSync.HttpCanceledException {
    if (isCanceled()) {
      throw new HttpSync.HttpCanceledException();
    }
    return launch();
  }

  final GHttpResponse launch() {
//    GHttpResponse response = new GHttpResponse(getFullUrl());
    GHttpResponse response = GHttp.instance().createHttpResponse(getFullUrl());
    GLog.d(getClass(), "Connecting to: " + getFullUrl() + " (" + getMethod() + ")");
    try {
      synchronized (this) {
        connection = makeConnection();
      }

      // NOTE: Don't put the following code inside the previous synchronized block, because its duration depends on network,
      // causing the UI thread to lock up when calling another synchronized method such as cancel().
      // Admittedly, cancel() invoked between the two synchronized blocks in this method might only apply to the first connection and therefore
      // not cancel the redirection. This behaviour is acceptable because redirection does not happen a lot in this app
      // and only executes on certain platforms as mentioned below.
      if (wasRedirected()) {
        synchronized (this) {
          // Honeycomb and lower does not seem to support redirection. This code should not execute at all on ICS because
          // redirection should have been handled.
          connection = makeRedirectConnection();
        }
      }
      response.extractFrom(connection);
    } catch (MalformedURLException e) {
      response.getError().markForNetwork(e);
    } catch (IOException e) {
      if (!isCanceled()) {
        response.getError().markForNetwork(e);
      }
    } finally {
      ResourceCloser.close(connection);
    }
    return hook.processResponse(response);
  }

  private boolean wasRedirected() throws IOException {
    int responseCode = connection.getResponseCode();
    return responseCode == HttpURLConnection.HTTP_MOVED_PERM ||
        responseCode == HttpURLConnection.HTTP_MOVED_TEMP;
  }

  private HttpURLConnection makeRedirectConnection() throws MalformedURLException, IOException {
    String redirectUrl = connection.getHeaderField("Location");
    GLog.d(getClass(), "Redirected to url: " + redirectUrl);
    return GHttp.instance().openConnection(redirectUrl, GImmutableParams.EMPTY, HttpMethod.GET);
  }


  // NOTE: disconnect() != cancel() therefore it does not to ensure that
  // it will prevent subsequent HTTP requests -- it's not the responsibility 
  // of this class 
  private final void disconnect() {
    // To prevent android.os.NetworkOnMainThreadException
    new AsyncTask<Void, Void, Void>() {
      @Override
      protected Void doInBackground(Void... params) {
        if (connection != null) {
          connection.disconnect();
        }
        return null;
      }
    };
  }

  final synchronized void cancel() {
    GLog.d(getClass(), "Canceling request: " + getFullUrl());
    this.canceled = true;
    disconnect();
  }

  private synchronized boolean isCanceled() {
    return canceled;
  }

  final void launch(AsyncHttpTask task) {
    hook.launchAsyncTask(task);
  }
}
