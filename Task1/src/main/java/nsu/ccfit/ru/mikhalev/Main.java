package nsu.ccfit.ru.mikhalev;

import nsu.ccfit.ru.mikhalev.service.MulticastService;

import java.io.IOException;

import static nsu.ccfit.ru.mikhalev.context.ContextValue.EXECUTE_RECEIVER;
import static nsu.ccfit.ru.mikhalev.context.ContextValue.EXECUTE_SENDER;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        MulticastService multicastService = new MulticastService("224.0.0.2", Integer.parseInt(args[1]));
        if(args[0].equals(EXECUTE_RECEIVER)) {
            multicastService.executeReceiver();
        } else if(args[0].equals(EXECUTE_SENDER)) {
            multicastService.executeSender();
        }
    }
}