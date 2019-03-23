package org.jab.thesourceoftruth.service.git;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.jab.thesourceoftruth.config.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
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

        List<GitDevEffort> list = new ArrayList<>();

        repos.stream().forEach(repository -> {

            //TODO Move to plugin
            LOGGER.info("1. Git Metadata Analysis");

            gitMetatadaAnalysis.run(repository);
        });

        //Starting point for the analysis
        String[] headers = { "Id", "Year", "Month", "Developer", "File", "Added", "Removed" };

        try {

            FileWriter out = new FileWriter("datalake.csv");
            try (CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT
                    .withHeader(headers).withDelimiter(','))) {
                list.forEach(x -> {
                    try {
                        printer.printRecord(
                                x.getIdrepo(),
                                x.getYear(),
                                x.getMonth(),
                                x.getContributor(),
                                x.getFile(),
                                x.getAdded(),
                                x.getRemoved());
                    } catch (IOException ex) {

                    }
                });
            }

        } catch (IOException e) {

        }
    }


}
