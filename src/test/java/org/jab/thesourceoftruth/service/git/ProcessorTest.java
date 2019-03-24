package org.jab.thesourceoftruth.service.git;

import java.util.ArrayList;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ProcessorTest {

    @MockBean
    private GitMetatadaAnalysis gitMetatadaAnalysis;

    @Autowired
    private Processor processor;

    @Test
    public void Given_a_configuration_When_call_processor_Then_process_it() throws Exception {

        //GIVEN
        when(gitMetatadaAnalysis.run(any())).thenReturn(new ArrayList<GitDevEffort>());

        //WHEN
        processor.run();

        //THEN
        verify(gitMetatadaAnalysis, times(3)).run(any());
    }

}
