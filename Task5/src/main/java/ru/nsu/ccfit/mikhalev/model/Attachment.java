package ru.nsu.ccfit.mikhalev.model;

import lombok.*;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;

import static ru.nsu.ccfit.mikhalev.configuration.ContextProxy.BUFFER_SIZE;

@Getter
public class Attachment {

    private final ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);

    @Setter
    private SelectionKey peer;

    @Setter
    private String ip;

    private boolean confirmation;

    public Attachment() {
        this.confirmation = true;
    }

    public void updateConfirmation(boolean confirmation) {
        this.confirmation = confirmation;
    }

    public Attachment(SelectionKey peer, String ip) {
        this.peer = peer;
        this.ip = ip;
    }


    public void clearData() {
        this.buffer.clear();
    }
    public void putData(byte[] data) {
        this.buffer.put(data);
    }
}
