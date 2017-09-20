package server;

import model.*;
import utilities.Util;

import java.net.Socket;

import static model.Request.*;

class ServerWorkerThread extends Thread {

    private Socket socket;
    private HttpdConf httpdConf;
    private MimeTypes mimeTypeConf;

    public ServerWorkerThread(Socket socket, HttpdConf httpdConf, MimeTypes mimeTypeConf) {
        this.socket=socket;
        this.httpdConf = httpdConf;
        this.mimeTypeConf = mimeTypeConf;
    }

    @Override
    public void run() {
        try {
            Request request = new Request(socket);
            Util.print(request.toString());
            Resource resource = new Resource(request.getUri(), httpdConf, mimeTypeConf);
            Response response = ResponseFactory.getResponse(request, resource);
            response.sendResponse(socket);
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}