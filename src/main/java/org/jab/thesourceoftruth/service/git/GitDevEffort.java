package org.jab.thesourceoftruth.service.git;

import lombok.Value;

@Value
public class GitDevEffort {

    String parent;
    String group;
    String idrepo;
    long year;
    long month;
    String contributor;
    long commits;
    long added;
    long removed;
    String file;
    int isTest;
    int isJson;

}
