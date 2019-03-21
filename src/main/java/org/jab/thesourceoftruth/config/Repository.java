package org.jab.thesourceoftruth.config;

import lombok.Data;

@Data
public class Repository {

    private String  id;
    private String path;
    private String address;
    private Boolean authentication;
    private String user;
    private String  password;
    private BuildTool build_tool;
    private String main_branch;
}
