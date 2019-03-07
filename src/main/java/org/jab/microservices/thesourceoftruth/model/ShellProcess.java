package org.jab.microservices.thesourceoftruth.model;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ShellProcess {

    private final String command;

    public ShellProcess(final ShellCommand command) {
        this.command = command.toString();
    }

    public ShellProcessResult run() {

        try {
            List<String> lines = new ArrayList<>();

            LOGGER.info("Command: {}", command);

            Process process = Runtime.getRuntime().exec(command);

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
                throw new IllegalArgumentException(command);
            }

            return new ShellProcessResult(lines);

        } catch (Exception e) {
            LOGGER.error("Error:", e);
            throw new RuntimeException(e.getMessage());
        }
    }
}
