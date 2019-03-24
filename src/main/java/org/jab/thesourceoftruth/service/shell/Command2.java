package org.jab.thesourceoftruth.service.shell;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;

public class Command2 {

    private static String OS = System.getProperty("os.name").toLowerCase();

    private final List<String> commandList;

    public Command2(final List<String> commandList) {
        this.commandList = commandList;
    }

    public List<String> get() {
        return this.commandList;
    }

    @Override
    public String toString() {
        return String.join(" ", commandList);
    }

    public static class Builder {

        private List<String> parts;

        private final String COMMAND_WIN_PREFIX = "cmd /c";
        private final String COMMAND_OSX_PREFIX = "/bin/sh";
        private final String COMMAND_OSX_PARAMETER = "-c";
        private final String SEPARATOR = "\"";

        public Builder(){
            parts = new ArrayList<>();
        }

        public Builder add(final String part){
            parts.add(part);
            return this;
        }

        public Command2 build(){
            List<String> finalList =  new ArrayList<>();
            if(OS.contains("win")) {
                finalList.add(COMMAND_WIN_PREFIX);
            } else {
                finalList.add(COMMAND_OSX_PREFIX);
                finalList.add(COMMAND_OSX_PARAMETER);
            }
            finalList.add(StringUtils.join(this.parts, " && "));

            return new Command2(finalList);
        }

    }
}
