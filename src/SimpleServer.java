import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class SimpleServer {

    public static final int PORT_NUMBER = 8080;

    public static void main(String args[]) throws IOException {
        ServerSocket server = new ServerSocket(PORT_NUMBER);
        System.out.println("Listening for connection on port 8080 ....");
        while (true) {
            try (Socket socket = server.accept()) {
                Date today = new Date();
                String httpResponse = "HTTP/1.1 200 OK\r\n\r\n" + today;
                socket.getOutputStream().write(httpResponse.getBytes("UTF-8"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    /*public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT_NUMBER);
            Socket clientSocket = serverSocket.accept();
            PrintWriter outputPrintWriter = new PrintWriter(clientSocket.getOutputStream(), true);//OutputStream
            BufferedReader bufferedInputReader = new BufferedReader(new server.ZInputStreamReader(clientSocket.getInputStream()));//InputStream
            utilities.Util.print("Client connected on port: " + PORT_NUMBER);
            String inputLine;
            while ((inputLine = bufferedInputReader.readLine()) != null) {
                utilities.Util.print("Received message: " + inputLine + " from " + clientSocket.toString());

            }*//*outputPrintWriter.println(inputLine);*//*
            outputPrintWriter.write("hello");
            bufferedInputReader.close();
            outputPrintWriter.close();
            clientSocket.close();
            serverSocket.close();

        } catch (Exception e) {
            utilities.Util.print("Exception caught when trying to listen on port " + PORT_NUMBER + " or listening for a connection: " + e.getMessage());
            //e.printStackTrace();
        }
    }*/

}
