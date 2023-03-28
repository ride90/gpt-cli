package io.ridelink.cmd;

class Spinner extends Thread {

    private final static String[] FRAMES = {"⢎⡰", "⢎⡡", "⢎⡑", "⢎⠱", "⠎⡱", "⢊⡱", "⢌⡱", "⢆⡱"};
    private final static int FPS = 23;
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
                System.out.print("\r" + frame);
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
