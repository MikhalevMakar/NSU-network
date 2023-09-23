package nsu.ccfit.ru.mikhalev.client;

import lombok.extern.slf4j.Slf4j;
import nsu.ccfit.ru.mikhalev.client.console.RunnerParser;
import nsu.ccfit.ru.mikhalev.client.service.ClientService;

import java.io.IOException;
import java.net.ConnectException;

@Slf4j
public class ApplicationClient {
    public static void main(String... args) throws IOException {
        log.info("create runner parser");
        RunnerParser runnerParser = new RunnerParser();

        log.info("create client for connect and receive ");
        try(ClientService service = new ClientService(runnerParser.execute(args))) {
            service.send();
        } catch (ConnectException e) {
            throw new ConnectException(args[0]);
        } catch (Exception e) {
            throw new IOException(e);
        }
    }
}