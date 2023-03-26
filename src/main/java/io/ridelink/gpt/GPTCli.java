package io.ridelink.gpt;

import io.ridelink.gpt.exception.GPTCliException;
import io.ridelink.gpt.exception.GPTMessageException;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;


// https://platform.openai.com/docs/api-reference/completions/create
public final class GPTCli {
    private final String openaiApiKey;
    private final static String COMPLETIONS_ENDPOINT_URL = "https://api.openai.com/v1/chat/completions";
    private final static String MODEL = "gpt-3.5-turbo";
    private final static double TEMPERATURE = 0.7;
    private final static double TOP_PROBABILITY_MASS = 1;
    private final static int N = 1;
    private final static boolean STREAM = false;

    public GPTCli() throws GPTCliException {
        this.openaiApiKey = System.getenv("OPENAI_API_KEY");
        if (this.openaiApiKey == null) {
            throw new GPTCliException("openaiApiKey is not set.");
        }
    }

    public String getCompletion(String question) throws IOException, GPTCliException, GPTMessageException {
        // Get configured connection.
        final HttpURLConnection connection = this.getConnection();
        // Write json body.
        connection.getOutputStream().write(
                this.getRequestBody(question).getBytes(StandardCharsets.UTF_8)
        );
        // Send request & read response.
        JSONObject respBodyJson = new JSONObject(this.getResponseBody(connection));
        return respBodyJson.getJSONArray("choices")
                .getJSONObject(0)
                .getJSONObject("message")
                .getString("content");
    }

    private HttpURLConnection getConnection() throws IOException {
        final URL requestUrl = new URL(GPTCli.COMPLETIONS_ENDPOINT_URL);
        HttpURLConnection connection = (HttpURLConnection) requestUrl.openConnection();
        connection.setConnectTimeout(6000);
        connection.setReadTimeout(12000);
        connection.setInstanceFollowRedirects(false);
        // Set method & headers.
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestProperty("Authorization", String.format("Bearer %s", this.openaiApiKey));
        connection.setDoOutput(true);
        return connection;
    }

    private String getRequestBody(String question) throws GPTMessageException {
        JSONObject jsonObjectBody = new JSONObject();
        jsonObjectBody.put("model", GPTCli.MODEL);
        jsonObjectBody.put("temperature", GPTCli.TEMPERATURE);
        jsonObjectBody.put("n", GPTCli.N);
        jsonObjectBody.put("top_p", GPTCli.TOP_PROBABILITY_MASS);
        jsonObjectBody.put("stream", GPTCli.STREAM);
        // Set messages (it's only 1).
        JSONArray jsonArrayMessages = new JSONArray();
        JSONObject jsonObjectMessage = new JSONObject();
        jsonObjectMessage.put("role", "user");
        // Get prepared message for GPT.
        jsonObjectMessage.put("content", GPTMessage.build(question));
        jsonArrayMessages.put(jsonObjectMessage);
        jsonObjectBody.put("messages", jsonArrayMessages);
        // Convert string to bytes.
        return jsonObjectBody.toString();
    }

    private String getResponseBody(HttpURLConnection connection) throws IOException {
        final BufferedReader incomingBufferReader = new BufferedReader(
                new InputStreamReader(connection.getInputStream())
        );
        final StringBuffer stringBuffer = new StringBuffer();
        String inputLine;
        while ((inputLine = incomingBufferReader.readLine()) != null) {
            stringBuffer.append(inputLine);
        }
        incomingBufferReader.close();
        connection.disconnect();
        return stringBuffer.toString();
    }

}
