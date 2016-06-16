package com.zireck.reactivesockets.utils;

import android.util.Log;
import java.io.IOException;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

/**
 * Created by andres.hernandez on 13/06/16.
 */
public class ProducerSocket {

  private static final int SOCKET_PORT = 9999;

  private ServerSocket mServerSocket;

  public ProducerSocket() {
    try {
      mServerSocket = new ServerSocket();
      mServerSocket.setReuseAddress(true);
      mServerSocket.bind(new InetSocketAddress(SOCKET_PORT));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void startProducing() {
    Socket clientSocket;
    do {
      Log.d(getClass().getSimpleName(), "P> Waiting for new client.");
      clientSocket = acceptNewClient();

      if (clientSocket != null && clientSocket.getRemoteSocketAddress() != null) {
        Log.d(getClass().getSimpleName(),
            "P> New client connected: " + clientSocket.getRemoteSocketAddress().toString());
      }

      processClient(clientSocket);
    } while (clientSocket != null && clientSocket.isConnected());
  }

  private Socket acceptNewClient() {
    Socket clientSocket;
    try {
      clientSocket = mServerSocket.accept();
      return clientSocket;
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }

  private void processClient(Socket client) {
    if (client == null) {
      return;
    }

    PrintStream printStream = getPrintStreamForSocket(client);

    while (client.isConnected()) {
      sendMessage(printStream, String.valueOf(generateRandomNumber()));
      pause();
    }
  }

  private PrintStream getPrintStreamForSocket(Socket socket) {
    PrintStream printStream = null;
    try {
      printStream = new PrintStream(socket.getOutputStream());
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      return printStream;
    }
  }

  private void sendMessage(PrintStream printStream, String message) {
    Log.d(getClass().getSimpleName(), "P> Producing: " + message);
    printStream.print(message);
  }

  private int generateRandomNumber() {
    int maximum = 100;
    int minimum = 1;
    int randomNumber = -1;

    Random random = new Random();
    int n = maximum - minimum + 1;
    int i = random.nextInt() % n;
    randomNumber =  minimum + i;

    return randomNumber;
  }

  private void pause() {
    try {
      Thread.sleep(5000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

}
