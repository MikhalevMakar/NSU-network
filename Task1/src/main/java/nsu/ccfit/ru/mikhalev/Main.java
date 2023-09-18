package nsu.ccfit.ru.mikhalev;

import lombok.extern.slf4j.Slf4j;
import nsu.ccfit.ru.mikhalev.ecxeption.InvalidMulticastIPException;
import nsu.ccfit.ru.mikhalev.service.MulticastService;

import java.io.IOException;

@Slf4j
public class Main {
    public static void main(String[] args) {
        log.info("arg " + args[1]);
        try (MulticastService multicastService = new MulticastService(args[0], Integer.parseInt(args[1]))) {
            log.info("start main and multicastService.run()");
            multicastService.run();
        } catch(InterruptedException ex) {
            log.info("interrupt exception");
            Thread.currentThread().interrupt();
        } catch (IOException e) {
           throw new InvalidMulticastIPException(args[0]);
        }
    }
}