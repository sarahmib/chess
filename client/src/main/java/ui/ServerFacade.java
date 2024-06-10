package ui;

import chess.ChessBoard;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dataaccess.DataAccessException;
import dataaccess.SQLGameDAO;
import request.CreateGameRequest;
import request.LoginRequest;
import request.RegisterRequest;
import response.*;

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

    public CreateGameResponse createGame(String gameName, String authToken) throws DataAccessException {
        String path = "/game";
        return this.makeRequest("POST", path, new CreateGameRequest(gameName, null), CreateGameResponse.class, authToken);
    }

    public ListGamesResponse listGames(String authToken) throws DataAccessException {
        String path = "/game";
        return this.makeRequest("GET", path, null, ListGamesResponse.class, authToken);
    }

    public void clearDatabase() throws DataAccessException {
        String path = "/db";
        this.makeRequest("DELETE", path, null, null, null);
    }

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass, String authToken) throws DataAccessException {
        HttpURLConnection http = null;
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);

            if (authToken != null) {
                http.addRequestProperty("authorization", authToken);
            }

            if (request != null) {
                writeBody(request, http);
            }

            throwIfNotSuccessful(http);

            return readBody(http, responseClass);
        } catch (Exception ex) {
            throw new DataAccessException(ex.getMessage());
        } finally {
            if (http != null) {
                http.disconnect();
            }
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
