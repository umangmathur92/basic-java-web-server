import model.HttpdConf;
import model.MimeTypes;
import server.ServerWorkerThread;
import utilities.Util;

import java.io.File;
import java.net.ServerSocket;
import java.net.Socket;

public class WebServer {

    public static void main(String[] args) {
        try {
            HttpdConf httpdConf = new HttpdConf("config" + File.separator + "httpd2.conf");
            MimeTypes mimeTypeConf = new MimeTypes("config" + File.separator + "mime.types");
            ServerSocket serverSocket = new ServerSocket(httpdConf.getListenPort());
            while (true) {
                Socket socket = serverSocket.accept();
                if (socket != null) {
                    ServerWorkerThread serverWorkerThread = new ServerWorkerThread(socket, httpdConf, mimeTypeConf);
                    serverWorkerThread.start();
                }
            }
        } catch (Exception e) {
            Util.print("Exception: " + e.getMessage());
        }
    }

}