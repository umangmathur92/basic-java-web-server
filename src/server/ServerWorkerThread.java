package server;

import utilities.Util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
            BufferedReader bufferedInputReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));//InputStream
            PrintWriter outputPrintWriter = new PrintWriter(socket.getOutputStream(), true);//OutputStream
            String strAllHeaders = "";
            boolean keepLooping = true;
            while (keepLooping) {
                String str = bufferedInputReader.readLine();
                if (str == null || str.equals("")) {
                    keepLooping = false;
                } else {
                    strAllHeaders = strAllHeaders + (strAllHeaders.length() == 0 ? str : "\n"+ str);
                }
            }
            Util.print(strAllHeaders);// Log the request
            outputPrintWriter.println("HTTP/1.1 200 Success\r\n" + "Content-type: text/html\r\n\r\n" + "<html><head></head><body>This is the HTML body</body></html>\n");
            outputPrintWriter.close();
        }
        catch (IOException e) {
            System.out.println(e);
        }
    }

}