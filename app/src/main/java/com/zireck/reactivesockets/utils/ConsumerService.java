package com.zireck.reactivesockets.utils;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by andres.hernandez on 16/06/16.
 */
public class ConsumerService extends Service {

  private final IBinder mBinder = new ConsumerServiceBinder();

  private Thread mConsumerThread;
  private ConsumerSocket mConsumerSocket;

  @Nullable @Override public IBinder onBind(Intent intent) {
    startConsuming();
    return mBinder;
  }

  private void startConsuming() {
    mConsumerThread = new Thread(new Runnable() {
      @Override public void run() {
        mConsumerSocket = new ConsumerSocket();
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
}
