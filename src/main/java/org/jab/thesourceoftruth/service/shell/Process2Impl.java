package org.jab.thesourceoftruth.service.shell;

import java.io.IOException;
import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.alexander.shell.SystemCommandExecutor;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class Process2Impl implements Proccess2 {

    @Override
    public ProcessResult execute(final Command2 command) {

        try {
            SystemCommandExecutor commandExecutor = new SystemCommandExecutor(command.get());
            int result = commandExecutor.executeCommand();

            StringBuilder stdout = commandExecutor.getStandardOutputFromCommand();
            StringBuilder stderr = commandExecutor.getStandardErrorFromCommand();

            if(result != 0) {
                LOGGER.error("Bad command: {}", command.get());
                throw new RuntimeException(stderr.toString());
            }

            return new ProcessResult(Arrays.asList(stdout.toString().split("\n")));

        } catch (IOException | InterruptedException e) {
            LOGGER.error("Error: ", e);
            throw new RuntimeException(e.getMessage());
        }

    }
}
