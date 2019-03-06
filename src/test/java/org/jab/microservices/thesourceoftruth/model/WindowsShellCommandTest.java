package org.jab.microservices.thesourceoftruth.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class WindowsShellCommandTest {

    @Test
    public void Given_no_command_When_we_build_the_command_Then_return_the_right_String() {

        //Given
        //When
        final ShellCommand gitCommand = new ShellCommand.Builder()
                .build();

        //Then
        final String expectedCommand = "cmd /c \"\"";
        assertThat(gitCommand.toString()).isEqualTo(expectedCommand);
    }

    @Test
    public void Given_one_command_When_we_build_the_command_Then_return_the_right_String() {

        //Given
        //When
        final ShellCommand gitCommand = new ShellCommand.Builder()
                .add("Demo")
                .build();

        //Then
        final String expectedCommand = "cmd /c \"Demo\"";
        assertThat(gitCommand.toString()).isEqualTo(expectedCommand);
    }

    @Test
    public void Given_two_commands_When_we_build_the_command_Then_return_the_right_String() {

        //Given
        //When
        final ShellCommand gitCommand = new ShellCommand.Builder()
                .add("Demo")
                .add("Demo")
                .build();

        //Then
        final String expectedCommand = "cmd /c \"Demo && Demo\"";
        assertThat(gitCommand.toString()).isEqualTo(expectedCommand);
    }

}