package nsu.ccfit.ru.mikhalev.client;

import nsu.ccfit.ru.mikhalev.client.model.MetaInfTransferProtocol;
import nsu.ccfit.ru.mikhalev.exception.ConnectException;
import nsu.ccfit.ru.mikhalev.client.service.ClientService;

import java.io.IOException;
public class ApplicationClient {
    public static void main(String[] args) throws IOException {

        try(ClientService service = new ClientService(new MetaInfTransferProtocol (args[0], Integer.parseInt(args[1]), args[2]))) {
            service.send();
        } catch (ConnectException e) {
            throw new ConnectException(args[0]);
        } catch (Exception e) {
            throw new IOException(e);
        }
    }
}