package com.zireck.reactivesockets.utils;

import rx.Observable;

/**
 * Created by andres.hernandez on 16/06/16.
 */
public interface ConsumerServiceCallback {
  void startSubscriptionWith(Observable observable);
}