package com.zireck.reactivesockets.ui.view;

import rx.Subscriber;

/**
 * Created by andres.hernandez on 13/06/16.
 */
public interface MainView {

  void startProducerService();
  void stopProducerService();
  void notifyProducerStarted();
  void notifyProducerStopped();

  void startConsumerService();
  void stopConsumerService();
  void notifyConsumerStarted();
  void notifyConsumerStopped();

  void setSubscriber(Subscriber subscriber);
  void append(String text);
}
