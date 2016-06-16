package com.zireck.reactivesockets.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.zireck.reactivesockets.R;
import com.zireck.reactivesockets.ui.presenter.MainPresenter;
import com.zireck.reactivesockets.ui.view.MainView;
import com.zireck.reactivesockets.utils.ConsumerService;
import com.zireck.reactivesockets.utils.ProducerService;

public class MainActivity extends AppCompatActivity implements MainView {

  private MainPresenter mPresenter;

  @BindView(R.id.button_producer) Button mButtonProducer;
  @BindView(R.id.button_consumer) Button mButtonConsumer;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);

    initialize();
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
    startService(new Intent(this, ConsumerService.class));
  }

  @Override public void stopConsumerService() {
    stopService(new Intent(this, ConsumerService.class));
  }

  @Override public void notifyConsumerStarted() {
    mButtonConsumer.setText(getString(R.string.stop_consumer));
  }

  @Override public void notifyConsumerStopped() {
    mButtonConsumer.setText(getString(R.string.start_consumer));
  }
}
