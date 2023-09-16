package nsu.ccfit.ru.mikhalev.server.service;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

@Slf4j
public class ConnectService implements AutoCloseable {
    private final ServerSocket serverSocket;

    public ConnectService(int port) throws IOException {
        log.info("create new ConnectService by port " + port);
        this.serverSocket = new ServerSocket(port);
    }
    public void execute() throws IOException {
        log.info("connect service execute() ");
        try {
            Socket clientSocket = serverSocket.accept();
            Thread thread = new Thread(new ReceiverService(clientSocket));
            thread.start();
        } catch(IOException ex) {
            throw new IOException(ex);
        }
    }

    @Override
    public void close() throws Exception {
        log.info("close resources");
        this.serverSocket.close();
    }
}