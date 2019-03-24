package org.jab.thesourceoftruth.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jab.thesourceoftruth.service.shell.ProcessResult;

@Slf4j
@AllArgsConstructor
public class GitCommitContributionDetail {

    private final ProcessResult result;

    public List<ContributionDetail> adapt() {

        if(result.getResults().get(0).equals("")) {
            return new ArrayList<>();
        }

        return result.getResults().stream().map(line -> {
            String[] parts = line.split("\t");
            long added = (parts[0].equals("-")) ? 0 : Long.parseLong(parts[0]);
            long removed = (parts[1].equals("-")) ? 0 : Long.parseLong(parts[1]);
            return new ContributionDetail(added, removed, parts[2]);
        }).collect(Collectors.toList());
    }

}