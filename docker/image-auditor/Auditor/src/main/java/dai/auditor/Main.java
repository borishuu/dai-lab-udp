package dai.auditor;

public class Main {
    public static void main(String[] args) {
        AuditorListener listener = new AuditorListener();
        AuditorSender sender = new AuditorSender(listener);

        // Start the listener in a new thread
        new Thread(listener::listen).start();

        // Start the sender
        new Thread(sender::startConnection).start();
    }
}