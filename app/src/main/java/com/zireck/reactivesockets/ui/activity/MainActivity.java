package com.zireck.reactivesockets.ui.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.zireck.reactivesockets.R;
import com.zireck.reactivesockets.ui.presenter.MainPresenter;
import com.zireck.reactivesockets.ui.view.MainView;
import com.zireck.reactivesockets.utils.ConsumerService;
import com.zireck.reactivesockets.utils.ProducerService;
import rx.Subscriber;

public class MainActivity extends AppCompatActivity implements MainView {

  private MainPresenter mPresenter;
  private ConsumerService mConsumerService;
  private boolean mConsumerServiceBound = false;

  @BindView(R.id.button_producer) Button mButtonProducer;
  @BindView(R.id.button_consumer) Button mButtonConsumer;
  @BindView(R.id.consumer_status) TextView mConsumerStatus;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);

    initialize();
  }

  @Override protected void onStop() {
    super.onStop();

    if (mConsumerServiceBound) {
      unbindService(mConsumerServiceConnection);
      mConsumerServiceBound = false;
    }
  }

  private void initialize() {
    mPresenter = new MainPresenter(this);
  }

  @OnClick(R.id.button_producer) void onClickProducer() {
    mPresenter.onClickProducer();
  }

  @OnClick(R.id.button_consumer) void onClickConsumer() {
    mPresenter.onClickConsumer();
  }

  @Override public void startProducerService() {
    startService(new Intent(this, ProducerService.class));
  }

  @Override public void stopProducerService() {
    stopService(new Intent(this, ProducerService.class));
  }

  @Override public void notifyProducerStarted() {
    mButtonProducer.setText(getString(R.string.stop_producer));
  }

  @Override public void notifyProducerStopped() {
    mButtonProducer.setText(getString(R.string.start_producer));
  }

  @Override public void startConsumerService() {
    Intent intent = new Intent(this, ConsumerService.class);
    bindService(intent, mConsumerServiceConnection, Context.BIND_AUTO_CREATE);
  }

  @Override public void stopConsumerService() {
    if (mConsumerServiceBound) {
      unbindService(mConsumerServiceConnection);
      mConsumerServiceBound = false;
    }
  }

  @Override public void notifyConsumerStarted() {
    mButtonConsumer.setText(getString(R.string.stop_consumer));
  }

  @Override public void notifyConsumerStopped() {
    mButtonConsumer.setText(getString(R.string.start_consumer));
  }

  @Override public void setSubscriber(Subscriber subscriber) {
    mConsumerService.setSubscriber(subscriber);
  }

  @Override
  public void append(String text) {
    String currentText = mConsumerStatus.getText().toString();
    mConsumerStatus.setText(String.format("%s\n%s", text, currentText));
  }

  private ServiceConnection mConsumerServiceConnection = new ServiceConnection() {
    @Override public void onServiceConnected(ComponentName name, IBinder service) {
      ConsumerService.ConsumerServiceBinder binder =
          ((ConsumerService.ConsumerServiceBinder) service);
      mConsumerService = binder.getService();
      mConsumerServiceBound = true;

      mPresenter.onServiceConnected();
    }

    @Override public void onServiceDisconnected(ComponentName name) {
      mConsumerServiceBound = false;
    }
  };
}
