package ru.nsu.ccfit.mikhalev.model;

import java.net.InetAddress;

public record SocksHeader(InetAddress dstIP, short dstPort){}