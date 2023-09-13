package nsu.ccfit.ru.mikhalev;

import nsu.ccfit.ru.mikhalev.service.MulticastService;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        MulticastService multicastService = new MulticastService(args[1], Integer.parseInt(args[2]));
        multicastService.execute();
    }
}