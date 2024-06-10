package ui;

import chess.ChessBoard;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dataaccess.DataAccessException;
import dataaccess.SQLGameDAO;
import request.LoginRequest;
import request.LogoutRequest;
import request.RegisterRequest;
import response.LoginResponse;
import response.LogoutResponse;
import response.RegisterResponse;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

public class ServerFacade {

    private final String serverUrl;
    private static Gson gson;

    public ServerFacade(String url) {
        serverUrl = url;
        gson = new GsonBuilder()
                .registerTypeAdapter(ChessBoard.class, new SQLGameDAO.ChessBoardDeserializer())
                .create();
    }

    public LoginResponse login(String username, String password) throws DataAccessException {
        String path = "/session";
        return this.makeRequest("POST", path, new LoginRequest(username, password), LoginResponse.class, null);
    }

    public RegisterResponse register(String username, String password, String email) throws DataAccessException {
        String path = "/user";
        return this.makeRequest("POST", path, new RegisterRequest(username, password, email), RegisterResponse.class, null);
    }

    public LogoutResponse logout(String authToken) throws DataAccessException {
        String path = "/session";
        return this.makeRequest("DELETE", path, null, LogoutResponse.class, authToken);
    }

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass, String authToken) throws DataAccessException {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);

            if (request != null) {
                writeBody(request, http);
            }
            if (authToken != null) {
                http.addRequestProperty("authorization", authToken);
            }
            http.connect();
            throwIfNotSuccessful(http);


            return readBody(http, responseClass);
        } catch (Exception ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }


    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = gson.toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, DataAccessException {
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
            throw new DataAccessException("failure: " + status);
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = gson.fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }


    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }
}
