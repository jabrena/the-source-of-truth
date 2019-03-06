package org.jab.microservices.thesourceoftruth.model;

import org.junit.jupiter.api.Test;

public class WindowsJavaShellProcessTest {

    //git.exe shortlog HEAD -sn --no-merges
    //git.exe shortlog HEAD -sn --no-merges --since="1 Jan, 2019" --before="1 Feb, 2019""
    //git.exe log HEAD --since="1 Jan, 2018" --before="1 Feb, 2019" --author="Juan Antonio Brena Moral" --pretty=tformat: --numstat"

    @Test
    public void Given_a_Windows_command_When_we_execute_Then_we_receive_the_expected_result() {

        /*
        ShellProcessResult result = new ShellProcess(new ShellCommand.Builder()
                                            .add("cd repos/ev3dev-lang-java")
                                            .add("git shortlog HEAD -sn --no-merges")
                                            .build()).run();

        result.getResults().stream().forEach(System.out::println);
        */
    }

}
