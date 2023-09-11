package nsu.ccfit.ru.mikhalev.service;

import lombok.extern.slf4j.Slf4j;
import nsu.ccfit.ru.mikhalev.model.MulticastUDP;

import java.io.IOException;
import java.net.DatagramPacket;

import static java.lang.Thread.sleep;
import static nsu.ccfit.ru.mikhalev.context.ContextValue.*;

@Slf4j
public class MulticastService {

    private final MulticastUDP multicastUDP;

    public MulticastService(String ip, int port) throws IOException {
        multicastUDP = new MulticastUDP(ip, port);
        multicastUDP.addToGroup();
    }

    public void executeSender() throws InterruptedException {
        int countSend = 0;

        while(countSend++ < NUMBER_MESSAGE_SENT) {
            multicastUDP.send(MESSAGE);
            log.info("send message " + MESSAGE);
            sleep(SECOND);
        }
        multicastUDP.leaveGroup();
    }

    public void executeReceiver() {
        while(true) {
            DatagramPacket packet = multicastUDP.receive();
            log.info("receive message by user " + packet.getAddress());
            multicastUDP.checkAlive();
        }
    }
}