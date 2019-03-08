package org.jab.thesourceoftruth.service.git;

import org.jab.thesourceoftruth.config.Repository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class ProcessorImpl implements Processor {

    final List<Repository> repos;

    public ProcessorImpl(final List<Repository> repos) {
        this.repos = Collections.unmodifiableList(repos);
    }

    @Override
    public void run() {
        //repos.stream()
        //        .forEach();
    }
}
