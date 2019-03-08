package org.jab.thesourceoftruth.service.shell;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class WindowsJavaShellProcessTest {

    @MockBean
    private Proccess shellProcess;

    @Test
    public void Given_a_Windows_command_When_we_execute_Then_we_receive_the_expected_result() throws Exception {

        //GIVEN
        when(shellProcess.execute(any())).thenReturn(
                new ProcessResult(getContentAsList("git/git-shortlog-sn-no-merges.txt")));

        //WHEN
        ProcessResult result = shellProcess.execute(new Command.Builder()
                                            .add("cd repos/ev3dev-lang-java")
                                            .add("git shortlog HEAD -sn --no-merges")
                                            .build());

        //THEN
        then(result.getResults().size() > 0);
        then(result.getResults().stream().findFirst().get().equals("   651  Juan Antonio Bren<CC><83>a Moral\n"));
    }

    @Test
    public void Given_a_bad_command_When_we_execute_Then_we_throw_an_exception() {

        //GIVEN
        when(shellProcess.execute(any())).thenThrow(RuntimeException.class);

        //WHEN
        //THEN
        Assertions.assertThrows(RuntimeException.class, () -> {
            shellProcess.execute(new Command.Builder()
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
