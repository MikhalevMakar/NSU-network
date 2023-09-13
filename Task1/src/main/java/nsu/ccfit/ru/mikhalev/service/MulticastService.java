package nsu.ccfit.ru.mikhalev.service;

import lombok.extern.slf4j.Slf4j;
import nsu.ccfit.ru.mikhalev.ecxeption.InvalidMulticastIPException;
import nsu.ccfit.ru.mikhalev.model.MulticastUDP;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;

import static java.lang.Thread.sleep;
import static nsu.ccfit.ru.mikhalev.context.ContextValue.*;

@Slf4j
public class MulticastService {

    private final MulticastUDP multicastUDP;
    public MulticastService(String ip, int port) throws IOException {
        if (!InetAddress.getByName(ip).isMulticastAddress())
            throw new InvalidMulticastIPException(ip);

        multicastUDP = new MulticastUDP(ip, port);
    }

    public void executeSender() throws InterruptedException {
        int countSend = 0;

        while(countSend++ < NUMBER_MESSAGE_SENT) {
            multicastUDP.send(MESSAGE);
            log.info("send message " + MESSAGE);
            sleep(TIMEOUT);
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

    public void execute() throws InterruptedException {
        multicastUDP.addToGroup();
        long pid = ProcessHandle.current().pid();
        DatagramPacket packet;

        int countSend = 0;

        while(countSend++ < NUMBER_MESSAGE_SENT) {
            multicastUDP.send(Long.toString(pid).trim());
            log.info("send message " + pid);

            sleep(TIMEOUT);
            packet = multicastUDP.receive();
            String dataAsString = new String(packet.getData(), StandardCharsets.UTF_8).trim();
            log.info("receive message by user " + packet.getAddress());

            Long receivePid = Long.parseLong(dataAsString);
            if(!multicastUDP.containsHost(receivePid))
                multicastUDP.addNewHost(receivePid);
        }
    }
}