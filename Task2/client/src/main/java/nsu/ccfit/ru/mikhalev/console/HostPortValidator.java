package nsu.ccfit.ru.mikhalev.console;

import com.beust.jcommander.*;

import static nsu.ccfit.ru.mikhalev.context.ContextValue.*;


public class HostPortValidator implements IParameterValidator {
    @Override
    public void validate(String name, String value) throws ParameterException{
        try {
            int port = Integer.parseInt(value);
            if (port < MIN_SIZE_PORT || port > MAX_SIZE_PORT) {
                throw new ParameterException("invalid port value: " + value);
            }
        } catch (NumberFormatException e) {
            throw new ParameterException("port must be a valid integer: " + value);
        }
    }
}