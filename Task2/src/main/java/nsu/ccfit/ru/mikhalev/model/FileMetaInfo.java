package nsu.ccfit.ru.mikhalev.model;

import java.io.*;

public record FileMetaInfo(String filePath, long length) implements Serializable { }
