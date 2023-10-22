package ru.nsu.ccfit.mikhalev.model;

public record SocksHeader(byte version, byte cmd, byte rsv, byte aTyp, String dstIP, short dstPort){}