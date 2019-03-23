package org.alexander.shell;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnOs;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.BDDAssertions.then;
import static org.junit.jupiter.api.condition.OS.LINUX;
import static org.junit.jupiter.api.condition.OS.MAC;

@EnabledOnOs({ LINUX, MAC })
@Slf4j
public class OSXTest {
  

    @Test
    public void BasicTest() throws Exception {

        //GIVEN
        List<String> commands = new ArrayList<>();
        commands.add("/bin/sh");
        commands.add("-c");
        commands.add("ps aux | wc -l");

        SystemCommandExecutor commandExecutor = new SystemCommandExecutor(commands);
        int result = commandExecutor.executeCommand();

        StringBuilder stdout = commandExecutor.getStandardOutputFromCommand();
        StringBuilder stderr = commandExecutor.getStandardErrorFromCommand();

        LOGGER.info("The numeric result of the command was: {}", result);
        LOGGER.info("STDOUT: {}", stdout);
        LOGGER.info("STDERR: {}", stderr);

        //THEN
        then(result != -99);

    }

    @Test
    public void BasicTest2() throws Exception {

        //GIVEN
        List<String> commands = new ArrayList<>();
        commands.add("/bin/sh");
        commands.add("-c");
        commands.add("ps aux && ls");

        SystemCommandExecutor commandExecutor = new SystemCommandExecutor(commands);
        int result = commandExecutor.executeCommand();

        StringBuilder stdout = commandExecutor.getStandardOutputFromCommand();
        StringBuilder stderr = commandExecutor.getStandardErrorFromCommand();

        LOGGER.info("The numeric result of the command was: {}", result);
        LOGGER.info("STDOUT: {}", stdout);
        LOGGER.info("STDERR: {}", stderr);

        //THEN
        then(result != -99);

    }
}