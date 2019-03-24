package org.jab.thesourceoftruth.service.git;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ProcessorSystemTest {

    @Autowired
    private Processor processor;

    @Disabled
    @Test
    public void Given_a_configuration_When_call_processor_Then_process_it() throws Exception {

        //GIVEN
        //WHEN
        //THEN
        processor.run();

    }

}
