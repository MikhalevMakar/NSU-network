package nsu.ccfit.ru.mikhalev.netserver.model.message;

import com.google.protobuf.InvalidProtocolBufferException;
import nsu.ccfit.ru.mikhalev.ecxeption.TypeCaseException;
import nsu.ccfit.ru.mikhalev.protobuf.snakes.SnakesProto;

import java.net.DatagramPacket;

public class MessageHandler implements Runnable {

    private final DatagramPacket packet;

    public MessageHandler(DatagramPacket packet) {
        this.packet = packet;
    }

    @Override
    public void run() {
        try {
            SnakesProto.GameMessage gameMessage = SnakesProto.GameMessage.parseFrom(packet.getData());
            switch (gameMessage.getTypeCase()) {
                case PING:
                case STEER:
                case ACK:
                case STATE:
                case ANNOUNCEMENT:
                case JOIN:
                case ERROR:
                case ROLE_CHANGE:
                case DISCOVER:
                case TYPE_NOT_SET:
                throw new TypeCaseException();
            }
        } catch (InvalidProtocolBufferException e) {
            throw new RuntimeException(e);
        }
    }
}
