package com.zireck.reactivesockets.utils;

import android.util.Log;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Created by andres.hernandez on 16/06/16.
 */
public class ConsumerSocket {

  private static final int SOCKET_PORT = 9999;

  private Socket mSocket;

  public void startConsuming() {
    try {
      connect();
      consume();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void connect() throws IOException {
    mSocket = new Socket();
    mSocket.setReuseAddress(true);
    mSocket.bind(new InetSocketAddress("localhost", SOCKET_PORT));
  }

  private void consume() throws IOException {
    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
    String message;
    while (mSocket.isConnected()) {
      message = bufferedReader.readLine();
      Log.d(getClass().getSimpleName(), "C> Reading: " + message);
    }
  }
}
