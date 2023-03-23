package io.ridelink.gpt;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;


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

    public String getCompletion(String question) throws IOException, GPTCliException {
        // Create connection object.
        URL requestUrl = new URL(this.COMPLETIONS_ENDPOINT_URL);
        HttpURLConnection connection = (HttpURLConnection) requestUrl.openConnection();
        // Set method & headers.
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestProperty("Authorization", String.format("Bearer %s", this.OPENAI_API_KEY));
        connection.setDoOutput(true);

        System.out.println(this.getRequestBody(question));


        return "";
    }

    private String getRequestBody(String question) throws GPTCliException {
        JSONObject jsonObjectBody = new JSONObject();
        jsonObjectBody.put("model", this.MODEL);
        jsonObjectBody.put("temperature", this.TEMPERATURE);
        jsonObjectBody.put("n", this.N);
        jsonObjectBody.put("top_p", this.TOP_PROBABILITY_MASS);
        jsonObjectBody.put("stream", this.STREAM);
        JSONArray jsonArrayMessages = new JSONArray();
        JSONObject jsonObjectMessage = new JSONObject();
        jsonObjectMessage.put("role", "user");
        try {
            jsonObjectMessage.put("content", GPTMessage.build(question));
        } catch (GPTMessageException e) {
            throw new GPTCliException(e.getMessage());
        }
        jsonArrayMessages.put(jsonObjectMessage);
        jsonObjectBody.put("messages", jsonArrayMessages);
        return jsonObjectBody.toString();
    }

}
