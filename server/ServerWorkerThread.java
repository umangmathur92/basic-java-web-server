package server;

import model.HttpdConf;
import model.MimeTypes;
import model.Resource;
import model.Request;
import model.responses.Response;
import model.responses.ResponseFactory;
import model.LogFile;
import utilities.Util;

import java.net.Socket;

public class ServerWorkerThread extends Thread {

    private Socket socket;
    private HttpdConf httpdConf;
    private MimeTypes mimeTypeConf;

    public ServerWorkerThread(Socket socket, HttpdConf httpdConf, MimeTypes mimeTypeConf) {
        this.socket = socket;
        this.httpdConf = httpdConf;
        this.mimeTypeConf = mimeTypeConf;
    }

    @Override
    public void run() {
        try {
            if (socket != null) {
                Request request = new Request(socket);
                Util.print(request.toString());
                Resource resource = new Resource(request.getUri(), httpdConf, mimeTypeConf);
                Response response = ResponseFactory.getResponse(request, resource);
                response.sendResponse(socket, resource);
                LogFile logFile = new LogFile(httpdConf.getLogFile());
                logFile.write(request, response, socket);
                socket.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}