package org.alexander.shell;

import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnOs;

import static org.assertj.core.api.BDDAssertions.then;
import static org.junit.jupiter.api.condition.OS.LINUX;
import static org.junit.jupiter.api.condition.OS.MAC;

@EnabledOnOs({ LINUX, MAC })
@Slf4j
public class OSXTest {
  

    @Test
    public void BasicTest() throws Exception {

        //GIVEN
        //WHEN
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
        then(result).isEqualTo(0);

    }

    @Test
    public void BasicTest2() throws Exception {

        //GIVEN
        //WHEN
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
        then(result).isEqualTo(0);

    }

    @Test
    public void BasicTest3() throws Exception {

        //GIVEN
        //WHEN
        List<String> commands = new ArrayList<>();
        commands.add("/bin/sh");
        commands.add("-c");
        commands.add("git --BAD-OPTION");

        SystemCommandExecutor commandExecutor = new SystemCommandExecutor(commands);
        int result = commandExecutor.executeCommand();

        StringBuilder stdout = commandExecutor.getStandardOutputFromCommand();
        StringBuilder stderr = commandExecutor.getStandardErrorFromCommand();

        LOGGER.info("The numeric result of the command was: {}", result);
        LOGGER.info("STDOUT: {}", stdout);
        LOGGER.info("STDERR: {}", stderr);

        //THEN
        then(result).isGreaterThan(0);
    }

    @Test
    public void BasicTest4() throws Exception {

        //GIVEN
        //WHEN
        List<String> commands = new ArrayList<>();
        commands.add("/bin/sh");
        commands.add("-c");
        commands.add("ps aux | wc -l && ls");

        SystemCommandExecutor commandExecutor = new SystemCommandExecutor(commands);
        int result = commandExecutor.executeCommand();

        StringBuilder stdout = commandExecutor.getStandardOutputFromCommand();
        StringBuilder stderr = commandExecutor.getStandardErrorFromCommand();

        LOGGER.info("The numeric result of the command was: {}", result);
        LOGGER.info("STDOUT: {}", stdout);
        LOGGER.info("STDERR: {}", stderr);

        //THEN
        then(result).isEqualTo(0);

    }

    @Test
    public void BasicTest5() throws Exception {

        //GIVEN
        List<String> commands = new ArrayList<>();
        commands.add("/bin/sh");
        commands.add("-c");
        commands.add("(ps aux | wc -l) && ls");

        //WHEN
        SystemCommandExecutor commandExecutor = new SystemCommandExecutor(commands);
        int result = commandExecutor.executeCommand();

        StringBuilder stdout = commandExecutor.getStandardOutputFromCommand();
        StringBuilder stderr = commandExecutor.getStandardErrorFromCommand();

        //THEN
        then(result).isEqualTo(0);
        then(stdout.length()).isGreaterThan(0);
        then(stderr.length()).isEqualTo(0);
    }

    @Disabled
    @Test
    public void BasicTest6() throws Exception {

        //GIVEN
        List<String> commands = new ArrayList<>();
        commands.add("/bin/sh");
        commands.add("-c");
        commands.add("git -C repos/ev3dev-lang-java shortlog HEAD -sn --no-merges");

        //WHEN
        SystemCommandExecutor commandExecutor = new SystemCommandExecutor(commands);
        int result = commandExecutor.executeCommand();

        LOGGER.info("{}", commandExecutor.getStandardOutputFromCommand().toString());

        //THEN
        then(result).isEqualTo(0);
        then(commandExecutor.getStandardOutputFromCommand().length()).isGreaterThan(0);
        then(commandExecutor.getStandardErrorFromCommand().length()).isEqualTo(0);
    }
}