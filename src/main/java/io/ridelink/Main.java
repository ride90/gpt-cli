package io.ridelink;

import io.ridelink.gpt.GPTCli;
import io.ridelink.gpt.GPTCliException;

import java.io.IOException;

class Main {
    public static void main(String[] args) {

        final GPTCli gptClient;
        try {
            gptClient = new GPTCli();
        } catch (GPTCliException e) {
            System.out.println("ALL bad " + e);
            return;
        }

        try {
            gptClient.getCompletion("How to see all python processes?");
        } catch (IOException | GPTCliException e) {
            System.out.println(e);
        }
    }
}
