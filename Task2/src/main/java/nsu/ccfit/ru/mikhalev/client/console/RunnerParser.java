package nsu.ccfit.ru.mikhalev.client.console;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nsu.ccfit.ru.mikhalev.client.model.ClientArgs;
import nsu.ccfit.ru.mikhalev.core.exception.RunnerParseException;

@Slf4j
@NoArgsConstructor
public class RunnerParser {
    private final ClientArgs clientArgs = new ClientArgs();

    public ClientArgs execute(String... args) {
        try {
            JCommander.newBuilder()
                .addObject(clientArgs)
                .build()
                .parse(args);
        } catch (ParameterException e) {
            log.warn("warn, when parsing command line arguments " + e.getMessage());
            throw new RunnerParseException();
        }
        return clientArgs;
    }
}
