package nsu.ccfit.ru.mikhalev.service;

import lombok.extern.slf4j.Slf4j;
import nsu.ccfit.ru.mikhalev.model.ClientArgs;
import nsu.ccfit.ru.mikhalev.model.FileMetaInfo;

import java.io.*;
import java.net.*;

import static nsu.ccfit.ru.mikhalev.context.ContextValue.*;


@Slf4j
public class ClientService implements AutoCloseable {

    private final Socket client;

    private final ObjectOutputStream objectOutputStream;

    private final FileInputStream fileStream;

    private final File file;

    public ClientService(ClientArgs clientArgs) throws IOException {
        log.info("create Service client by serverIp {} and serverPort {}", clientArgs.getHost(), clientArgs.getPort());
        log.info("path to file {}", clientArgs.getPathToFile());
        log.info("file");
        this.client = new Socket(InetAddress.getByName(clientArgs.getHost()), clientArgs.getPort());
        this.fileStream = new FileInputStream(clientArgs.getPathToFile());
        this.file = new File(clientArgs.getPathToFile());
        log.info("file");
        this.objectOutputStream = new ObjectOutputStream(client.getOutputStream());
        log.info("objectOutputStream");
    }

    private void handlerResponseReceipt() {
        log.info("handler response receipt from server");

        try (ObjectInputStream inputStream = new ObjectInputStream(client.getInputStream())) {
            log.info("server response  " + inputStream.readObject());
        } catch (IOException | ClassNotFoundException e) {
            log.warn("error while sending status result to client " + e.getMessage());
        }
    }

    public void send() throws IOException {
        log.info("call method send()");

        OutputStream output = client.getOutputStream();

        log.info("send object metaInf file: path {} size {}", file.getName(), file.length());
        this.objectOutputStream.writeObject(new FileMetaInfo(file.getName(), file.length()));
        this.objectOutputStream.flush();

        log.info("send file data");
        while (this.fileStream.available() != EMPTY) {
            output.write(this.fileStream.readNBytes(DEFAULT_SIZE_BUFFER_TRANSFER));
            output.flush();
        }
//        client.shutdownOutput();
        log.info("all data from the file has been sent");
        this.handlerResponseReceipt();
    }

    @Override
    public void close() throws Exception {
        log.info("close resources");
        this.objectOutputStream.close();
        this.fileStream.close();
        this.client.close();
    }
}
