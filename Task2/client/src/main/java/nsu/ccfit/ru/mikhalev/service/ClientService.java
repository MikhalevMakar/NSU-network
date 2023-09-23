package nsu.ccfit.ru.mikhalev.client.service;

import lombok.extern.slf4j.Slf4j;
import nsu.ccfit.ru.mikhalev.client.model.ClientArgs;


import java.io.*;
import java.net.*;


@Slf4j
public class ClientService implements AutoCloseable {

    private final Socket client;

    private final ObjectOutputStream objectOutputStream;

    private final FileInputStream fileStream;

    private final File file;

    public ClientService(ClientArgs clientArgs) throws ConnectException, IOException {
        log.info("create Service client by serverIp {} and serverPort {}", clientArgs.getHost(), clientArgs.getPort());

        this.client = new Socket(InetAddress.getByName(clientArgs.getHost()), clientArgs.getPort());
        this.fileStream = new FileInputStream(clientArgs.getPathToFile());
        this.file = new File(clientArgs.getPathToFile());
        this.objectOutputStream = new ObjectOutputStream(client.getOutputStream());
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
        while (this.fileStream.available () != EMPTY) {
            output.write(this.fileStream.readNBytes(DEFAULT_SIZE_BUFFER_TRANSFER));
            output.flush();
        }

        log.info("all data from the file has been sent");
    }

    @Override
    public void close() throws Exception {
        log.info("close resources");
        this.objectOutputStream.close();
        this.fileStream.close();
        this.client.close();
    }
}
