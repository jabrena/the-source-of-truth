package org.jab.microservices.thesourceoftruth.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class GITCommandTest {

    @Test
    public void Given_no_command_When_we_build_the_command_Then_return_the_right_String() {

        //When
        final GITCommand gitCommand = new GITCommand.Builder()
                .build();

        //Then
        final String expectedCommand = "";
        assertThat(gitCommand.toString()).isEqualTo(expectedCommand);
    }

    @Test
    public void Given_one_command_When_we_build_the_command_Then_return_the_right_String() {

        //When
        final GITCommand gitCommand = new GITCommand.Builder()
                .add("Demo")
                .build();

        //Then
        final String expectedCommand = "Demo";
        assertThat(gitCommand.toString()).isEqualTo(expectedCommand);
    }

    @Test
    public void Given_two_commands_When_we_build_the_command_Then_return_the_right_String() {

        //When
        final GITCommand gitCommand = new GITCommand.Builder()
                .add("Demo")
                .add("Demo")
                .build();

        //Then
        final String expectedCommand = "Demo Demo";
        assertThat(gitCommand.toString()).isEqualTo(expectedCommand);
    }

}