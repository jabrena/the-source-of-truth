package org.jab.microservices.thesourceoftruth.model.shell;

import lombok.Value;

import java.util.List;

@Value
public class ShellProcessResult {

    private List<String> results;
}
