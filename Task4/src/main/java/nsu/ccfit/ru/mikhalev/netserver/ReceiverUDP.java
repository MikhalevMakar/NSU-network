package nsu.ccfit.ru.mikhalev.netserver;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nsu.ccfit.ru.mikhalev.ecxeption.ReceiverException;

import java.io.IOException;
import java.net.*;

import static nsu.ccfit.ru.mikhalev.context.ContextValue.SIZE_BUFFER;

@Slf4j
@AllArgsConstructor
public class ReceiverUDP implements Runnable {

    private final DatagramSocket datagramSocket;

    @Override
    public void run() {
        byte[] buffer = new byte[SIZE_BUFFER];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

        try {
            while(!Thread.currentThread().isInterrupted()) {
                this.datagramSocket.receive(packet);
                log.info("receive message by ip" + packet.getAddress());
            }
        } catch (IOException e) {
                throw new ReceiverException(e.getMessage());
        }

    }
}
