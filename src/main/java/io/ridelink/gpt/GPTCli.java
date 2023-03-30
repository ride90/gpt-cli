package io.ridelink.gpt;

import io.ridelink.gpt.exception.GPTCliParamException;
import io.ridelink.gpt.exception.GPTMessageException;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;


public final class GPTCli {

    private final String openaiApiKey;
    private final static float TEMPERATURE_DEFAULT = 1F;
    private final static String COMPLETIONS_ENDPOINT_URL = "https://api.openai.com/v1/chat/completions";
    private final static String MODEL_DEFAULT = "gpt-3.5-turbo";
    private final static String[] MODELS_AVAILABLE = {
            GPTCli.MODEL_DEFAULT, "gpt-4", "gpt-4-0314", "gpt-4-32k", "gpt-4-32k-0314", "gpt-3.5-turbo-0301"
    };
    private final static float TOP_PROBABILITY_MASS = 1F;
    private final static int N = 1;
    private final static boolean STREAM = false;

    public GPTCli(String openaiApiKey) {
        this.openaiApiKey = openaiApiKey;
    }

    public String getCompletion(String question, Float temperature, boolean isGeneral, String model) throws IOException,
            GPTMessageException {
        // Validate args.
        GPTCli.validateQuestion(question);
        GPTCli.validateTemperature(temperature);
        GPTCli.validateModel(model);
        // Get configured connection.
        final HttpURLConnection connection = this.getConnection();
        // Write json body.
        connection.getOutputStream().write(
                this.getRequestBody(question, temperature, isGeneral).getBytes(StandardCharsets.UTF_8)
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
        connection.setReadTimeout(60000);
        connection.setInstanceFollowRedirects(false);
        // Set method & headers.
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestProperty("Authorization", String.format("Bearer %s", this.openaiApiKey));
        connection.setDoOutput(true);
        return connection;
    }

    private String getRequestBody(String question, Float temperature, boolean isGeneral) throws GPTMessageException {
        JSONObject jsonObjectBody = new JSONObject();
        jsonObjectBody.put("model", GPTCli.MODEL_DEFAULT);
        jsonObjectBody.put("temperature", temperature);
        jsonObjectBody.put("n", GPTCli.N);
        jsonObjectBody.put("top_p", GPTCli.TOP_PROBABILITY_MASS);
        jsonObjectBody.put("stream", GPTCli.STREAM);
        // Set messages (it's only 1).
        JSONArray jsonArrayMessages = new JSONArray();
        JSONObject jsonObjectMessage = new JSONObject();
        jsonObjectMessage.put("role", "user");
        // Get prepared message for GPT and set it to the body.
        String preparedQuestion = isGeneral ? GPTMessage.prepareGeneral(question) :
                GPTMessage.prepareExecutable(question);
        jsonObjectMessage.put("content", preparedQuestion);
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

    private static void validateQuestion(String question) throws GPTCliParamException {
        if (question == null || question.isBlank()) {
            throw new GPTCliParamException("Question must be not empty!");
        }
    }

    private static void validateTemperature(Float temperature) throws GPTCliParamException {
        temperature = temperature != null ? temperature : GPTCli.TEMPERATURE_DEFAULT;
        if (temperature < 0 || temperature > 2) {
            throw new GPTCliParamException(
                    String.format(
                            "Temperature with value '%s' is not allowed. Allowed value is between 0 and 2.",
                            temperature
                    )
            );
        }
    }

    private static void validateModel(String model) throws GPTCliParamException {
        if (model == null || model.isBlank()) {
            throw new GPTCliParamException("Model must be not empty!");
        } else if (!Arrays.asList(GPTCli.MODELS_AVAILABLE).contains(model)) {
            throw new GPTCliParamException(
                    String.format(
                            "'%s' is not a valid model. Available models: %s",
                            model, Arrays.toString(GPTCli.MODELS_AVAILABLE)
                    )
            );
        }
    }
}
