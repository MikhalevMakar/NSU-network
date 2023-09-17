package nsu.ccfit.ru.mikhalev;

import nsu.ccfit.ru.mikhalev.ecxeption.InvalidMulticastIPException;
import nsu.ccfit.ru.mikhalev.service.MulticastService;

public class Main {
    public static void main(String[] args) {
        try (MulticastService multicastService = new MulticastService(args[0], Integer.parseInt(args[1]))) {
            multicastService.run();
        } catch (Exception e) {
           throw new InvalidMulticastIPException(args[0]);
        }
    }
}