package org.jab.thesourceoftruth.service.shell;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class Command {

    private final String command;

    public Command(final String command) {
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

        public Command build(){
            final StringBuilder sb = new StringBuilder();
            sb.append(COMMAND_WIN_PREFIX);
            sb.append(SEPARATOR);
            sb.append(StringUtils.join(this.parts, " && "));
            sb.append(SEPARATOR);
            return new Command(sb.toString());
        }

    }
}
