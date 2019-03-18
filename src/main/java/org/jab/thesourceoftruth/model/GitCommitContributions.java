package org.jab.thesourceoftruth.model;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jab.thesourceoftruth.service.shell.ProcessResult;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
public class GitCommitContributions {

    private final ProcessResult result;

    private final static Pattern LTRIM = Pattern.compile("^\\s+");

    public static String ltrim(String s) {
        return LTRIM.matcher(s).replaceAll("");
    }

    public List<Contribution> adapt() {

        return result.getResults().stream().map(line -> {
            String[] parts = ltrim(line).split("\t");
            return new Contribution(Long.parseLong(parts[0]), parts[1]);
        }).collect(Collectors.toList());
    }

}