package org.jab.thesourceoftruth.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.BDDAssertions.then;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class ConfigTest {

    @Autowired
    private GlobalConfiguration config;

    @Test
    public void Given_a_config_When_load_the_bean_Then_data_is_loaded() {

        then(config.getRepositories().size() == 1);
        then(config.getRepositories().get(0).getId().equals("MY-TEST-REPO"));
    }

}