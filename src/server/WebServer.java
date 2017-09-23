package server;

import model.HttpdConf;
import model.MimeTypes;
import utilities.Util;

import java.net.ServerSocket;
import java.net.Socket;

public class WebServer {

    public static void main(String[] args) {
        try {
            HttpdConf httpdConf = new HttpdConf("config/httpd.conf");
            MimeTypes mimeTypeConf = new MimeTypes("config/mime.types");
            ServerSocket serverSocket = new ServerSocket(httpdConf.getListenPort());
            while (true) {
                Socket socket = serverSocket.accept();
                ServerWorkerThread serverWorkerThread = new ServerWorkerThread(socket, httpdConf, mimeTypeConf);
                serverWorkerThread.start();//This causes the 'run()' method to be executed
            }
        } catch (Exception e) {
            Util.print("Exception: " + e.getMessage());
        }
    }
}