package io.ridelink.cmd;

import picocli.CommandLine.Help.Ansi;

import java.util.Random;

class Spinner extends Thread {

    private final static String[] FRAMES = {"⢎⡰", "⢎⡡", "⢎⡑", "⢎⠱", "⠎⡱", "⢊⡱", "⢌⡱", "⢆⡱"};
    private final static String[] COLOURS = {"red", "green", "yellow", "blue", "magenta", "white"};
    private final static int FPS = 10;
    private boolean showSpinner = true;

    public void stopSpinner() {
        this.showSpinner = false;
        System.out.print("\r");
    }

    public void run() {
        // TODO: Think of better solution instead of this hack.
        while (true) {
            for (String frame : Spinner.FRAMES) {
                if (!showSpinner) {
                    return;
                }
                Random generator = new Random();
                int randomIndex = generator.nextInt(Spinner.COLOURS.length);
                // System.out.print(Ansi.AUTO.string("@|bold,yellow " + "\r" + frame + "|@"));
                System.out.print(
                        Ansi.AUTO.string(
                                String.format("@|bold,%s \r%s|@", Spinner.COLOURS[randomIndex], frame)
                        )
                );
                try {
                    Thread.sleep(1000 / Spinner.FPS);
                } catch (InterruptedException e) {
                    System.out.print("\r");
                    throw new RuntimeException(e);
                }
            }
        }
    }


}
