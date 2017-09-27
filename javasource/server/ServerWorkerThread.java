package javasource.server;

import javasource.configuration.HttpdConf;
import javasource.configuration.MimeTypes;
import javasource.logging.LogFile;
import javasource.request.Request;
import javasource.resources.Resource;
import javasource.responses.Response;
import javasource.responses.ResponseFactory;
import javasource.utilities.Util;

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