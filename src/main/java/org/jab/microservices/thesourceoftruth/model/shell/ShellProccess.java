package org.jab.microservices.thesourceoftruth.model.shell;

public interface ShellProccess {

    ShellProcessResult execute(ShellCommand command);
}
