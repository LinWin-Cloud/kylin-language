package api;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpRequests {
    public String GET_Requests(String url) throws Exception {
        URL u = new URL(url);
        HttpURLConnection httpURLConnection = (HttpURLConnection) u.openConnection();
        httpURLConnection.setRequestMethod("GET");
        httpURLConnection.connect();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
        StringBuilder stringBuilder = new StringBuilder();
        while (true) {
            String line = bufferedReader.readLine();
            if (line == null) {
                break;
            }
            stringBuilder.append(line);
        }
        return stringBuilder.toString();
    }
}
