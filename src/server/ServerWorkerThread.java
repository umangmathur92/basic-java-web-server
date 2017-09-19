package server;

import model.Request;
import utilities.Util;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedHashMap;

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
            String firstLine = bufferedInputReader.readLine();
            String[] variables = firstLine.split(" ");
            String verb = variables[0];
            String uri = variables[1];
            String httpVersion = variables[2];
            HashMap<String, String> headersMap = new LinkedHashMap<>();
            String body = "";
            String str = bufferedInputReader.readLine();
            while (str.length()>0) {
                String[] keyValStrArr = str.split(":");
                String key = keyValStrArr[0].trim();
                String value = keyValStrArr[1].trim();
                headersMap.put(key, value);
                str = bufferedInputReader.readLine();
            }
            String contentLength = null;
            if((contentLength = headersMap.get("Content-Length"))!=null) {
                StringBuilder bodyBuilder = new StringBuilder();
                for(int i=0;i<Integer.parseInt(contentLength);i++){
                    bodyBuilder.append((char)bufferedInputReader.read());
                }
                body = bodyBuilder.toString();
            }
            Request request = new Request(uri, verb, httpVersion, headersMap, body.getBytes("UTF-8"));
            Util.print(request.toString());
            outputPrintWriter.println("HTTP/1.1 200 Success\r\n" + "Content-type: text/html\r\n\r\n" + "<html><head></head><body>This is the HTML body</body></html>\n");
            outputPrintWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}