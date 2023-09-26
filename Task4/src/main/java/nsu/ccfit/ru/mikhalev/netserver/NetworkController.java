package nsu.ccfit.ru.mikhalev.netserver;

import nsu.ccfit.ru.mikhalev.netserver.model.MulticastSend;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class NetworkController implements  Runnable {

    public static int MAX_SIZE_BUFFER = 1024;
    private MulticastSend multicastReceiver;

    private final DatagramSocket datagramSocket;

    NetworkController() throws SocketException{
        this.datagramSocket = new DatagramSocket();
    }

        @Override
    public void run(){
        byte[] buffer = new byte[MAX_SIZE_BUFFER];
        DatagramPacket packet = new DatagramPacket (buffer, buffer.length);
        try {
            while (!Thread.currentThread().isInterrupted ()) {
                datagramSocket.receive (packet);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
