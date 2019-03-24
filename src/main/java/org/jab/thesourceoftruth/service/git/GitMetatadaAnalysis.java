package org.jab.thesourceoftruth.service.git;

import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.control.Try;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.jab.thesourceoftruth.config.GlobalConfiguration;
import org.jab.thesourceoftruth.config.Repository;
import org.jab.thesourceoftruth.model.Contribution;
import org.jab.thesourceoftruth.model.ContributionDetail;
import org.jab.thesourceoftruth.model.GitCommitContributionDetail;
import org.jab.thesourceoftruth.model.GitCommitContributions;
import org.jab.thesourceoftruth.service.shell.Command2;
import org.jab.thesourceoftruth.service.shell.Proccess2;
import org.jab.thesourceoftruth.service.shell.ProcessResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class GitMetatadaAnalysis {

    @Autowired
    private GlobalConfiguration configuration;

    @Autowired
    private Proccess2 shellProcess;

    public List<GitDevEffort> run(final Repository repository) {

        //1. Checkout
        checkout(repository);

        //2. Upgrade git branch
        upgradeBranch(repository);

        //3. Get commit ranges (First & Last)
        Tuple2<LocalDate, LocalDate> commitRanges = getFirstAndLastDate(repository);

        //4. Get contributions
        return getRepoContribution(repository, commitRanges);
    }

    private void checkout(Repository repository) {

        LOGGER.info("Checkout repo");

        File directory = new File("repos");
        if (! directory.exists()){
            directory.mkdir();
        }

        if (Files.notExists(Paths.get(repository.getPath()))) {
            shellProcess.execute(new Command2.Builder()
                    .add("cd repos")
                    .add(configuration.getGit() + " clone " + repository.getAddress())
                    .build());
        }
    }

    private List<GitDevEffort> getRepoContribution(Repository repository, Tuple2<LocalDate, LocalDate> commitRanges) {

        LOGGER.info("Get Git Dev Effort metadata");

        List<GitDevEffort> list = new ArrayList<>();

        int year;
        int month;
        for(year = commitRanges._1.getYear(); year < commitRanges._2.getYear(); year++) {

            LOGGER.info("Analyzing git metadata in year: {}", year);

            for (month = 1; month < 12; month++) {

                int nextMonth = month+1;

                String gitDateFilter = "";
                if(month == 12) {
                    gitDateFilter = "--since=\"1 " + "Dec" +  ", " + year + "\" --before=\"1 " + "Jan" + ", " + (year+1) + "\"";
                }else {
                    gitDateFilter = "--since=\"1 " + GitMonths.from(month) +  ", " + year + "\" --before=\"1 " + GitMonths.from(nextMonth) + ", " + year + "\"";
                }
                //LOGGER.info("{}", gitDateFilter);

                ProcessResult result4 = shellProcess.execute(new Command2.Builder()
                        .add("cd " + repository.getPath())
                        .add(configuration.getGit() + " shortlog HEAD -sn --no-merges " + gitDateFilter)
                        .build());

                GitCommitContributions contributions = new GitCommitContributions(result4);
                
                for (Contribution contribution: contributions.adapt()) {
                    ProcessResult result5 = shellProcess.execute(new Command2.Builder()
                            .add("cd " + repository.getPath())
                            .add(configuration.getGit() + " log HEAD " + gitDateFilter + " --author=\"" + contribution.getContributor() + "\" --pretty=tformat: --numstat ")
                            .build());

                    GitCommitContributionDetail contributionDetail = new GitCommitContributionDetail(result5);

                    for (ContributionDetail detail: contributionDetail.adapt()) {
                        list.add(new GitDevEffort(
                                repository.getId(),
                                year,
                                month,
                                contribution.getContributor(),
                                contribution.getCommit(),
                                detail.getAdded(),
                                detail.getRemoved(),
                                detail.getFile()
                        ));
                    }
                }
            }

        }

        LOGGER.info("Get repo contribution metadata");
        ProcessResult result4 = shellProcess.execute(new Command2.Builder()
                .add("cd " + repository.getPath())
                .add(configuration.getGit() + " shortlog HEAD -sn --no-merges")
                .build());

        result4.getResults().stream().forEach(LOGGER::info);

        return list;
    }

    private Tuple2<LocalDate, LocalDate> getFirstAndLastDate(Repository repository) {
        LOGGER.info("Get first & last commit medatadata");
        ProcessResult result2 = shellProcess.execute(new Command2.Builder()
                .add("cd " + repository.getPath())
                .add(configuration.getGit() + " log --format=%ci")
                .build());

        LocalDate lastCommit = result2.getResults().stream().limit(1).map(commitDate -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            return LocalDate.parse(commitDate.substring(0,10), formatter);
        }).findFirst().get();

        ProcessResult result3 = shellProcess.execute(new Command2.Builder()
                .add("cd " + repository.getPath())
                .add(configuration.getGit() + " log --reverse --format=%ci")
                .build());

        LocalDate firstCommit = result3.getResults().stream().limit(1).map(commitDate -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            return LocalDate.parse(commitDate.substring(0,10), formatter);
        }).findFirst().get();

        LOGGER.info("{} {}", lastCommit, firstCommit);

        return Tuple.of(firstCommit, lastCommit);
    }

    private void upgradeBranch(Repository repository) {
        LOGGER.info("Upgrade git branch");

        shellProcess.execute(new Command2.Builder()
                .add("cd " + repository.getPath())
                .add(configuration.getGit() + " fetch")
                .add(configuration.getGit() + " status")
                .build());

        Try.of(() -> shellProcess.execute(new Command2.Builder()
                .add("cd " + repository.getPath())
                .add(configuration.getGit() + " checkout " + repository.getMain_branch())
                .build()));

        ProcessResult result = shellProcess.execute(new Command2.Builder()
                .add("cd " + repository.getPath())
                .add(configuration.getGit() + " pull")
                .build());

        result.getResults().stream().forEach(LOGGER::debug);
    }

}
