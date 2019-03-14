package org.jab.thesourceoftruth.service.shell;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class ProcessImpl implements Proccess {

    public ProcessResult execute(final Command command) {

        try {
            List<String> lines = new ArrayList<>();
            List<String> errLines = new ArrayList<>();

            LOGGER.info("Command: {}", command.toString());

            Process process = Runtime.getRuntime().exec(command.toString());

            // Get input streams
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream(), "UTF-8"));
            BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));

            // Read command standard output
            String s;
            LOGGER.info("Standard output: ");
            while ((s = stdInput.readLine()) != null) {
                lines.add(s);
            }

            while ((s = stdError.readLine()) != null) {
                errLines.add(s);
            }
            if(errLines.size() > 0) {
                LOGGER.error("Bad command: {}", command);
                errLines.stream().forEach(line -> LOGGER.error("{}", line));
                throw new IllegalArgumentException(command.toString());
            }

            return new ProcessResult(lines);

        } catch (Exception e) {
            LOGGER.error("Error:", e);
            throw new RuntimeException(e.getMessage());
        }
    }
}
