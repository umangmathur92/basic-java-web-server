package server;

import model.Request;
import utilities.Util;

import java.io.PrintWriter;
import java.net.Socket;

class ServerWorkerThread extends Thread {

    private Socket socket;

    public ServerWorkerThread(Socket socket) {
        this.socket=socket;
    }

    @Override
    public void run() {
        try {
            Request request = new Request(socket);
            Util.print(request.toString());
            PrintWriter outputPrintWriter = new PrintWriter(socket.getOutputStream(), true);//OutputStream
            outputPrintWriter.println("HTTP/1.1 200 Success\r\n" + "Content-type: text/html\r\n\r\n" + "<html><head></head><body>This is the HTML body</body></html>\n");
            outputPrintWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}