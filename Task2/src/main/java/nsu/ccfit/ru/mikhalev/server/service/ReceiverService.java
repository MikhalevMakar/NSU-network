package nsu.ccfit.ru.mikhalev.server.service;

import lombok.extern.slf4j.Slf4j;
import nsu.ccfit.ru.mikhalev.model.FileMetaInfo;

import java.io.*;
import java.net.Socket;

import static nsu.ccfit.ru.mikhalev.context.ContextValue.DEFAULT_SIZE_BUFFER_TRANSFER;
import static nsu.ccfit.ru.mikhalev.context.ContextValue.EMPTY;

@Slf4j
public class ReceiverService implements Runnable, AutoCloseable{

    private final Socket socket;

    private File file;

    private InputStream inputStream;

    private final ObjectInputStream objectInputStream;

    private final byte[] buffer = new byte[DEFAULT_SIZE_BUFFER_TRANSFER];

    public ReceiverService(Socket socket) throws IOException {
        this.socket = socket;
        this.objectInputStream = new ObjectInputStream(socket.getInputStream());
    }

    private void createFile() throws IOException, ClassNotFoundException {
        FileMetaInfo fileMetaInfo = (FileMetaInfo) this.objectInputStream.readObject();

        file = new File(fileMetaInfo.filePath());

        if(file.createNewFile())
            log.info("create new file");
        else
            log.warn("failed to create a file");
    }

    private void fileReceipt() throws IOException {
        log.info("call fileReceipt()");
        FileOutputStream outputStream = new FileOutputStream(file);

        log.info("size file " + file.length());

        int len;
        int offset = EMPTY;

        while((len = inputStream.read(buffer)) >= EMPTY) {
            outputStream.write(buffer, offset, len);
            offset += len;
        }
    }

    @Override
    public void run() {
        try {
            inputStream = socket.getInputStream();
            createFile();
            fileReceipt();
        } catch (FileNotFoundException e) {
            log.warn("file not found exception " + e);
        } catch (ClassNotFoundException e) {
            log.warn("class not found exception " + e);
        } catch (IOException e) {
            log.warn("io exception " + e);
        }
        Thread.currentThread().interrupt();
    }

    @Override
    public void close() throws Exception {
        log.info("close resources");
        this.socket.close();
    }
}