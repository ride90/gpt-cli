package io.ridelink.gpt;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;


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
        // Get configured connection.
        final HttpURLConnection connection = this.getConnection();
        // Write json body.
        connection.getOutputStream().write(this.getRequestBody(question));
        // Send request & read response.
        String respBody = this.getResponseBody(connection);

        System.out.println("****************************");
        System.out.println(respBody);


        return "";
    }

    private HttpURLConnection getConnection() throws IOException {
        final URL requestUrl = new URL(this.COMPLETIONS_ENDPOINT_URL);
        HttpURLConnection connection = (HttpURLConnection) requestUrl.openConnection();
        connection.setConnectTimeout(6000);
        connection.setReadTimeout(12000);
        connection.setInstanceFollowRedirects(false);
        // Set method & headers.
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestProperty("Authorization", String.format("Bearer %s", this.OPENAI_API_KEY));
        connection.setDoOutput(true);
        return connection;
    }

    private byte[] getRequestBody(String question) throws GPTCliException {
        JSONObject jsonObjectBody = new JSONObject();
        jsonObjectBody.put("model", this.MODEL);
        jsonObjectBody.put("temperature", this.TEMPERATURE);
        jsonObjectBody.put("n", this.N);
        jsonObjectBody.put("top_p", this.TOP_PROBABILITY_MASS);
        jsonObjectBody.put("stream", this.STREAM);
        // Set messages (it's only 1).
        JSONArray jsonArrayMessages = new JSONArray();
        JSONObject jsonObjectMessage = new JSONObject();
        jsonObjectMessage.put("role", "user");
        // Get prepared message for GPT.
        try {
            jsonObjectMessage.put("content", GPTMessage.build(question));
        } catch (GPTMessageException e) {
            throw new GPTCliException(e.getMessage());
        }
        jsonArrayMessages.put(jsonObjectMessage);
        jsonObjectBody.put("messages", jsonArrayMessages);
        // Convert string to bytes.
        return jsonObjectBody.toString().getBytes(StandardCharsets.UTF_8);
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
