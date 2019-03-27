package org.jab.thesourceoftruth.service.shell;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.assertj.core.api.BDDAssertions.then;
import static org.junit.jupiter.api.condition.OS.WINDOWS;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@EnabledOnOs({ WINDOWS })
@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("ev3dev")
public class WindowsJavaSystemShellProcessTest {

    @Autowired
    private Proccess2 shellProcess;

    @Test
    public void Given_a_Windows_command_When_we_execute_Then_we_receive_the_expected_result() throws Exception {

        //GIVEN
        //WHEN
        ProcessResult result = shellProcess.execute(new Command2.Builder()
                                            .add("cd repos/ev3dev-lang-java")
                                            .add("dir")
                                            .build());

        result.getResults().stream().forEach(System.out::println);

        //THEN
        then(result.getResults().size() > 0);
        then(result.getResults().stream().findFirst().get().equals("   651  Juan Antonio Bren<CC><83>a Moral\n"));
    }

    @Test
    public void Given_a_bad_command_When_we_execute_Then_we_throw_an_exception() {

        //GIVEN
        //when(shellProcess.execute(any())).thenThrow(RuntimeException.class);

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
