package ru.nsu.ccfit.mikhalev;

import lombok.extern.slf4j.Slf4j;
import ru.nsu.ccfit.mikhalev.exception.BuildProxyServerException;
import ru.nsu.ccfit.mikhalev.proxy.ProxyServer;

@Slf4j
public class Main {
    public static void main(String[] args) {
        try(ProxyServer proxyServer = new ProxyServer( Short.parseShort(args[0]))) {
            log.info("run proxy service");
            proxyServer.run();
        } catch(Exception ex) {
            throw new BuildProxyServerException(ex);
        }
    }
}