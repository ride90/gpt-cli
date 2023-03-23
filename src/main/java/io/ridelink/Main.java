package io.ridelink;

import io.ridelink.gpt.GPTCli;
import io.ridelink.gpt.GPTCliException;

class Main {
    public static void main(String[] args) {
        final GPTCli gptClient;
        try {
            gptClient = new GPTCli();
        } catch (GPTCliException e) {
            System.out.println("ALL bad " + e);
            return;
        }
        System.out.println("All good" + gptClient);
    }
}
