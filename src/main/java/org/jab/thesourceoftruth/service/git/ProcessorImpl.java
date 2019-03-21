package org.jab.thesourceoftruth.service.git;

import lombok.extern.slf4j.Slf4j;
import org.jab.thesourceoftruth.config.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class ProcessorImpl implements Processor {

    @Autowired
    private GitMetatadaAnalysis gitMetatadaAnalysis;

    final List<Repository> repos;

    public ProcessorImpl(final List<Repository> repos) {
        this.repos = Collections.unmodifiableList(repos);
    }

    @Override
    public void run() {

        repos.stream().forEach(repository -> {

            //TODO Move to plugin
            LOGGER.info("1. Git Metadata Analysis");

            gitMetatadaAnalysis.run(repository);
        });
    }


}
