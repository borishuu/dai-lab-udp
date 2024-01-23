package dai.auditor;

import java.net.*;
import java.util.ArrayList;

import static java.nio.charset.StandardCharsets.UTF_8;

public class Auditor {
    final static String IPADDRESS = "239.255.22.5";
    final static int PORT = 9904;
    private MulticastSocket socket;
    InetSocketAddress groupAddress;
    NetworkInterface netif;
    ArrayList<MusicianData> musicians = new ArrayList<>();

    public Auditor() {
        // TODO init sound -> insturment map
    }

    public void listen() {
        try {
            socket = new MulticastSocket(PORT);
            groupAddress = new InetSocketAddress(IPADDRESS, PORT);
            netif = NetworkInterface.getByName("eth0");
            socket.joinGroup(groupAddress, netif);

            while (true) {
                byte[] buffer = new byte[1024];
                var packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);
                String message = new String(packet.getData(), 0,
                        packet.getLength(), UTF_8);


            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void stopListening() {
        try {
            socket.leaveGroup(groupAddress, netif);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
