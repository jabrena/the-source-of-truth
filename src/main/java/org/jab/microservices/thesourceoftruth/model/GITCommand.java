package org.jab.microservices.thesourceoftruth.model;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class GITCommand {

    private final String command;

    public GITCommand(final String command) {
        this.command = command;
    }

    @Override
    public String toString() {
        return command;
    }

    public static class Builder {

        private List<String> parts;

        public Builder(){
            parts = new ArrayList<>();
        }

        public Builder add(final String part){
            this.parts.add(part);
            return this;
        }

        public GITCommand build(){
            return new GITCommand(StringUtils.join(this.parts, " && "));
        }

    }
}
