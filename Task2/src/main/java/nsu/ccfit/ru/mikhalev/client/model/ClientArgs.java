package nsu.ccfit.ru.mikhalev.client.model;

import com.beust.jcommander.Parameter;
import lombok.Data;
import nsu.ccfit.ru.mikhalev.client.console.*;

@Data
public class ClientArgs {
    @Parameter(names = { "-h", "--host" }, description = "Server host name or IP address",
               required = true, validateWith = HostIPValidator.class)
    private String host;

    @Parameter(names = { "-p", "--port" }, description = "Server port",
               required = true, validateWith = HostPortValidator.class)
    private int port;

    @Parameter(names = { "-f", "--file" }, description = "Path to the file to send",
               required = true, validateWith = FilePathValidator.class)
    private String pathToFile;
}
