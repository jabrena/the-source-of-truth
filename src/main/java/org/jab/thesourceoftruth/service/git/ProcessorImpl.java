package org.jab.thesourceoftruth.service.git;

import lombok.extern.slf4j.Slf4j;
import org.jab.thesourceoftruth.config.GlobalConfiguration;
import org.jab.thesourceoftruth.config.Repository;
import org.jab.thesourceoftruth.service.shell.Command;
import org.jab.thesourceoftruth.service.shell.Proccess;
import org.jab.thesourceoftruth.service.shell.ProcessResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class ProcessorImpl implements Processor {

    @Autowired
    private GlobalConfiguration configuration;

    @Autowired
    private Proccess shellProcess;

    final List<Repository> repos;

    public ProcessorImpl(final List<Repository> repos) {
        this.repos = Collections.unmodifiableList(repos);
    }

    @Override
    public void run() {

        repos.stream().forEach(repository -> {
            ProcessResult result = shellProcess.execute(new Command.Builder()
                    .add("cd " + repository.getPath())
                    .add(configuration.getGit() + " shortlog HEAD -sn --no-merges")
                    .build());

            result.getResults().stream().forEach(System.out::println);
        });
    }


}
