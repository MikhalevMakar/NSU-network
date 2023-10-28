package ru.nsu.ccfit.mikhalev.model;

import lombok.*;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;

import static ru.nsu.ccfit.mikhalev.configuration.ContextProxy.BUFFER_SIZE;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Attachment {
    private final ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);

    private SelectionKey peerKey;

    private boolean confirmation = true;

    public void updateConfirmation(boolean confirmation){
        this.confirmation = confirmation;
    }

    public Attachment(SelectionKey peer){
        this.peerKey = peer;
    }


    public void clearData(){
        this.buffer.flip();
        this.buffer.clear();
    }
}