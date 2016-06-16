package com.zireck.reactivesockets.utils;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.Subscriptions;

/**
 * Created by andres.hernandez on 16/06/16.
 */
public class ConsumerService extends Service implements ConsumerServiceCallback {

  private final IBinder mBinder = new ConsumerServiceBinder();

  private Thread mConsumerThread;
  private ConsumerSocket mConsumerSocket;

  private Subscription mSubscription = Subscriptions.empty();
  private Subscriber<Integer> mSubscriber;

  @Nullable @Override public IBinder onBind(Intent intent) {
    startConsuming();
    return mBinder;
  }

  private void startConsuming() {
    mConsumerThread = new Thread(new Runnable() {
      @Override public void run() {
        mConsumerSocket = new ConsumerSocket(ConsumerService.this);
        mConsumerSocket.startConsuming();
      }
    });

    mConsumerThread.start();
  }

  @Override public void onDestroy() {
    super.onDestroy();

    if (mConsumerSocket != null) {
      mConsumerSocket.stopConsuming();
      mConsumerSocket = null;
    }
  }

  public class ConsumerServiceBinder extends Binder {
    public ConsumerService getService() {
      return ConsumerService.this;
    }
  }

  public void setSubscriber(Subscriber<Integer> subscriber) {
    mSubscriber = subscriber;
  }

  @Override public void startSubscriptionWith(Observable observable) {
    if (observable == null) {
      Log.e(getClass().getSimpleName(), "Observable not set");
      return;
    }

    if (mSubscriber == null) {
      Log.e(getClass().getSimpleName(), "Subscriber not set");
      return;
    }

    mSubscription = observable
        .subscribeOn(Schedulers.newThread())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(mSubscriber);
  }
}
