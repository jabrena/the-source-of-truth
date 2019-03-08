package org.jab.thesourceoftruth.config;

public enum BuildTool {

    MAVEN ("MAVEN"),
    GRADLE ("GRADLE");

    private String value;

    BuildTool(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
