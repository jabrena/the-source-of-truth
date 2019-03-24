package org.jab.thesourceoftruth.service.shell;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnOs;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.condition.OS.WINDOWS;

@EnabledOnOs({ WINDOWS })
public class WindowsShellCommandTest {

    @Test
    public void Given_no_command_When_we_build_the_command_Then_return_the_right_String() {

        //Given
        //When
        final Command gitCommand = new Command.Builder()
                .build();

        //Then
        final String expectedCommand = "cmd /c \"\"";
        assertThat(gitCommand.toString()).isEqualTo(expectedCommand);
    }

    @Test
    public void Given_one_command_When_we_build_the_command_Then_return_the_right_String() {

        //Given
        //When
        final Command gitCommand = new Command.Builder()
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
        final Command gitCommand = new Command.Builder()
                .add("Demo")
                .add("Demo")
                .build();

        //Then
        final String expectedCommand = "cmd /c \"Demo && Demo\"";
        assertThat(gitCommand.toString()).isEqualTo(expectedCommand);
    }

}