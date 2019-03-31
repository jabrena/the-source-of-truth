package org.jab.thesourceoftruth.service.git;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.jab.thesourceoftruth.config.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

        List<GitDevEffort> list = new ArrayList<>();

        repos.stream().forEach(repository -> {

            //TODO Move to plugin
            LOGGER.info("1. Git Metadata Analysis");

            list.addAll(gitMetatadaAnalysis.run(repository));
        });

        //Starting point for the analysis
        String[] headers = { "Parent", "Group", "Id", "Year", "Month", "Developer", "File", "Added", "Removed", "IsTest", "IsJSON" };

        try {

            FileWriter out = new FileWriter("datalake.csv");
            try (CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT
                    .withHeader(headers).withDelimiter(','))) {
                list.forEach(row -> {
                    try {
                        printer.printRecord(
                                row.getParent(),
                                row.getGroup(),
                                row.getIdrepo(),
                                row.getYear(),
                                row.getMonth(),
                                row.getContributor(),
                                row.getFile(),
                                row.getAdded(),
                                row.getRemoved(),
                                row.getIsTest(),
                                row.getIsJson());
                    } catch (IOException ex) {

                    }
                });
            }

        } catch (IOException e) {

        }
    }


}
