package com.zireck.reactivesockets.utils;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by andres.hernandez on 13/06/16.
 */
public class ProducerService extends Service {

  private Thread mProducerThread;
  private ProducerSocket mProducerSocket;

  @Override public int onStartCommand(Intent intent, int flags, int startId) {
    startProducing();
    return super.onStartCommand(intent, flags, startId);
  }

  @Nullable @Override public IBinder onBind(Intent intent) {
    return null;
  }

  private void startProducing() {
    mProducerThread = new Thread(new Runnable() {
      @Override public void run() {
        mProducerSocket = new ProducerSocket();
        mProducerSocket.listen();
      }
    });

    mProducerThread.start();
  }

  @Override public void onDestroy() {
    super.onDestroy();
    if (mProducerSocket != null) {
      mProducerSocket.stopListening();
      mProducerSocket = null;
    }
  }
}
