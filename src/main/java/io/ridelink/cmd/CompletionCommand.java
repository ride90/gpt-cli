package io.ridelink.cmd;

import io.ridelink.gpt.GPTCli;
import io.ridelink.gpt.exception.GPTCliParamException;
import io.ridelink.gpt.exception.GPTMessageException;
import picocli.CommandLine;

import java.io.IOException;
import java.util.concurrent.Callable;

@CommandLine.Command
public class CompletionCommand extends BaseCommand implements Callable<Integer> {

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

    @CommandLine.Option(
            names = {"-g", "--general"},
            defaultValue = "false",
            description = "Used in case you want to mark your question as general. It means you can ask anything. " +
                    "Default is ${DEFAULT-VALUE}."
    )
    private boolean isGeneral;

    @Override
    public Integer call() throws Exception {
        // Ensure we have OPENAI_API_KEY env var set.
        String openaiApiKey = this.getEnv("OPENAI_API_KEY");
        if (openaiApiKey == null) {
            this.stdWarn("OPENAI_API_KEY is not set!");
            return CommandLine.ExitCode.USAGE;
        }
        // Init GPT client.
        final GPTCli gptClient = new GPTCli(openaiApiKey);
        // Hit API and get a reply.
        final String reply;
        Spinner spinner = new Spinner();
        spinner.start();
        try {
            reply = gptClient.getCompletion(this.question, this.temperature, this.isGeneral);
        } catch (GPTCliParamException | GPTMessageException e) {
            this.stdWarn(e.getMessage());
            return CommandLine.ExitCode.USAGE;
        } catch (IOException e) {
            this.stdErr(e.getMessage());
            return CommandLine.ExitCode.SOFTWARE;
        } finally {
            spinner.stopSpinner();
        }
        // Interactive dialog.
        if (!this.isGeneral) {
            this.stdMagenta("Command suggestion:\n");
            this.stdInfo(reply);
        } else {
            this.stdMagenta("Reply:\n");
            this.stdInfo(reply);
        }
        return CommandLine.ExitCode.OK;
    }
}
