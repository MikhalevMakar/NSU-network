package nsu.ccfit.ru.mikhalev.network;


import lombok.extern.slf4j.Slf4j;

import nsu.ccfit.ru.mikhalev.ecxeption.ThreadInterException;
import nsu.ccfit.ru.mikhalev.game.controller.GameController;
import nsu.ccfit.ru.mikhalev.network.model.message.*;
import nsu.ccfit.ru.mikhalev.network.model.udp.*;

import java.net.*;
import java.util.*;

@Slf4j
public class ServiceUDP {
    private static final int timeoutDelay = 200;

    private static final int SEND_DELAY = 1;

    private static final int RECEIVE_DELAY = 2;

    private final DatagramSocket datagramSocket = new DatagramSocket();

    private final ReceiverUDP receiverUDP;

    private final SenderUDP senderUDP = new SenderUDP(datagramSocket);

    private final NetworkStorage networkStorage;

    public ServiceUDP(NetworkStorage networkStorage, GameController gameController) throws SocketException {
        receiverUDP = new ReceiverUDP(datagramSocket, gameController, networkStorage);

        this.networkStorage = networkStorage;
        datagramSocket.setSoTimeout(timeoutDelay);
    }

    public void startReceiver() {
        Thread thread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                long currentTime = System.currentTimeMillis();
                synchronized (datagramSocket) {
                    while (System.currentTimeMillis () - currentTime < timeoutDelay / 10) {
                        receiverUDP.receive();
                    }
                }
              try {
                  Thread.sleep(RECEIVE_DELAY);
              } catch (InterruptedException e) {
                    throw new ThreadInterException(e.getMessage());
              }
            }
        });
        thread.start();
    }

    public void startSender() {
        Thread thread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                synchronized (datagramSocket) {
                    for(var message : networkStorage.getMessagesToSend()) {
                        senderUDP.send(message);
                        if(MessageType.isNeedConfirmation(message.getGameMessage().getTypeCase().getNumber()))
                            networkStorage.getSentMessages().put(message.getGameMessage().getMsgSeq(),
                                                                 new NodeInfo(message.getHostNetworkKey(), message, new Date()));

                        networkStorage.getMessagesToSend().remove(message);
                    }
                }
                try {
                    Thread.sleep(SEND_DELAY);
                } catch (InterruptedException e) {
                    throw new ThreadInterException(e.getMessage());
                }
            }
        });
        thread.start();
    }

    public void startCheckerMsgACK() {
        Runnable r = ()-> {
            Map<Long, NodeInfo> sentMessages = networkStorage.getSentMessages();
            while(!Thread.currentThread().isInterrupted()) {
                for(var sentMessage : sentMessages.entrySet()) {
                     if(System.currentTimeMillis() - sentMessage.getValue().date().getTime() > timeoutDelay / 10) {
                         networkStorage.getMessagesToSend().add(sentMessage.getValue().message());
                         sentMessages.remove(sentMessage.getValue().message().getGameMessage().getMsgSeq());
                     }
                }
                try {
                    Thread.sleep(timeoutDelay / 10);
                } catch (InterruptedException e) {
                    throw new ThreadInterException(e.getMessage());
                }
            }
        };
        new Thread(r).start();
    }
}

