package io.ridelink;

import io.ridelink.gpt.GPTCli;
import io.ridelink.gpt.exception.GPTCliException;

import java.io.IOException;

class Main {
    public static void main(String[] args) {

        // new CommandLine(
        //         new CompletionCommand()
        // ).execute(args);

        final GPTCli gptClient;

        try {
            gptClient = new GPTCli();
        } catch (GPTCliException e) {
            System.out.println("ALL bad " + e.getMessage());
            return;
        }

        final String answer;
        try {
            answer = gptClient.getCompletion("How to see all java processes?");
            System.out.println(answer);
        } catch (IOException | GPTCliException e) {
        // } catch (Error e) {
            System.out.println(e.getMessage());
        }

    }
}
