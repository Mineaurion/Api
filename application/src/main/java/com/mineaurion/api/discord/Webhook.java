package com.mineaurion.api.discord;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;

/**
 * @see <a href="https://gist.github.com/k3kdude/fba6f6b37594eae3d6f9475330733bdb">Gist Webhook</a>
 */
// TODO: remove class and use WebClient
public class Webhook {

    private final String url;
    private String content;

    public Webhook(String url) {
        this.url = url;
    }

    public Webhook setContent(String content) {
        this.content = content;
        return this;
    }

    public void execute() throws IOException {
        if (this.content == null) {
            throw new IllegalArgumentException("Set content");
        }

        URL url = new URL(this.url);
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.addRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");

        OutputStream stream = connection.getOutputStream();
        stream.write(String.format("{\"content\": \"%s\"}", content).getBytes());
        stream.flush();
        stream.close();

        connection.getInputStream().close();
        connection.disconnect();
    }
}
