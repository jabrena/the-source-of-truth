package org.jab.thesourceoftruth.config;

import lombok.Data;

import java.util.List;

@Data
public class GlobalConfiguration {
    private List<Repository> repositories;
}
