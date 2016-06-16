package com.zireck.reactivesockets.utils;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by andres.hernandez on 16/06/16.
 */
public class ConsumerService extends Service {

  private Thread mConsumerThread;
  private ConsumerSocket mConsumerSocket;

  @Override public int onStartCommand(Intent intent, int flags, int startId) {
    startConsuming();
    return super.onStartCommand(intent, flags, startId);
  }

  @Nullable @Override public IBinder onBind(Intent intent) {
    return null;
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
}
