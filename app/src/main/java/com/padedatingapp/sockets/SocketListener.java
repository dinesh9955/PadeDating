package com.padedatingapp.sockets;

/**
 * Created by Mahabali on 11/16/15.
 */
public interface SocketListener {
    void onSocketConnected();
    void onSocketDisconnected();
    void onSocketConnectionError();
    void onSocketConnectionTimeOut();
}
