package org.jab.thesourceoftruth.service.shell;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class ShellProcessImpl implements ShellProccess {

    public ShellProcessResult execute(final ShellCommand command) {

        try {
            List<String> lines = new ArrayList<>();

            LOGGER.info("Command: {}", command.toString());

            Process process = Runtime.getRuntime().exec(command.toString());

            // Get input streams
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));

            // Read command standard output
            String s;
            LOGGER.info("Standard output: ");
            while ((s = stdInput.readLine()) != null) {
                lines.add(s);
            }

            if(stdError.readLine() != null) {
                LOGGER.error("Bad command: {}", command);
                throw new IllegalArgumentException(command.toString());
            }

            return new ShellProcessResult(lines);

        } catch (Exception e) {
            LOGGER.error("Error:", e);
            throw new RuntimeException(e.getMessage());
        }
    }
}
