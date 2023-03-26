package io.ridelink;

import io.ridelink.cmd.CompletionCommand;
import picocli.CommandLine;

class Main {
    public static void main(String[] args) {
        int returnCode = new CommandLine(new CompletionCommand()).execute(args);
        System.exit(returnCode);
    }
}
