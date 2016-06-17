package com.zireck.reactivesockets.utils;

import android.util.Log;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by andres.hernandez on 16/06/16.
 */
public class ConsumerSocket {

  private static final int SOCKET_PORT = 9999;

  private final ConsumerServiceCallback mCallback;

  private Socket mSocket;
  BufferedReader mBufferedReader;
  private boolean mConsuming;
  private Observable mObservable;

  public ConsumerSocket(ConsumerServiceCallback callback) {
    mCallback = callback;
  }

  public Observable<Integer> startConsuming() {
    mConsuming = true;

    try {
      connect();
      consume();
    } catch (IOException e) {
      e.printStackTrace();
    }

    mObservable = Observable.empty();
    return mObservable;
  }

  public void stopConsuming() {
    mConsuming = false;

    try {
      if (mBufferedReader != null) {
        mBufferedReader.close();
      }
      if (mSocket != null) {
        mSocket.close();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    Log.d(getClass().getSimpleName(), "C> Manually stopped consuming");
  }

  private void connect() throws IOException {
    mSocket = new Socket("localhost", SOCKET_PORT);
  }

  private void consume() {
    Observable observable = Observable.create(new Observable.OnSubscribe<Integer>() {
      @Override public void call(Subscriber<? super Integer> subscriber) {
        try {
          mBufferedReader = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
          String message = new String();
          while (!mSocket.isClosed() && message != null && mConsuming) {
            message = mBufferedReader.readLine();
            Log.d(getClass().getSimpleName(), "C> Reading: " + message);
            subscriber.onNext(Integer.parseInt(message));
          }
          subscriber.onCompleted();
          Log.d(getClass().getSimpleName(), "C> Stopped consuming");
        } catch (IOException e) {
          e.printStackTrace();
          subscriber.onError(e);
        }
      }
    });

    mCallback.startSubscriptionWith(observable);
  }
}
