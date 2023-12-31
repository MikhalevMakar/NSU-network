package nsu.ccfit.ru.mikhalev.service;

import lombok.extern.slf4j.Slf4j;
import nsu.ccfit.ru.mikhalev.exception.SessionException;
import nsu.ccfit.ru.mikhalev.model.FileMetaInfo;
import nsu.ccfit.ru.mikhalev.model.SpeedScheduler;

import java.io.*;
import java.net.Socket;

import static nsu.ccfit.ru.mikhalev.context.ContextValue.*;

@Slf4j
public class ReceiverService implements Runnable {

    private final Socket socket;

    private File file;

    private InputStream inputStream;

    private final ObjectInputStream objectInputStream;

    private final byte[] buffer = new byte[DEFAULT_SIZE_BUFFER_TRANSFER];

    private final SpeedScheduler speedScheduler;

    public ReceiverService(Socket socket) throws IOException {
        this.socket = socket;
        this.objectInputStream = new ObjectInputStream(socket.getInputStream());
        speedScheduler = new SpeedScheduler(SCHEDULE_TIMER);
    }

    private FileMetaInfo createFile() throws IOException, ClassNotFoundException {
        FileMetaInfo fileMetaInfo = (FileMetaInfo) this.objectInputStream.readObject();
        file = new File(fileMetaInfo.filePath());
        log.info("size file " + fileMetaInfo.length());

        if(file.createNewFile())
            log.info("create new file by name " + fileMetaInfo.filePath());
        else
            log.warn("failed to create a file");

        log.info("size file " + file.length());
        return fileMetaInfo;
    }

    private void checkStatusResult(long bytesReceived, long totalSize) throws IOException {
        log.info("check result and send to client");
        if (totalSize == bytesReceived) {
            log.info ("{} by client {}", SUCCESSFUL_TRANSMISSION, socket.getInetAddress ());
        } else {
            log.warn("error while sending status result to client " + socket.getInetAddress());
            throw new SessionException(UNSUCCESSFUL_TRANSMISSION);
        }
    }

    private long fileReceipt() throws IOException {
        log.info("call fileReceipt()");
        int bytesReceived = 0;
        try(FileOutputStream outputStream = new FileOutputStream(file)) {
            int len;
            log.info("write to file");
            long deltaTime = System.nanoTime();
            while ((len = inputStream.read(buffer)) >= EMPTY) {
                log.info("receive size data: {} from userIP: {},  {} ", len, socket.getInetAddress(), (System.nanoTime() - deltaTime));
                speedScheduler.updateDataTransferRate((len / ((System.nanoTime() - deltaTime) / CONVERT_MILISEC_TO_SEC)));
                outputStream.write(buffer, EMPTY, len);
                bytesReceived += len;
                deltaTime = System.nanoTime();
            }
            log.info("finish wrote to file");
        } catch(InterruptedIOException e) {
            log.warn("file output stream e: " + e.getMessage());
        }
        return bytesReceived;
    }

    @Override
    public void run() {
        try {
            inputStream = socket.getInputStream();

            FileMetaInfo fileMetaInfo = createFile();
            long bytesReceived = fileReceipt();
            log.info("byte received " + bytesReceived);
            this.checkStatusResult(bytesReceived, fileMetaInfo.length());
        } catch (IOException | ClassNotFoundException e) {
            log.warn("exception " + e.getMessage());
        } finally {
            this.close();
        }
    }

    public void close() {
        try {
            log.info("close resources");

            this.speedScheduler.close();
            this.objectInputStream.close();
            this.socket.close();
        }  catch (Exception ex) {
           log.warn("failed to close the socket" + ex);
        }
    }
}
