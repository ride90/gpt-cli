package io.ridelink;

import picocli.CommandLine;

import java.util.List;

@CommandLine.Command
class CompletionCommand implements Runnable {

    @CommandLine.Parameters
    private List<String> questions;

    @CommandLine.Option(names = {"-t", "--temperature"})
    private String temperature;

    @Override
    public void run() {

        System.out.println(this.questions);
        System.out.println(this.temperature);

        System.out.println("CompletionCommand: Hello, Picocli");
    }
}
