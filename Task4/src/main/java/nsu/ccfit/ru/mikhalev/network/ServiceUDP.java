package nsu.ccfit.ru.mikhalev.network;


import lombok.extern.slf4j.Slf4j;

import nsu.ccfit.ru.mikhalev.game.controller.GameController;
import nsu.ccfit.ru.mikhalev.network.model.message.NetworkStorage;
import nsu.ccfit.ru.mikhalev.network.model.udp.*;

import java.net.*;
import java.util.*;

@Slf4j
public class ServiceUDP {
    public static final int TIMEOUT_SOCKET = 50;

    private final DatagramSocket datagramSocket = new DatagramSocket();

    private final ReceiverUDP receiverUDP;

    private final SenderUDP senderUDP = new SenderUDP(datagramSocket);

    private final NetworkStorage networkStorage;

    public ServiceUDP(NetworkStorage networkStorage, GameController gameController) throws SocketException {
        receiverUDP = new ReceiverUDP(datagramSocket, networkStorage, gameController);
        this.networkStorage = networkStorage;
        datagramSocket.setSoTimeout(TIMEOUT_SOCKET);
    }

    public void startReceiver() {
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                long currentTime = System.currentTimeMillis();
                synchronized (datagramSocket) {
                    while (System.currentTimeMillis() - currentTime < TIMEOUT_SOCKET) {
                        receiverUDP.receive();
                    }
                }
            }
        };
        timer.scheduleAtFixedRate(timerTask, 0, TIMEOUT_SOCKET);
    }

    public void startSender() {
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                synchronized (datagramSocket) {
                    for(var message : networkStorage.getMessagesToSend()) {
                        log.info("SEND MESSAGE");
                        senderUDP.send(message);
                        networkStorage.getMessagesToSend().remove(message);
                    }
                }
            }
        };
        timer.scheduleAtFixedRate(timerTask, 0, TIMEOUT_SOCKET);
    }
}
