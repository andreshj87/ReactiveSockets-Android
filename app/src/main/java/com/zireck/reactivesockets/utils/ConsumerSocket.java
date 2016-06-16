package com.zireck.reactivesockets.utils;

import android.util.Log;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Created by andres.hernandez on 16/06/16.
 */
public class ConsumerSocket {

  private static final int SOCKET_PORT = 9999;

  private Socket mSocket;
  BufferedReader mBufferedReader;
  private boolean mConsuming;

  public void startConsuming() {
    mConsuming = true;

    try {
      connect();
      consume();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void stopConsuming() {
    mConsuming = false;

    try {
      mBufferedReader.close();
      mSocket.close();
    } catch (IOException e) {
      e.printStackTrace();
    }

    Log.d(getClass().getSimpleName(), "Manually stopped consuming");
  }

  private void connect() throws IOException {
    mSocket = new Socket("localhost", SOCKET_PORT);
  }

  private void consume() throws IOException {
    mBufferedReader = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
    String message = new String();
    while (!mSocket.isClosed() && message != null && mConsuming) {
      message = mBufferedReader.readLine();
      Log.d(getClass().getSimpleName(), "C> Reading: " + message);
    }
    Log.d(getClass().getSimpleName(), "C> Stopped consuming");
  }
}
