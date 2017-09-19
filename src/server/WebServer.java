package server;

import utilities.Util;

import java.net.ServerSocket;
import java.net.Socket;

public class WebServer {

    public static final int PORT_NUMBER = 7000;

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT_NUMBER);
            while (true) {
                Socket socket = serverSocket.accept();
                ServerWorkerThread serverWorkerThread = new ServerWorkerThread(socket);
                serverWorkerThread.start();//This causes the 'run()' method to be executed

                Thread t = new Thread();
                t.start();
            }
        }
        catch (Exception e) {
            Util.print("Exception: " + e.getMessage());
        }
    }
}



