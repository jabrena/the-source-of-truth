package org.jab.microservices.thesourceoftruth.model.shell;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Arrays;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class WindowsJavaShellProcessTest {

    //git.exe shortlog HEAD -sn --no-merges
    //git.exe shortlog HEAD -sn --no-merges --since="1 Jan, 2019" --before="1 Feb, 2019""
    //git.exe log HEAD --since="1 Jan, 2018" --before="1 Feb, 2019" --author="Juan Antonio Brena Moral" --pretty=tformat: --numstat"

    @MockBean
    private ShellProccess shellProcess;

    @Test
    public void Given_a_Windows_command_When_we_execute_Then_we_receive_the_expected_result() {

        //GIVEN
        Mockito.when(shellProcess.execute(any())).thenReturn(new ShellProcessResult(
                new ArrayList<String>(Arrays.asList("Results Line 1", "Results Line 3", "Results Line 3"))));

        //WHEN
        ShellProcessResult result = shellProcess.execute(new ShellCommand.Builder()
                                            .add("cd repos/ev3dev-lang-java")
                                            .add("git shortlog HEAD -sn --no-merges")
                                            .build());

        //THEN
        then(result.getResults().size() > 0);
        then(result.getResults().stream().findFirst().get().equals("Results Line 1"));
    }

    @Test
    public void Given_a_bad_command_When_we_execute_Then_we_throw_an_exception() {

        //GIVEN
        Mockito.when(shellProcess.execute(any())).thenThrow(RuntimeException.class);

        //WHEN
        //THEN
        Assertions.assertThrows(RuntimeException.class, () -> {
            shellProcess.execute(new ShellCommand.Builder()
                    .add("cd repos/ev3dev-lang-java")
                    .add("git --bad-argument")
                    .build());
        });

    }

}
