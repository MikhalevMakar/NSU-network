package nsu.ccfit.ru.mikhalev.server;


import nsu.ccfit.ru.mikhalev.exception.ConnectException;
import nsu.ccfit.ru.mikhalev.server.service.ConnectService;

public class ApplicationServer {

    public static void main(String[] args) {
        System.out.println("hello");
        try(ConnectService server = new ConnectService(Integer.parseInt(args[0]))) {
            server.execute();
        } catch(Exception ex ) {
            throw new ConnectException(args[0]);
        }
    }
}
