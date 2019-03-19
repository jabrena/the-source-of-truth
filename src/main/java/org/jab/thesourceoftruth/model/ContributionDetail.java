package org.jab.thesourceoftruth.model;

import lombok.Value;

@Value
public class ContributionDetail {

    private final long added;
    private final long removed;
    private final String file;
}
