package nsu.ccfit.ru.mikhalev.client.console;

import com.beust.jcommander.*;
import nsu.ccfit.ru.mikhalev.core.exception.InetAddressException;

import java.net.*;

public class HostIPValidator implements IParameterValidator {

    @Override
    public void validate(String name, String value) throws ParameterException, InetAddressException {
        try {
            InetAddress address = InetAddress.getByName(value);
            if (address == null) {
                throw new ParameterException("invalid host value: " + value);
            }
        } catch (UnknownHostException e) {
            throw new InetAddressException(e);
        }
    }
}
