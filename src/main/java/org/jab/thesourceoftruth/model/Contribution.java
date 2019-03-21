package org.jab.thesourceoftruth.model;

import lombok.Value;

@Value
public class Contribution {

    private final long commit;
    private final String contributor;

}
