package nsu.ccfit.ru.mikhalev.network;


import lombok.extern.slf4j.Slf4j;

import nsu.ccfit.ru.mikhalev.ecxeption.ThreadInterException;
import nsu.ccfit.ru.mikhalev.game.controller.GameController;
import nsu.ccfit.ru.mikhalev.network.model.message.NetworkStorage;
import nsu.ccfit.ru.mikhalev.network.model.udp.*;

import java.net.*;
import java.util.Timer;
import java.util.TimerTask;

@Slf4j
public class ServiceUDP {
    public static final int TIMEOUT_DELAY = 5;
    public static final int TIMEOUT_SOCKET = 50;


    public static final String THREAD_SENDER = "Thread sender";

    private final DatagramSocket datagramSocket = new DatagramSocket();

    private final ReceiverUDP receiverUDP;

    private final SenderUDP senderUDP = new SenderUDP(datagramSocket);

    private final NetworkStorage networkStorage;

    public ServiceUDP(NetworkStorage networkStorage, GameController gameController) throws SocketException {
        receiverUDP = new ReceiverUDP(datagramSocket, gameController);
        this.networkStorage = networkStorage;
        datagramSocket.setSoTimeout(TIMEOUT_DELAY);
    }

   // public void startReceiver() {
//        Runnable r = () -> {
//            while (!Thread.currentThread().isInterrupted()) {
//                long currentTime = System.currentTimeMillis();
//                synchronized (datagramSocket) {
//                    while (System.currentTimeMillis() - currentTime < TIMEOUT_DELAY)
//                        receiverUDP.receive();
//                }
//            }
//        };
//        new Thread(r, THREAD_SENDER).start();
//    }
//
//    public void startSender(){
//        Runnable r = () -> {
//            try {
//                while (!Thread.currentThread().isInterrupted()) {
//                    synchronized (datagramSocket) {
//                        for (var message : networkStorage.getMessagesToSend()) {
//                            senderUDP.send(message);
//                            networkStorage.getMessagesToSend().remove(message);
//                        }
//                    }
//                    Thread.sleep(TIMEOUT_DELAY);
//                }
//            } catch (InterruptedException e) {
//                throw new ThreadInterException(THREAD_SENDER);
//            }
//        };
//        new Thread (r, THREAD_SENDER).start();
//    }

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
            Timer timer = new Timer ();
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

