package server;

import dataaccess.AlreadyTakenException;
import dataaccess.BadRequestException;
import dataaccess.DataAccessException;
import dataaccess.UnauthorizedException;
import dataaccess.SQLAuthDAO;
import dataaccess.SQLGameDAO;
import dataaccess.SQLUserDAO;
import model.AuthData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import request.*;
import response.*;
import serialization.GsonConfigurator;
import server.websocket.ConnectionManager;
import service.AuthService;
import service.GameService;
import service.UserService;
import spark.Request;
import spark.Response;
import com.google.gson.Gson;
import websocket.commands.*;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;

import java.io.IOException;

import static dataaccess.SQLExecution.configureDatabase;

@WebSocket
public class Handler {

    private final UserService userService;
    private final GameService gameService;
    private final AuthService authService;
    private final Gson gson = GsonConfigurator.makeSerializerDeserializer();
    private final ConnectionManager connections = new ConnectionManager();

    public Handler() throws DataAccessException {
        configureDatabase();

        authService = new AuthService(new SQLAuthDAO());
        userService = new UserService(new SQLUserDAO(), authService);
        gameService = new GameService(new SQLGameDAO(), authService);

    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        try {
            UserGameCommand command = gson.fromJson(message, UserGameCommand.class);

            String username = getUsername(command.getAuthString());

            saveSession(command.getGameID(), session);

            switch (command.getCommandType()) {
                case CONNECT -> connect(session, username, (ConnectCommand) command);
                case MAKE_MOVE -> makeMove(session, username, (MakeMoveCommand) command);
                case LEAVE -> leaveGame(session, username, (LeaveCommand) command);
                case RESIGN -> resign(session, username, (ResignCommand) command);
            }
        } catch (Exception ex) {
            ErrorMessage errorMessage = new ErrorMessage(ex.getMessage());
            session.getRemote().sendString(gson.toJson(errorMessage));
        }
    }

    private String getUsername(String authToken) throws DataAccessException {
        AuthData data = authService.isAuthorized(authToken);
        return data.username();
    }

    private Integer getGameID() {
        return null;
    }

    private void saveSession(Integer gameID, Session session) {
        connections.add(gameID, session);
    }

    private void connect(Session session, String username, ConnectCommand command) throws IOException, DataAccessException {
        LoadGameMessage loadGameMessage = new LoadGameMessage(gameService.getGame(command.getGameID()));
        if (loadGameMessage.getGame() == null) {
            ErrorMessage errorMessage = new ErrorMessage("This game ID does not exist.");
            session.getRemote().sendString(gson.toJson(errorMessage));
        } else if (authService.isNotAuthorizedWs(command.getAuthString())) {
            ErrorMessage errorMessage = new ErrorMessage("You are not authorized.");
            session.getRemote().sendString(gson.toJson(errorMessage));
        } else {
            NotificationMessage message = new NotificationMessage(String.format("%s has joined the game.", username));
            connections.broadcast(command.getGameID(), session, message);

            session.getRemote().sendString(gson.toJson(loadGameMessage));
        }
    }

    private void leaveGame(Session session, String username, LeaveCommand command) {}

    private void resign(Session session, String username, ResignCommand command) {}

    private void makeMove(Session session, String username, MakeMoveCommand command) {}


    public Object register(Request req, Response res) throws DataAccessException {
        RegisterRequest request = gson.fromJson(req.body(), RegisterRequest.class);
        try {
            RegisterResponse registerResponse = userService.register(request);;
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
            LoginResponse loginResponse = userService.login(request);
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
            ListGamesResponse listGamesResponse = gameService.listGames(req.headers("authorization"));
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
            JoinGameResponse joinGameResponse = gameService.joinGame(request, req.headers("authorization"));
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
        return resultAsJson;
    }

}
