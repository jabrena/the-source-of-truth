package org.jab.microservices.thesourceoftruth.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class GITCommandTest {

    @Test
    public void Given_a_command_When_we_use_the_object_Then_return_right_String() {

        //When
        final GITCommand gitCommand = new GITCommand.Builder()
                .add("Demo")
                .build();

        //Then
        final String expectedCommand = "Demo";
        assertThat(expectedCommand).isEqualTo(gitCommand.toString());
    }

}