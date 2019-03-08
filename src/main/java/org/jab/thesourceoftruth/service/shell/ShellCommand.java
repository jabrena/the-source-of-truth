package org.jab.thesourceoftruth.service.shell;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class ShellCommand {

    private final String command;

    public ShellCommand(final String command) {
        this.command = command;
    }

    @Override
    public String toString() {
        return command;
    }

    public static class Builder {

        private List<String> parts;

        private final String COMMAND_WIN_PREFIX = "cmd /c ";
        private final String SEPARATOR = "\"";

        public Builder(){
            parts = new ArrayList<>();
        }

        public Builder add(final String part){
            this.parts.add(part);
            return this;
        }

        public ShellCommand build(){
            final StringBuilder sb = new StringBuilder();
            sb.append(COMMAND_WIN_PREFIX);
            sb.append(SEPARATOR);
            sb.append(StringUtils.join(this.parts, " && "));
            sb.append(SEPARATOR);
            return new ShellCommand(sb.toString());
        }

    }
}
