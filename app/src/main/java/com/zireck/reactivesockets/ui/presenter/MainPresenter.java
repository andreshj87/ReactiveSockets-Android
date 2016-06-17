package com.zireck.reactivesockets.ui.presenter;

import android.util.Log;
import com.zireck.reactivesockets.ui.view.MainView;
import rx.Subscriber;

/**
 * Created by andres.hernandez on 13/06/16.
 */
public class MainPresenter {

  private MainView mView;
  private boolean mIsProducing = false;
  private boolean mIsConsuming = false;
  private Subscriber<Integer> mSubscriber;

  public MainPresenter(MainView view) {
    mView = view;
    initSubscriber();
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

  public void onServiceConnected() {
    mView.setSubscriber(mSubscriber);
  }

  private void initSubscriber() {
    mSubscriber = new Subscriber<Integer>() {
      @Override public void onCompleted() {
        Log.d(getClass().getSimpleName(), "startConsumerService()::observer::onCompleted()");
        mView.append("onCompleted()");
      }

      @Override public void onError(Throwable e) {

      }

      @Override public void onNext(Integer integer) {
        Log.d(getClass().getSimpleName(), "startConsumerService()::observer::onNext() -> " + integer);
        mView.append("onNext(): " + String.valueOf(integer));
      }
    };
  }
}
