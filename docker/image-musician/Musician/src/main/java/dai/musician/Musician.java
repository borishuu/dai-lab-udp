package dai.musician;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.*;

import static java.nio.charset.StandardCharsets.*;

class Musician {
    private final static String IPADDRESS = "239.255.22.5";
    private final static int PORT = 9904;
    private static final HashMap<String, String> INSTRUMENTS = new HashMap<>(Map.of(
            "piano", "ti-ta-ti",
            "trumpet", "pouet",
            "flute", "trulu",
            "violin", "gzi-gzi",
            "drum", "boum-boum"
    ));

    private final UUID uuid;
    private final String sound;

    public Musician(String instrument) throws IOException {
        this.uuid = UUID.randomUUID();
        this.sound = INSTRUMENTS.get(instrument);
    }

    public void run() {
        Gson gson = new Gson();
        String message = gson.toJson(this);
        byte[] payload = message.getBytes(UTF_8);
        DatagramPacket packet = new DatagramPacket(payload,
                payload.length, new InetSocketAddress(IPADDRESS, PORT));

        try (DatagramSocket socket = new DatagramSocket()) {
            socket.send(packet);
            System.out.println("Sent packet: " + message);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) throws IOException {
        if (args.length != 1)
            throw new IllegalArgumentException("Not enough or too many arguments.");

        String instrument = args[0];

        final int rythm = 1000;
        long lastTime = System.currentTimeMillis();
        long nextPlay = 0;

        Musician musician = new Musician(instrument);

        while (true) {
            long currentTime = System.currentTimeMillis();
            long delta = currentTime - lastTime;
            lastTime = currentTime;
            nextPlay -= delta;

            if (nextPlay <= 0) {
                nextPlay += rythm;
                musician.run();
            }
        }
    }
}
