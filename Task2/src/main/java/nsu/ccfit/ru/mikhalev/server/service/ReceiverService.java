package nsu.ccfit.ru.mikhalev.server.service;

import lombok.extern.slf4j.Slf4j;
import nsu.ccfit.ru.mikhalev.model.*;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;

import static java.lang.Thread.sleep;
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
        try(FileOutputStream outputStream = new FileOutputStream(file)) {
            log.info("size file " + file.length());

            int len;
            long deltaTime = System.nanoTime();

            while ((len = inputStream.read (buffer)) >= EMPTY) {
                speedScheduler.updateDataTransferRate(len / ((System.nanoTime () - deltaTime) / CONVERT_MILISEC_TO_SEC));
                outputStream.write(buffer, EMPTY, len);
                deltaTime = System.nanoTime();
            }
        } catch(InterruptedIOException ex) {
            close();
            log.warn("file output stream ex: " + Arrays.toString(ex.getStackTrace()));
        }
    }

    @Override
    public void run() {
        try {
            inputStream = socket.getInputStream();
            createFile();
            fileReceipt();
        } catch (IOException | ClassNotFoundException e) {
            log.warn("exception " + e.getMessage());
        } finally {
            close();
        }
    }

    public void close() {
        try {
            log.info("close resources");
            sleep(SCHEDULE_TIMER);
            speedScheduler.close();
            this.socket.close();
        } catch (InterruptedException ex) {
            log.warn("InterruptedException sleep()" + ex);
            Thread.currentThread().interrupt();
        } catch (Exception ex) {
           log.warn("failed to close the socket" + ex);
        }
    }
}