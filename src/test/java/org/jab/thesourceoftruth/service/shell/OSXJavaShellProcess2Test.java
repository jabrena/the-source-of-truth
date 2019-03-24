package org.jab.thesourceoftruth.service.shell;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.BDDAssertions.then;
import static org.junit.jupiter.api.condition.OS.LINUX;
import static org.junit.jupiter.api.condition.OS.MAC;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@EnabledOnOs({ LINUX, MAC })
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class OSXJavaShellProcess2Test {

    @MockBean
    private Proccess2 shellProcess;

    @Test
    public void Given_a_Windows_command_When_we_execute_Then_we_receive_the_expected_result() throws Exception {

        //GIVEN
        when(shellProcess.execute(any())).thenReturn(
                new ProcessResult(getContentAsList("git/git-shortlog-sn-no-merges.txt")));

        //WHEN
        ProcessResult result = shellProcess.execute(new Command2.Builder()
                                            .add("cd repos/ev3dev-lang-java")
                                            .add("git shortlog HEAD -sn --no-merges")
                                            .build());

        //THEN
        then(result.getResults().size()).isGreaterThan(0);
        then(result.getResults().stream().findFirst().get()).contains("   651\tJuan Antonio Breña Moral");
    }

    @Test
    public void Given_a_Windows_command_When_we_execute_Then_we_receive_the_expected_result2() throws Exception {

        //GIVEN
        when(shellProcess.execute(any())).thenReturn(
                new ProcessResult(getContentAsList("git/git-shortlog-sn-no-merges.txt")));

        //WHEN
        ProcessResult result = shellProcess.execute(new Command2.Builder()
                .add("cd repos/ev3dev-lang-java")
                .add("git shortlog HEAD -sn --no-merges")
                .build());

        //THEN
        then(result.getResults().size()).isGreaterThan(0);
        then(result.getResults().stream().findFirst().get()).contains("   651\tJuan Antonio Breña Moral");
    }

    @Test
    public void Given_a_bad_command_When_we_execute_Then_we_throw_an_exception() {

        //GIVEN
        when(shellProcess.execute(any())).thenThrow(RuntimeException.class);

        //WHEN
        //THEN
        Assertions.assertThrows(RuntimeException.class, () -> {
            shellProcess.execute(new Command2.Builder()
                    .add("cd repos/ev3dev-lang-java")
                    .add("git --bad-argument")
                    .build());
        });

    }

    public static List<String> getContentAsList(final String path) {

        try {
            return Files.readAllLines(Paths.get(ClassLoader.getSystemResource(path).toURI()));
        }catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

}
