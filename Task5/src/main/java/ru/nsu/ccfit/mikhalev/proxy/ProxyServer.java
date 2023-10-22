package ru.nsu.ccfit.mikhalev.proxy;

import lombok.extern.slf4j.Slf4j;
import ru.nsu.ccfit.mikhalev.exception.SelectorException;
import ru.nsu.ccfit.mikhalev.model.*;

import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.channels.spi.SelectorProvider;
import java.util.Arrays;

import static ru.nsu.ccfit.mikhalev.configuration.ContextProxy.*;

@Slf4j
public class ProxyServer implements AutoCloseable {

    private static final int SELECT_ERROR = -1;

    private final short port;

    private final Selector selector;

    private final ServerSocketChannel serverChannel;

    public ProxyServer(String host, short port) throws IOException{
        this.port = port;

        this.selector = SelectorProvider.provider().openSelector();

        this.serverChannel = ServerSocketChannel.open();
        this.serverChannel.configureBlocking(false);
        this.serverChannel.socket().bind(new InetSocketAddress(host, port));
        this.serverChannel.register(selector, serverChannel.validOps());
    }

    public void run() {
        try {
            while (selector.select () > SELECT_ERROR) {
                    for (SelectionKey key : selector.selectedKeys())
                        this.switchKey(key);
                    selector.selectedKeys().clear();
            }
        }  catch (IOException ex) {
            throw new SelectorException(ex);
        }
    }

    private void switchKey(SelectionKey key) throws IOException{
        if (key.isAcceptable()) this.accept(key);
        else if(key.isConnectable()) this.connect(key);
        else if (key.isReadable()) this.read(key);
        else if (key.isWritable()) this.write(key);
    }

    private void accept(SelectionKey key) throws IOException{
        log.info("accept");
        SocketChannel newSocketChannel = ((ServerSocketChannel) key.channel()).accept();
        newSocketChannel.configureBlocking(false);
        newSocketChannel.register(selector, SelectionKey.OP_READ);
    }

    private void read(SelectionKey key) throws IOException {
        ByteBuffer header = ByteBuffer.allocate(BUFFER_SIZE);
        SocketChannel channel = (SocketChannel) key.channel();

        int bytesRead = channel.read(header);
        log.info("chanel read  {}", (Arrays.toString(header.array())));
        Attachment attachment = (Attachment) key.attachment();

        if (bytesRead == -1) key.cancel();
        else if(attachment == null) this.readHeader(channel, header, key);
        else if(attachment.isConfirmation()) this.confirmationHeader(key, header, bytesRead, attachment);
        else this.saveData(attachment, header, key);
    }

    private void saveData(Attachment attachment, ByteBuffer data, SelectionKey key) {
        log.info("save data");

        attachment.clearData();
        attachment.putData(data.array());
        data.flip();

        SelectionKey peer = ((Attachment) key.attachment()).getPeer();

        peer.interestOps(peer.interestOps() | SelectionKey.OP_WRITE);
        key.interestOps(key.interestOps() ^ SelectionKey.OP_READ);
    }

    private void readHeader(SocketChannel channel, ByteBuffer header, SelectionKey key) throws IOException {
        log.info ("header {}", Arrays.toString(header.array()));

        ByteBuffer sendBuffer = ByteBuffer.allocate(2);

        sendBuffer.put(VERSION_SOCKS5);
        sendBuffer.put(AUTHENTICATION_NO_REQUIRED);

        sendBuffer.flip();
        channel.write(sendBuffer);
        Attachment attachment = new Attachment();
        attachment.updateConfirmation(true);
        key.attach(attachment);
    }

    private void confirmationHeader(SelectionKey key, ByteBuffer header, int bytesRead, Attachment attachment) throws IOException {
        log.info("confirmationHeader");
        SocksHeader socksHeader = parseHeader(header, bytesRead);
        this.connectToHost(key, socksHeader, attachment);
        attachment.updateConfirmation(false);
    }

    private void requestSendingClient(SocketChannel channel) throws IOException {
        log.info("request sending client");
        ByteBuffer sendBuffer = ByteBuffer.allocate(DEFAULT_SIZE_PARAM + DEFAULT_ADDRESS_BIND.length());

        sendBuffer.put(VERSION_SOCKS5);
        sendBuffer.put(REP);
        sendBuffer.put(RSV);
        sendBuffer.put(A_TYP);
        sendBuffer.put(DEFAULT_ADDRESS_BIND.getBytes());
        sendBuffer.putShort(this.port);
        sendBuffer.flip();
        channel.write(sendBuffer);
    }

    private void connect(SelectionKey key) throws IOException {
        log.info("connect");

        ((SocketChannel) key.channel()).finishConnect();

        SelectionKey peerClient = ((Attachment) key.attachment()).getPeer();

        this.requestSendingClient((SocketChannel) peerClient.channel());
        key.interestOps(REMOVE_PRIVILEGES);
        peerClient.interestOps(SelectionKey.OP_READ);
    }

    private void connectToHost(SelectionKey key, SocksHeader socksHeader, Attachment attachment) throws IOException {
        SocketChannel peer = SocketChannel.open();
        peer.configureBlocking(false);

        peer.connect(new InetSocketAddress(socksHeader.dstIP(), socksHeader.dstPort()));

        SelectionKey peerKey = peer.register(selector, SelectionKey.OP_CONNECT);
        attachment.setPeer(peerKey);
        attachment.setIp(socksHeader.dstIP());
        key.attach(attachment);
        peerKey.attach(new Attachment(key, socksHeader.dstIP()));
    }

    private SocksHeader parseHeader(ByteBuffer header, int lengthHeader) throws UnknownHostException {
        byte[] ipBytes = Arrays.copyOfRange(header.array(), 5, lengthHeader - 2);
        InetAddress ip =  InetAddress.getByName(new String(ipBytes));
        short clientPort = (short)(((header.get(lengthHeader - 2) & 0xFF) << 8) | (header.get(lengthHeader - 1) & 0xFF));
        log.info("host ip {} and port {}", ip.getHostName(), clientPort);
        return new SocksHeader(header.get(0), header.get(1), header.get(2), header.get(3), ip.getHostName(), clientPort);
    }

    private void write(SelectionKey key) throws IOException {
        SocketChannel channel = ((SocketChannel) key.channel());

        Attachment attachmentPeer = ((Attachment)((Attachment) key.attachment()).getPeer().attachment());
        log.info("write ip {}, buffer {}", ((Attachment) key.attachment()).getIp(), Arrays.toString(attachmentPeer.getBuffer().array()));
        channel.write(attachmentPeer.getBuffer());


        key.interestOps(SelectionKey.OP_READ);
        attachmentPeer.getPeer().interestOps(SelectionKey.OP_READ);
        attachmentPeer.clearData();
    }

    @Override
    public void close() throws Exception {
        this.serverChannel.close();
        this.selector.close();
    }
}
