package nsu.ccfit.ru.mikhalev.model;

import java.io.Serializable;

public record FileMetaInfo(String filePath, long length) implements Serializable { }
