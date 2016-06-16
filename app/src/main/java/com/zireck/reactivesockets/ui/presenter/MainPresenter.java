package com.zireck.reactivesockets.ui.presenter;

import com.zireck.reactivesockets.ui.view.MainView;

/**
 * Created by andres.hernandez on 13/06/16.
 */
public class MainPresenter {

  private MainView mView;
  private boolean mIsProducing = false;
  private boolean mIsConsuming = false;

  public MainPresenter(MainView view) {
    mView = view;
  }

  public void onClickProducer() {
    if (!mIsProducing) {
      mView.startProducerService();
      mView.notifyProducerStarted();
    } else {
      mView.stopProducerService();
      mView.notifyProducerStopped();
    }

    mIsProducing = !mIsProducing;
  }

  public void onClickConsumer() {
    if (!mIsConsuming) {
      mView.startConsumerService();
      mView.notifyConsumerStarted();
    } else {
      mView.stopConsumerService();
      mView.notifyConsumerStopped();
    }

    mIsConsuming = !mIsConsuming;
  }

}
