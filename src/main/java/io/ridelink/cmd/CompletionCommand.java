package io.ridelink.cmd;

import io.ridelink.gpt.GPTCli;
import io.ridelink.gpt.exception.GPTCliException;
import io.ridelink.gpt.exception.GPTCliParamException;
import io.ridelink.gpt.exception.GPTMessageException;
import picocli.CommandLine;
import picocli.CommandLine.Help.Ansi;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Callable;

@CommandLine.Command
public class CompletionCommand implements Callable<Integer> {

    @CommandLine.Parameters(description = "Your question to ChatGPT")
    private String question;

    @CommandLine.Option(
            names = {"-t", "--temperature"},
            defaultValue = "1",
            description = "What sampling temperature to use, between 0 and 2. Higher values like 0.8 will make the " +
                    "output more random, while lower values like 0.2 will make it more focused and deterministic. " +
                    "Default is ${DEFAULT-VALUE}."
    )
    private Float temperature;

    @Override
    public Integer call() throws Exception {
        // Ensure we have OPENAI_API_KEY set.
        String openaiApiKey = System.getenv("OPENAI_API_KEY");
        if (openaiApiKey == null) {
            this.stdWarn("OPENAI_API_KEY is not set!");
            return CommandLine.ExitCode.USAGE;
        }
        // Init GPT client.
        final GPTCli gptClient;
        try {
            gptClient = new GPTCli(openaiApiKey, this.temperature);
        } catch (GPTCliParamException e) {
            this.stdWarn(e.getMessage());
            return CommandLine.ExitCode.USAGE;
        } catch (GPTCliException e) {
            this.stdErr(e.getMessage());
            return CommandLine.ExitCode.SOFTWARE;
        }
        // Get an answer.
        final String answer;
        try {
            answer = gptClient.getCompletion(this.question);
        } catch (GPTMessageException e ) {
            this.stdWarn(e.getMessage());
            return CommandLine.ExitCode.USAGE;
        } catch (IOException  e ) {
            this.stdErr(e.getMessage());
            return CommandLine.ExitCode.SOFTWARE;
        }

        this.stdInfo(answer);


        return CommandLine.ExitCode.OK;
    }

    private void stdWarn(String x) {
        // TODO: Implement ANSI colors in case of Windows.
        System.out.println(
                Ansi.AUTO.string("@|bold,yellow " + x + "|@")
        );
    }

    private void stdErr(String x) {
        // TODO: Implement ANSI colors in case of Windows.
        System.out.println(
                Ansi.AUTO.string("@|bold,red " + x + "|@")
        );
    }

    private void stdSuccess(String x) {
        // TODO: Implement ANSI colors in case of Windows.
        System.out.println(
                Ansi.AUTO.string("@|bold,green " + x + "|@")
        );
    }

    private void stdInfo(String x) {
        // TODO: Implement ANSI colors in case of Windows.
        System.out.println(
                Ansi.AUTO.string("@|bold,blue " + x + "|@")
        );
    }
}
