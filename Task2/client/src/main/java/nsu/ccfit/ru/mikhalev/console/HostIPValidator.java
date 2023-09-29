package nsu.ccfit.ru.mikhalev.console;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;
import nsu.ccfit.ru.mikhalev.exception.InetAddressException;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class HostIPValidator implements IParameterValidator {

    @Override
    public void validate(String name, String value) throws ParameterException, InetAddressException {
        try {
            InetAddress address = InetAddress.getByName(value);
            if (address == null) {
                throw new ParameterException("invalid host value: " + value);
            }
        } catch (UnknownHostException e) {
            throw new InetAddressException (e);
        }
    }
}
