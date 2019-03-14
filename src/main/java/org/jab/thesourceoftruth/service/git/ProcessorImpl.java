package org.jab.thesourceoftruth.service.git;

import lombok.extern.slf4j.Slf4j;
import org.jab.thesourceoftruth.config.GlobalConfiguration;
import org.jab.thesourceoftruth.config.Repository;
import org.jab.thesourceoftruth.service.shell.Command;
import org.jab.thesourceoftruth.service.shell.Proccess;
import org.jab.thesourceoftruth.service.shell.ProcessResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

            //1. Upgrade git branch
            upgradeBranch(repository);

            //2. Get commit ranges (First & Last)
            getFirstAndLastDate(repository);

            //3.
            getRepoContribution(repository);

        });
    }

    private void getRepoContribution(Repository repository) {
        LOGGER.info("Get repo contribution metadata");
        ProcessResult result4 = shellProcess.execute(new Command.Builder()
                .add("cd " + repository.getPath())
                .add(configuration.getGit() + " shortlog HEAD -sn --no-merges")
                .build());

        result4.getResults().stream().forEach(System.out::println);
    }

    private void getFirstAndLastDate(Repository repository) {
        LOGGER.info("Get first & last commit medatadata");
        ProcessResult result2 = shellProcess.execute(new Command.Builder()
                .add("cd " + repository.getPath())
                .add(configuration.getGit() + " log --format=%ci")
                .build());

        LocalDate lastCommit = result2.getResults().stream().limit(1).map(commitDate -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            return LocalDate.parse(commitDate.substring(0,10), formatter);
        }).findFirst().get();

        ProcessResult result3 = shellProcess.execute(new Command.Builder()
                .add("cd " + repository.getPath())
                .add(configuration.getGit() + " log --reverse --format=%ci")
                .build());

        LocalDate firstCommit = result3.getResults().stream().limit(1).map(commitDate -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            return LocalDate.parse(commitDate.substring(0,10), formatter);
        }).findFirst().get();

        LOGGER.info("{} {}", lastCommit, firstCommit);
    }

    private void upgradeBranch(Repository repository) {
        LOGGER.info("Upgrade git branch");
        ProcessResult result = shellProcess.execute(new Command.Builder()
                .add("cd " + repository.getPath())
                .add(configuration.getGit() + " fetch")
                .add(configuration.getGit() + " status")
                .build());

        result.getResults().stream().forEach(System.out::println);

        try {
            ProcessResult result2 = shellProcess.execute(new Command.Builder()
                    .add("cd " + repository.getPath())
                    .add(configuration.getGit() + " checkout master")
                    //.add(configuration.getGit() + " pull")
                    .build());

        } catch (RuntimeException e) {

        }

        ProcessResult result3 = shellProcess.execute(new Command.Builder()
                .add("cd " + repository.getPath())
                .add(configuration.getGit() + " pull")
                .build());

        result3.getResults().stream().forEach(System.out::println);
    }


}
