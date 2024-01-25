package dai.auditor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

import static java.nio.charset.StandardCharsets.UTF_8;

public class AuditorSender {
    final static String IPADDRESS = "localhost";
    final static int PORT = 2205;

    AuditorListener udpListener;

    public AuditorSender(AuditorListener listener) {
        udpListener = listener;
    }

    public void startConnection() {
    try (ServerSocket serverSocket = new ServerSocket(1234)) {
        ObjectMapper om = new ObjectMapper();
        while (true) {
            try (Socket socket = serverSocket.accept();
                 var in = new BufferedReader(
                         new InputStreamReader(
                                 socket.getInputStream(), UTF_8));
                 var out = new BufferedWriter(
                         new OutputStreamWriter(
                                 socket.getOutputStream(), UTF_8))){
                String line;
                while ((line = in.readLine()) != null) {}

                ArrayList<MusicianClientData> activeMusicians = udpListener.getActiveMusicians();
                String json = om.writeValueAsString(activeMusicians);
                out.write(json);
                out.flush();
            } catch (IOException e) {
                System.out.println("Server: socket ex.: " + e);
            }
        }
    } catch (IOException e) {
        System.out.println("Server: server socket ex.: " + e);
    }
}
}
