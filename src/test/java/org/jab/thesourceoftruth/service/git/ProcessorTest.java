package org.jab.thesourceoftruth.service.git;

import org.jab.thesourceoftruth.service.shell.Proccess;
import org.jab.thesourceoftruth.service.shell.ProcessResult;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.jab.thesourceoftruth.service.shell.WindowsJavaShellProcessTest.getContentAsList;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ProcessorTest {

    @MockBean
    private Proccess shellProcess;

    @Autowired
    private Processor processor;

    @Test
    public void Given_a_configuration_When_call_processor_Then_process_it() throws Exception {

        //GIVEN
        when(shellProcess.execute(any())).thenReturn(
                new ProcessResult(getContentAsList("git/git-shortlog-sn-no-merges.txt")));

        //WHEN
        processor.run();

        //THEN
        verify(shellProcess).execute(any());
    }

}
