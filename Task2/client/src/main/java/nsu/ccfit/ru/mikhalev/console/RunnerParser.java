package nsu.ccfit.ru.mikhalev.console;

import com.beust.jcommander.*;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nsu.ccfit.ru.mikhalev.exception.RunnerParseException;
import nsu.ccfit.ru.mikhalev.model.ClientArgs;

@Slf4j
@NoArgsConstructor
public class RunnerParser {
    private final ClientArgs clientArgs = new ClientArgs();

    public ClientArgs execute(String... args) {
        log.info("client arg execute");
        try {
            JCommander.newBuilder()
                .addObject(clientArgs)
                .build()
                .parse(args);
        } catch (ParameterException e) {
            log.warn("warn, when parsing command line arguments " + e.getMessage());
            throw new RunnerParseException ();
        }
        return clientArgs;
    }
}
