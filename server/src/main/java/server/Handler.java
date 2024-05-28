package server;

import dataaccess.*;
import model.AuthData;
import request.RegisterRequest;
import response.RegisterResponse;
import service.AuthService;
import service.GameService;
import service.UserService;
import spark.Request;
import spark.Response;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Handler {

    private final UserService userService;
    private final GameService gameService;
    private final AuthService authService;
    private final Gson gson;

    public Handler() {
        userService = new UserService(new MemoryUserDAO());
        gameService = new GameService(new MemoryGameDAO());
        authService = new AuthService(new MemoryAuthDAO());

        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();

        gson = builder.create();
    }

    public Object register(Request req, Response res) throws DataAccessException {
        RegisterRequest request = gson.fromJson(req.body(), RegisterRequest.class);
        try {
            userService.register(request);
            AuthData response = authService.createAuth(request.username());
            RegisterResponse registerResponse = new RegisterResponse(response.username(), response.authToken(), null);
            res.status(200);
            return toJson(registerResponse);
        } catch (AlreadyTakenException ex) {
            res.status(403);
            RegisterResponse registerResponse = new RegisterResponse(null, null, ex.getMessage());
            return toJson(registerResponse);
        } catch (BadRequestException ex) {
            res.status(400);
            RegisterResponse registerResponse = new RegisterResponse(null, null, ex.getMessage());
            return toJson(registerResponse);
        }
    }

    public Object clearDb(Request req, Response res) throws DataAccessException {
        userService.clearUsers();
        gameService.clearGames();
        authService.clearAuths();
        res.status(200);
        res.type("application/json");
        return "{}";
    }

    private String toJson(Object response) {
        String resultAsJson;
        resultAsJson = gson.toJson(response);
        System.out.println(resultAsJson);
        return resultAsJson;
    }
}
