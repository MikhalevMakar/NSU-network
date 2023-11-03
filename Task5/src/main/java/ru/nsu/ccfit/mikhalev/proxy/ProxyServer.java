package ru.nsu.ccfit.mikhalev.proxy;

import lombok.extern.slf4j.Slf4j;
import ru.nsu.ccfit.mikhalev.exception.SelectorException;
import ru.nsu.ccfit.mikhalev.exception.TypeException;
import ru.nsu.ccfit.mikhalev.exception.VersionSocksException;
import ru.nsu.ccfit.mikhalev.model.*;

import java.io.IOException;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;
import java.nio.channels.spi.SelectorProvider;
import java.util.Arrays;

import static ru.nsu.ccfit.mikhalev.configuration.ContextProxy.*;

@Slf4j
public class ProxyServer implements AutoCloseable {

    private final short port;

    private final ServerSocketChannel serverSocket = ServerSocketChannel.open();

    private final Selector selector = SelectorProvider.provider().openSelector();

    public ProxyServer(short port) throws IOException {
        this.port = port;

        this.serverSocket.configureBlocking(false);
        this.serverSocket.socket().bind(new InetSocketAddress(InetAddress.getLoopbackAddress(), port));
        this.serverSocket.register(selector, serverSocket.validOps());
    }

    public void run() {
        try {
            while (selector.select () > SELECT_ERROR) {
                for (SelectionKey key : selector.selectedKeys())
                    this.switchKey(key);
                selector.selectedKeys().clear();
            }
        } catch (IOException ex) {
            throw new SelectorException(ex);
        }
    }

    private void switchKey(SelectionKey key) throws IOException {
        if (key.isAcceptable()) this.accept(key);
        else if(key.isConnectable()) this.connect(key);
        else if (key.isReadable()) this.read(key);
        else if (key.isWritable()) this.write(key);
    }

    private void read(SelectionKey key) {
        SocketChannel channel = ((SocketChannel) key.channel());

        Attachment attachment = (Attachment) key.attachment();
        if (attachment == null) attachment = new Attachment();

        try {
            int byteRead = channel.read(attachment.getBuffer());

            if (byteRead == READ_ERROR || byteRead == 0) return;
            if (attachment.getBuffer().get(INDEX_SOCKS5) == VERSION_SOCKS5 && attachment.isConfirmation()) readHeader(channel, attachment, key);
            else if (attachment.getBuffer().get(INDEX_SOCKS5) == VERSION_SOCKS5) this.confirmationHeader(key, attachment, byteRead);
            else saveData(attachment, key);
        } catch (IOException e) {}
    }

    private void saveData(Attachment attachment, SelectionKey key) {
        log.info("save data");
        key.interestOps(key.interestOps() ^ SelectionKey.OP_READ);
        SelectionKey peerKey = attachment.getPeerKey();
        peerKey.interestOps(peerKey.interestOps() | SelectionKey.OP_WRITE);
        attachment.getBuffer().flip();
    }

    private void readHeader(SocketChannel channel, Attachment headerAtt, SelectionKey key) throws IOException {
        log.info ("header handle connection message");
        ByteBuffer answer = ByteBuffer.allocate(SIZEOF_HEADER_ANSWER);

        answer.put(VERSION_SOCKS5);
        answer.put(AUTHENTICATION_NO_REQUIRED);
        answer.flip();
        channel.write(answer);

        headerAtt.updateConfirmation(false);
        key.attach(headerAtt);
        headerAtt.clearData();
    }

    private void confirmationHeader(SelectionKey key, Attachment attachment, int bytesRead) throws IOException {
        log.info("confirmationHeader");
        SocksHeader socksHeader = parseHeader(attachment.getBuffer(), bytesRead);
        this.establishConnection(key, socksHeader, attachment);
        attachment.updateConfirmation(false);
        attachment.clearData();
    }

    private void establishConnection(SelectionKey key, SocksHeader socksHeader, Attachment attachment) throws IOException {
        log.info("connect new host");
        SocketChannel peer = SocketChannel.open();
        peer.configureBlocking(false);
        peer.connect(new InetSocketAddress(socksHeader.dstIP(), socksHeader.dstPort()));
        SelectionKey newPeer = peer.register(selector, SelectionKey.OP_CONNECT);

        key.interestOps(REMOVE_PRIVILEGES);
        attachment.setPeerKey(newPeer);
        newPeer.attach(new Attachment(key));
    }

    private SocksHeader parseHeader(ByteBuffer buffer, int messageLength) throws UnknownHostException {
        this.checkHeader(buffer.get(0), buffer.get(1), buffer.get(3));
        byte[] ipBytes = Arrays.copyOfRange(buffer.array(), INDEX_IP_HEADER, messageLength - SIZEOF_PORT);
        short clientPort = (short) (((buffer.get(messageLength - SIZEOF_PORT) & 0xFF) << 8) | (buffer.get(messageLength - 1) & 0xFF));
        InetAddress ip = InetAddress.getByName(new String(ipBytes));
        log.info("host ip {} and port {}", ip.getHostName(), clientPort);
        return new SocksHeader(ip, clientPort);
    }

    private void checkHeader(byte versionSocks, byte rep, byte aTyp) {
        log.info("checkHeader");
        if(versionSocks != VERSION_SOCKS5) throw new VersionSocksException(versionSocks);
        if(aTyp != A_TYP_IP4 && aTyp != A_TYP_DOMAIN && aTyp != A_TYP_IPV6) throw new TypeException(aTyp);
    }

    private void write(SelectionKey key) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();

        Attachment attachment = (Attachment) key.attachment();
        if (!attachment.getBuffer().hasRemaining()) return;

        Attachment peerAttachment = (Attachment) attachment.getPeerKey().attachment();
        channel.write(peerAttachment.getBuffer());

        log.info("write buffer {}", Arrays.toString(peerAttachment.getBuffer().array()));
        peerAttachment.clearData();

        key.interestOps(SelectionKey.OP_READ);
        attachment.getPeerKey().interestOps(SelectionKey.OP_READ);
    }

    private void connect(SelectionKey key) throws IOException {
        log.info("connect");
        ((SocketChannel) key.channel()).finishConnect();

        SelectionKey peerKey = ((Attachment) key.attachment()).getPeerKey();
        this.requestSendingClient((SocketChannel) peerKey.channel());
        peerKey.interestOps(SelectionKey.OP_READ);
        key.interestOps(REMOVE_PRIVILEGES);
    }

    private void requestSendingClient(SocketChannel channel) throws IOException {
        log.info("request sending client");
        ByteBuffer request = ByteBuffer.allocate(DEFAULT_SIZE_PARAM + InetAddress.getLoopbackAddress().getAddress().length);

        request.put(VERSION_SOCKS5);
        request.put(REP_CONNECT_SUCCESS);
        request.put(RSV);
        request.put(A_TYP_IP4);
        request.put(InetAddress.getLoopbackAddress().getAddress());
        request.putShort(this.port);
        request.flip();
        channel.write(request);
    }

    private void accept(SelectionKey key) throws IOException{
        log.info("accept");
        SocketChannel client = ((ServerSocketChannel) key.channel()).accept();
        client.configureBlocking(false);
        client.register(selector, SelectionKey.OP_READ);
    }

    @Override
    public void close() throws Exception {
        log.info("close resources");
        this.selector.close();
        this.serverSocket.close();
    }
}