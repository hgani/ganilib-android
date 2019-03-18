package com.gani.lib.http;

import android.os.AsyncTask;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class AsyncHttpTask extends AsyncTask<Void, Void, GHttpResponse> {
  // Taken from AsyncTask
//  private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
//  private static final int CORE_POOL_SIZE = Math.max(2, Math.min(CPU_COUNT - 1, 4));
  private static final int KEEP_ALIVE_SECONDS = 30;
  private static final BlockingQueue<Runnable> QUEUE = new LinkedBlockingQueue<Runnable>(128);

  private static final int MAXIMUM_POOL_SIZE = 20;
  // New threads only get added when the queue is full, so we just set core size to be the maximum right from the beginning.
  // See https://stackoverflow.com/questions/19528304/how-to-get-the-threadpoolexecutor-to-increase-threads-to-max-before-queueing
  private static final int CORE_POOL_SIZE = MAXIMUM_POOL_SIZE;

  public static final Executor THREAD_POOL_EXECUTOR;

  static {
    ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
        CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE_SECONDS, TimeUnit.SECONDS, QUEUE);
    threadPoolExecutor.allowCoreThreadTimeOut(true);
    THREAD_POOL_EXECUTOR = threadPoolExecutor;
  }

  private GHttpCallback callback;
  private HttpDelegate delegate;
  
  AsyncHttpTask(GHttpCallback callback, HttpDelegate delegate) {
    this.callback = callback;
    this.delegate = delegate;
  }
  
  public String getUrl() {
    return delegate.getUrl();
  }

  @Override
  protected GHttpResponse doInBackground(Void... unused) {
    return delegate.launch();
  }

  @Override
  protected void onPostExecute(GHttpResponse result) {
    callback.onHttpResponse(result);
//    result.handle(callback);
  }

//  public void notifyFailure(GHttpError error) {
//    callback.onHttpFailure(error);
//  }

  // NOTE: calling this to ensure that no future call to executeIfNotCanceled() will take effect
  synchronized void safeCancel() {  // Sync with executeIfNotCanceled()
  	cancel(true);
  }

  public synchronized void firstPhaseExecute() {
    // TODO: refactor so we don't need to use instanceof
    if (callback instanceof GRestCallback) {
      ((GRestCallback) callback).onBeforeHttp();
    }
  }

  public synchronized void secondPhaseExecute() {
    if (!isCancelled()) {
      executeOnExecutor(THREAD_POOL_EXECUTOR);
    }
  }

  public synchronized void executeIfNotCanceled() {
    firstPhaseExecute();
    secondPhaseExecute();
  }
}