package nsu.ccfit.ru.mikhalev.network.model.udp;

import lombok.extern.slf4j.Slf4j;
import nsu.ccfit.ru.mikhalev.game.controller.GameController;
import nsu.ccfit.ru.mikhalev.network.model.message.*;

import java.io.IOException;
import java.net.*;

import java.util.concurrent.*;

import static nsu.ccfit.ru.mikhalev.context.ContextValue.SIZE_BUFFER;

@Slf4j
public class ReceiverUDP {

    private final DatagramSocket datagramSocket;

    private final ExecutorService executorService = Executors.newFixedThreadPool(10);

    private final byte[] buffer = new byte[SIZE_BUFFER];
    private final DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

    private final GameController gameController;

    private final NetworkStorage networkStorage;

    public ReceiverUDP(DatagramSocket datagramSocket, NetworkStorage networkStorage, GameController gameController) {
        this.datagramSocket = datagramSocket;
        this.gameController = gameController;
        this.networkStorage = networkStorage;
    }

    public void receive() {
        try {
            datagramSocket.receive(packet);
            executorService.submit(new MessageHandler(packet, networkStorage, gameController));
        } catch (IOException ignored) {}
    }
}