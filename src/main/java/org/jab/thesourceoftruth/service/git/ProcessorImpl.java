package org.jab.thesourceoftruth.service.git;

import io.vavr.Tuple;
import io.vavr.Tuple2;
import lombok.extern.slf4j.Slf4j;
import org.jab.thesourceoftruth.config.GlobalConfiguration;
import org.jab.thesourceoftruth.config.Repository;
import org.jab.thesourceoftruth.model.GitCommitContributionDetail;
import org.jab.thesourceoftruth.model.GitCommitContributions;
import org.jab.thesourceoftruth.service.shell.Command;
import org.jab.thesourceoftruth.service.shell.Proccess;
import org.jab.thesourceoftruth.service.shell.ProcessResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
            Tuple2<LocalDate, LocalDate> commitRanges = getFirstAndLastDate(repository);

            //3. Get contributions
            getRepoContribution(repository, commitRanges);

        });
    }

    private void getRepoContribution(Repository repository, Tuple2<LocalDate, LocalDate> commitRanges) {

        List<GitDevEffort> list = new ArrayList<>();

        for(int year = commitRanges._1.getYear(); year < commitRanges._2.getYear(); year++) {

            for (int month = 1; month < 12; month++) {

                final int currentYear = year;
                final int currentMonth = month;
                int nextMonth = month+1;

                String gitDateFilter = "--since=\"1 " + GitMonths.from(currentMonth) +  ", " + year + "\" --before=\"1 " + GitMonths.from(nextMonth) + ", " + year + "\"";
                //LOGGER.info("{}", gitDateFilter);

                ProcessResult result4 = shellProcess.execute(new Command.Builder()
                        .add("cd " + repository.getPath())
                        .add(configuration.getGit() + " shortlog HEAD -sn --no-merges " + gitDateFilter)
                        .build());

                GitCommitContributions contributions = new GitCommitContributions(result4);
                contributions.adapt().stream().forEach(contribution -> {

                    //LOGGER.info("{}",contribution);

                    ProcessResult result5 = shellProcess.execute(new Command.Builder()
                            .add("cd " + repository.getPath())
                            .add(configuration.getGit() + " log HEAD " + gitDateFilter + " --author=\"" + contribution.getContributor() + "\" --pretty=tformat: --numstat ")
                            .build());

                    GitCommitContributionDetail contributionDetail = new GitCommitContributionDetail(result5);
                    contributionDetail.adapt().stream().forEach(detail -> {
                        list.add(new GitDevEffort(
                                repository.getId(),
                                currentYear,
                                currentMonth,
                                contribution.getContributor(),
                                contribution.getCommit(),
                                detail.getAdded(),
                                detail.getRemoved(),
                                detail.getFile()
                                ));
                    });
                });
            }

        }

        //Starting point for the analysis
        list.stream().map(row -> row.toString()).forEach(LOGGER::info);

        LOGGER.info("Get repo contribution metadata");
        ProcessResult result4 = shellProcess.execute(new Command.Builder()
                .add("cd " + repository.getPath())
                .add(configuration.getGit() + " shortlog HEAD -sn --no-merges")
                .build());

        result4.getResults().stream().forEach(LOGGER::info);
    }

    private Tuple2<LocalDate, LocalDate> getFirstAndLastDate(Repository repository) {
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

        //LOGGER.info("{} {}", lastCommit, firstCommit);

        return Tuple.of(firstCommit, lastCommit);
    }

    private void upgradeBranch(Repository repository) {
        LOGGER.info("Upgrade git branch");
        ProcessResult result = shellProcess.execute(new Command.Builder()
                .add("cd " + repository.getPath())
                .add(configuration.getGit() + " fetch")
                .add(configuration.getGit() + " status")
                .build());

        //result.getResults().stream().forEach(System.out::println);

        //TODO Create a new method for commands which return errors.
        try {
            ProcessResult result2 = shellProcess.execute(new Command.Builder()
                    .add("cd " + repository.getPath())
                    .add(configuration.getGit() + " checkout " + repository.getMain_branch())
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
