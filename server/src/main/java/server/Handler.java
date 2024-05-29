package server;

import dataaccess.*;
import model.AuthData;
import request.*;
import response.*;
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
            RegisterResponse registerResponse = authService.register(request.username());
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

    public Object login(Request req, Response res) throws DataAccessException {
        LoginRequest request = gson.fromJson(req.body(), LoginRequest.class);
        try {
            userService.login(request);
            LoginResponse loginResponse = authService.login(request.username());
            res.status(200);
            return toJson(loginResponse);
        } catch (UnauthorizedException ex) {
            res.status(401);
            LoginResponse loginResponse = new LoginResponse(null, null, ex.getMessage());
            return toJson(loginResponse);
        }
    }

    public Object logout(Request req, Response res) throws DataAccessException {
        LogoutRequest request = new LogoutRequest(req.headers("authorization"));
        try {
            LogoutResponse logoutResponse = authService.logout(request);
            res.status(200);
            return toJson(logoutResponse);
        } catch (UnauthorizedException ex) {
            res.status(401);
            LogoutResponse logoutResponse = new LogoutResponse(ex.getMessage());
            return toJson(logoutResponse);
        }
    }

    public Object listGames(Request req, Response res) throws DataAccessException {
        try {
            authService.isAuthorized(req.headers("authorization"));
            ListGamesResponse listGamesResponse = gameService.listGames();
            res.status(200);
            return toJson(listGamesResponse);
        } catch (UnauthorizedException ex) {
            res.status(401);
            ListGamesResponse listGamesResponse = new ListGamesResponse(null, ex.getMessage());
            return toJson(listGamesResponse);
        }
    }
    public Object createGame(Request req, Response res) throws DataAccessException {
        CreateGameRequest request = gson.fromJson(req.body(), CreateGameRequest.class);
        request = request.setAuthToken(req.headers("authorization"));

        try {
            authService.isAuthorized(request.authToken());
            CreateGameResponse createGameResponse = gameService.createGame(request);
            res.status(200);
            return toJson(createGameResponse);
        } catch (BadRequestException ex) {
            res.status(400);
            CreateGameResponse createGameResponse = new CreateGameResponse(null, ex.getMessage());
            return toJson(createGameResponse);
        } catch (UnauthorizedException ex) {
            res.status(401);
            CreateGameResponse createGameResponse = new CreateGameResponse(null, ex.getMessage());
            return toJson(createGameResponse);
        }
    }

    public Object joinGame(Request req, Response res) throws DataAccessException {
        JoinGameRequest request = gson.fromJson(req.body(), JoinGameRequest.class);

        try {
            AuthData authData = authService.isAuthorized(req.headers("authorization"));
            request = request.setUsername(authData.username());
            JoinGameResponse joinGameResponse = gameService.joinGame(request);
            res.status(200);
            return toJson(joinGameResponse);

        } catch (BadRequestException ex) {
            res.status(400);
            JoinGameResponse joinGameResponse = new JoinGameResponse(ex.getMessage());
            return toJson(joinGameResponse);
        } catch (UnauthorizedException ex) {
            res.status(401);
            JoinGameResponse joinGameResponse = new JoinGameResponse(ex.getMessage());
            return toJson(joinGameResponse);
        } catch (AlreadyTakenException ex) {
            res.status(403);
            JoinGameResponse joinGameResponse = new JoinGameResponse(ex.getMessage());
            return toJson(joinGameResponse);
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
