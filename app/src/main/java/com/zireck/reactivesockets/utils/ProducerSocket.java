package com.zireck.reactivesockets.utils;

import android.util.Log;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

/**
 * Created by andres.hernandez on 13/06/16.
 */
public class ProducerSocket {

  private static final int SOCKET_PORT = 9999;

  private ServerSocket mServerSocket;
  private PrintStream mPrintStream;
  private boolean mListening;

  public void listen() {
    try {
      mServerSocket = new ServerSocket(SOCKET_PORT);
    } catch (IOException e) {
      e.printStackTrace();
    }

    mListening = true;

    Socket clientSocket;
    while (mListening) {
      try {
        clientSocket = listenForNewClient();
        processClient(clientSocket);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  public void stopListening() {
    mListening = false;

    try {
      if (mPrintStream != null) {
        mPrintStream.close();
      }

      if (mServerSocket != null) {
        mServerSocket.close();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    Log.d(getClass().getSimpleName(), "P> Stopped listening");
  }

  private Socket listenForNewClient() throws IOException {
    Socket clientSocket = null;

    Log.d(getClass().getSimpleName(), "P> Waiting for new client.");
    clientSocket = acceptNewClient();
    Log.d(getClass().getSimpleName(),
        "P> New client connected: " + clientSocket.getRemoteSocketAddress().toString());

    return clientSocket;
  }

  private Socket acceptNewClient() throws IOException {
    Socket clientSocket = mServerSocket.accept();
    return clientSocket;
  }

  private void processClient(Socket client) {
    if (client == null) {
      return;
    }

    mPrintStream = getPrintStreamForSocket(client);

    while (client.isConnected() && mListening) {
      sendMessage(mPrintStream, String.valueOf(generateRandomNumber()));
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
    printStream.println(message);
  }

  private int generateRandomNumber() {
    int maximum = 100;
    int minimum = 0;
    int randomNumber = -1;

    Random random = new Random();
    randomNumber = random.nextInt(maximum - minimum + 1) + minimum;

    return randomNumber;
  }

  private void pause() {
    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

}
