package org.jab.thesourceoftruth.service.git;

import com.jakewharton.fliptables.FlipTableConverters;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.control.Try;
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
import java.util.List;

@Slf4j
@Service
public class GitMetatadaAnalysis {

    @Autowired
    private GlobalConfiguration configuration;

    @Autowired
    private Proccess shellProcess;

    public void run(final Repository repository) {

        //1. Upgrade git branch
        upgradeBranch(repository);

        //2. Get commit ranges (First & Last)
        Tuple2<LocalDate, LocalDate> commitRanges = getFirstAndLastDate(repository);

        //3. Get contributions
        getRepoContribution(repository, commitRanges);
    }


    private void getRepoContribution(Repository repository, Tuple2<LocalDate, LocalDate> commitRanges) {

        LOGGER.info("Get Git Dev Effort metadata");

        List<GitDevEffort> list = new ArrayList<>();

        for(int year = commitRanges._1.getYear(); year < commitRanges._2.getYear(); year++) {

            LOGGER.info("Analyzing git metadata in year: {}", year);

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
        //list.stream().map(row -> row.toString()).forEach(LOGGER::info);

        String[] headers = { "Id", "Year", "Month", "Developer", "File", "Added", "Removed" };
        Object[][] data2 = new Object[list.size()][];

        int counter = 0;
        for(GitDevEffort item : list) {
            data2[counter] = new Object[7];
            data2[counter][0] = item.getIdrepo();
            data2[counter][1] = item.getYear();
            data2[counter][2] = item.getMonth();
            data2[counter][3] = item.getContributor();
            data2[counter][4] = item.getFile();
            data2[counter][5] = item.getAdded();
            data2[counter][6] = item.getRemoved();

            counter++;
        }
        System.out.println(FlipTableConverters.fromObjects(headers, data2));

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

        shellProcess.execute(new Command.Builder()
                .add("cd " + repository.getPath())
                .add(configuration.getGit() + " fetch")
                .add(configuration.getGit() + " status")
                .build());

        Try.of(() -> shellProcess.execute(new Command.Builder()
                .add("cd " + repository.getPath())
                .add(configuration.getGit() + " checkout " + repository.getMain_branch())
                .build()));

        ProcessResult result = shellProcess.execute(new Command.Builder()
                .add("cd " + repository.getPath())
                .add(configuration.getGit() + " pull")
                .build());

        result.getResults().stream().forEach(LOGGER::debug);
    }

}
