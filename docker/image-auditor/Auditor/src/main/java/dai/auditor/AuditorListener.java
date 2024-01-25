package dai.auditor;

import java.net.DatagramPacket;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.HashMap;
import java.time.LocalDateTime;
import java.util.Iterator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import static java.nio.charset.StandardCharsets.UTF_8;

public class AuditorListener {
    final static String IPADDRESS = "239.255.22.5";
    final static int PORT = 9904;
    private MulticastSocket socket;
    InetSocketAddress groupAddress;
    NetworkInterface netif;
    ArrayList<MusicianClientData> musicians = new ArrayList<>();
    HashMap<String, String> soundInstruments = new HashMap<>();

    public AuditorListener() {
        // TODO init sound -> insturment map

        soundInstruments.put("ti-ta-ti", "piano");
        soundInstruments.put("pouet", "trumpet");
        soundInstruments.put("trulu", "flute");
        soundInstruments.put("gzi-gzi", "violin");
        soundInstruments.put("boum-boum", "drum");
    }

    public void listen() {
        ObjectMapper mapper = new ObjectMapper();
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

                Gson gson = new Gson();
                MusicianData receivedMusician = gson.fromJson(message, MusicianData.class);
                LocalDateTime receiveTime = LocalDateTime.now();

                boolean found = false;
                for (int i = 0; i < musicians.size(); ++i) {
                    if (musicians.get(i).uuid.equals(receivedMusician.uuid)) {
                        musicians.get(i).lastActivity = receiveTime;
                        found = true;
                        break;
                    }
                }

                if (!found)
                    musicians.add(new MusicianClientData(
                            receivedMusician.uuid, soundInstruments.get(receivedMusician.sound), receiveTime));

                for (MusicianClientData musician : musicians) {
                    System.out.println(musician.uuid + " " + musician.instrument + " " + musician.lastActivity);
                }
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

    public ArrayList<MusicianClientData> getActiveMusicians() {
        ArrayList<MusicianClientData> activeMusicians = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();

        for (Iterator<MusicianClientData> iterator = musicians.iterator(); iterator.hasNext(); ) {
            MusicianClientData musician = iterator.next();
            if (musician.lastActivity.plusSeconds(5).isAfter(now))
                activeMusicians.add(musician);
            else
                iterator.remove();
        }

        return activeMusicians;
    }
}
