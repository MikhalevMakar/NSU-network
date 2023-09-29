package nsu.ccfit.ru.mikhalev.player;

import nsu.ccfit.ru.mikhalev.model.MulticastReceiver;

import java.io.IOException;

public class Player {

    private final MulticastReceiver multicastReceiver;


    public Player(String ip, Integer port) throws IOException {
        this.multicastReceiver = new MulticastReceiver(ip, port);
    }
}
