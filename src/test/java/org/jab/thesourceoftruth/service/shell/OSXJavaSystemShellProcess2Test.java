package org.jab.thesourceoftruth.service.shell;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.BDDAssertions.then;
import static org.junit.jupiter.api.condition.OS.LINUX;
import static org.junit.jupiter.api.condition.OS.MAC;

@EnabledOnOs({ LINUX, MAC })
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class OSXJavaSystemShellProcess2Test {

    @Autowired
    private Proccess2 shellProcess;

    @Disabled
    @Test
    public void Given_a_Windows_command_When_we_execute_Then_we_receive_the_expected_result() throws Exception {

        //GIVEN
        //WHEN
        ProcessResult result = shellProcess.execute(new Command2.Builder()
                                            .add("git -C repos/ev3dev-lang-java shortlog HEAD -sn --no-merges")
                                            .build());

        //THEN
        then(result.getResults().size()).isGreaterThan(0);
        then(result.getResults().stream().findFirst().get()).contains("   651\tJuan Antonio Breña Moral");
    }

    @Disabled
    @Test
    public void Given_a_Windows_command_When_we_execute_Then_we_receive_the_expected_result2() throws Exception {

        //WHEN
        ProcessResult result = shellProcess.execute(new Command2.Builder()
                .add("cd repos/ev3dev-lang-java")
                .add("git shortlog HEAD -sn --no-merges")
                .build());

        //THEN
        then(result.getResults().size()).isGreaterThan(0);
        then(result.getResults().stream().findFirst().get()).contains("   651\tJuan Antonio Breña Moral");
    }

    @Disabled
    @Test
    public void Given_a_Windows_command_When_we_execute_Then_we_receive_the_expected_result3() throws Exception {

        //WHEN
        ProcessResult result = shellProcess.execute(new Command2.Builder()
                .add("cd repos/ev3dev-lang-java")
                .add("git shortlog HEAD -sn --no-merges")
                .add("ps aux | wc -l")
                .build());

        result.getResults().stream().forEach(System.out::println);

        //THEN
        then(result.getResults().size()).isGreaterThan(0);
        then(result.getResults().stream().findFirst().get()).contains("   651\tJuan Antonio Breña Moral");
    }

    @Disabled
    @Test
    public void Given_a_bad_command_When_we_execute_Then_we_throw_an_exception() {

        //WHEN
        //THEN
        Assertions.assertThrows(RuntimeException.class, () -> {
            shellProcess.execute(new Command2.Builder()
                    .add("cd repos/ev3dev-lang-java")
                    .add("git --bad-argument")
                    .build());
        });

    }

}
