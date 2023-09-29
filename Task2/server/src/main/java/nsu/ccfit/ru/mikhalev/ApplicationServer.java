package nsu.ccfit.ru.mikhalev;

import nsu.ccfit.ru.mikhalev.exception.ConnectException;
import nsu.ccfit.ru.mikhalev.service.ConnectService;

public class ApplicationServer {

    public static void main(String[] args) {
        try(ConnectService server = new ConnectService(Integer.parseInt(args[0]))) {
            server.execute();
        } catch(Exception ex) {
            throw new ConnectException(args[0]);
        }
    }
}
