package io.ridelink.gpt;

// https://platform.openai.com/docs/api-reference/completions/create
public final class GPTCli {
    // TODO: Make some of these constants configurable via env vars/cmd args.
    private final String OPENAI_API_KEY;
    private final String COMPLETIONS_ENDPOINT_URL = "https://api.openai.com/v1/chat/completions";
    private final String MODEL = "gpt-3.5-turbo";
    private final double TEMPERATURE = 0.7;
    private final double TOP_PROBABILITY_MASS = 1;
    private final int N = 1;
    private final boolean STREAM = false;

    public GPTCli() throws GPTCliException {
        this.OPENAI_API_KEY = System.getenv("OPENAI_API_KEY");
        if (this.OPENAI_API_KEY == null) {
            throw new GPTCliException("OPENAI_API_KEY is not set.");
        }
    }
}
