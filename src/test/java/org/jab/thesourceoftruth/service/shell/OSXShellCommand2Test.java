package org.jab.thesourceoftruth.service.shell;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnOs;

import static org.assertj.core.api.BDDAssertions.then;
import static org.junit.jupiter.api.condition.OS.LINUX;
import static org.junit.jupiter.api.condition.OS.MAC;

@EnabledOnOs({ LINUX, MAC })
public class OSXShellCommand2Test {

    @Test
    public void Given_no_command_When_we_build_the_command_Then_return_the_right_String() {

        //Given
        //When
        final Command2 gitCommand = new Command2.Builder().build();

        //Then
        final String expectedCommand = "/bin/sh -c ";
        then(gitCommand.toString()).isEqualTo(expectedCommand);
    }

    @Test
    public void Given_one_command_When_we_build_the_command_Then_return_the_right_String() {

        //Given
        //When
        final Command2 gitCommand = new Command2.Builder()
                .add("Demo")
                .build();

        //Then
        final String expectedCommand = "/bin/sh -c Demo";
        then(gitCommand.toString()).isEqualTo(expectedCommand);
    }

    @Test
    public void Given_two_commands_When_we_build_the_command_Then_return_the_right_String() {

        //Given
        //When
        final Command2 gitCommand = new Command2.Builder()
                .add("Demo")
                .add("Demo")
                .build();

        //Then
        final String expectedCommand = "/bin/sh -c Demo && Demo";
        then(gitCommand.toString()).isEqualTo(expectedCommand);
    }

    @Test
    public void Given_two_commands_When_we_build_the_command_Then_return_the_right_String2() {

        //Given
        //When
        final Command2 gitCommand = new Command2.Builder()
                .add("ps aux | wc -l")
                .add("ls")
                .build();

        //Then
        final String expectedCommand = "/bin/sh -c ps aux | wc -l && ls";
        then(gitCommand.toString()).isEqualTo(expectedCommand);
    }

}