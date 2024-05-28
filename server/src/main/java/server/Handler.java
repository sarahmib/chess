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

    public Response register(Request req, Response res) throws DataAccessException {
        RegisterRequest request = gson.fromJson(req.body(), RegisterRequest.class);
        try {
            userService.register(request);
            AuthData response = authService.createAuth(request.username());
            RegisterResponse registerResponse = new RegisterResponse(response.username(), response.authToken());
            res.status(200);
            res.body(toJson(registerResponse));
        } catch (AlreadyTakenException ex) {
            res.status(403);
            res.body(toJson(ex));
        } catch (BadRequestException ex) {
            res.status(400);
            res.body(toJson(ex));
        }
        return res;
    }

    public Object clearDb(Request req, Response res) throws DataAccessException {
        userService.clearUsers();
        gameService.clearGames();
        authService.clearAuths();
        res.status(200);
        res.body(toJson(""));
        return toJson("");
    }

    private String toJson(Object response) {
        String resultAsJson;
        resultAsJson = gson.toJson(response);
        return resultAsJson;
    }
}
