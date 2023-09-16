package nsu.ccfit.ru.mikhalev.client.service;

import lombok.extern.slf4j.Slf4j;
import nsu.ccfit.ru.mikhalev.client.model.MetaInfCustomProtocol;
import nsu.ccfit.ru.mikhalev.exception.ConnectException;
import nsu.ccfit.ru.mikhalev.model.FileMetaInfo;

import java.io.*;
import java.net.*;

import static nsu.ccfit.ru.mikhalev.context.ContextValue.*;

@Slf4j
public class ClientService implements AutoCloseable {

    private final Socket client;

    private final MetaInfCustomProtocol metaInfProtocol;

    private final ObjectOutputStream objectOutputStream;

    private final FileInputStream fileStream;

    private final File file;

    public ClientService(String serverIp, Integer serverPort, String pathToFile) throws ConnectException, IOException {
        log.info("create Service client by serverIp {} and serverPort {}", serverIp, serverPort);

        this.client = new Socket(InetAddress.getByName(serverIp), serverPort);
        this.metaInfProtocol = new MetaInfCustomProtocol(serverIp, serverPort, pathToFile);
        this.fileStream = new FileInputStream(this.metaInfProtocol.pathToFile());
        this.file = new File(this.metaInfProtocol.pathToFile());
        this.objectOutputStream = new ObjectOutputStream(client.getOutputStream());
    }

    public void send() throws IOException {
        log.info("call method send()");

        OutputStream output = client.getOutputStream();

        this.objectOutputStream.writeObject(new FileMetaInfo(file.getName(), file.length()));
        this.objectOutputStream.flush();
        while(this.fileStream.available() != EMPTY) {
            output.write(this.fileStream.readNBytes(DEFAULT_SIZE_BUFFER_TRANSFER));
            output.flush();
        }
    }

    @Override
    public void close() throws Exception {
        log.info("close resources");
        client.close();
    }
}
