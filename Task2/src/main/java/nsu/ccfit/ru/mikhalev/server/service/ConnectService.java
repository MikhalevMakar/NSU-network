package nsu.ccfit.ru.mikhalev.server.service;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class ConnectService implements AutoCloseable {

    private final ServerSocket serverSocket;

    public ConnectService(int port) throws IOException {
        log.info("create new ConnectService by port " + port);
        this.serverSocket = new ServerSocket(port);
    }

    public void execute() {
        log.info("connect service execute()");
        try(ExecutorService executorService = Executors.newCachedThreadPool()) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                executorService.submit(new ReceiverService(clientSocket));
            }
        } catch (Exception ex) {
            log.warn("exception in method accept" + ex.getMessage());
        }
    }

    @Override
    public void close() throws Exception {
        log.info("close resources");
        this.serverSocket.close();
    }
}