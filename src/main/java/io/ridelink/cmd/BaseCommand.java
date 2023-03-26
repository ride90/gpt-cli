package io.ridelink.cmd;

import picocli.CommandLine.Help.Ansi;

class BaseCommand {

    protected void stdWarn(String x) {
        // TODO: Implement ANSI colors in case of Windows.
        System.out.println(
                Ansi.AUTO.string("@|bold,yellow " + x + "|@")
        );
    }

    protected void stdErr(String x) {
        // TODO: Implement ANSI colors in case of Windows.
        System.out.println(
                Ansi.AUTO.string("@|bold,red " + x + "|@")
        );
    }

    protected void stdSuccess(String x) {
        // TODO: Implement ANSI colors in case of Windows.
        System.out.println(
                Ansi.AUTO.string("@|bold,green " + x + "|@")
        );
    }

    protected void stdInfo(String x) {
        // TODO: Implement ANSI colors in case of Windows.
        System.out.println(
                Ansi.AUTO.string("@|bold,blue " + x + "|@")
        );
    }
}