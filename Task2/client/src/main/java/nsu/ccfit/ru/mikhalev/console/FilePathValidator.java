package nsu.ccfit.ru.mikhalev.console;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;

import java.io.File;
import java.nio.charset.StandardCharsets;

import static nsu.ccfit.ru.mikhalev.context.ContextValue.MAX_FILENAME_LENGTH;
import static nsu.ccfit.ru.mikhalev.context.ContextValue.MAX_SIZE_FILE;

public class FilePathValidator implements IParameterValidator {
    @Override
    public void validate(String name, String value) throws ParameterException {
        File file = new File(value);
        if (!file.exists() || !file.isFile()) {
            throw new ParameterException("file does not exist or is not a regular file: " + value);
        }

        String fileName = file.getName();
        if (fileName.getBytes(StandardCharsets.UTF_8).length > MAX_FILENAME_LENGTH) {
            throw new ParameterException("file name exceeds the maximum length of " + MAX_FILENAME_LENGTH + " bytes: " + fileName);
        }

        if(file.length() > MAX_SIZE_FILE) {
            throw new ParameterException("the file is larger than allowed, size: " + MAX_FILENAME_LENGTH);
        }
    }
}