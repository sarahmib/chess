package ui;

import dataaccess.DataAccessException;
import model.GameData;
import response.ListGamesResponse;
import response.LoginResponse;
import response.RegisterResponse;

import java.util.Arrays;
import java.util.Collection;

public class ChessClient {

    private String playerName = null;
    private String authToken = null;
    private Collection<GameData> currentGames;
    private final ServerFacade server;
    private State state = State.SIGNEDOUT;

    public ChessClient(String serverUrl) {
        server = new ServerFacade(serverUrl);
    }

    public String eval(String input) {
        try {
            String[] tokens = input.toLowerCase().split(" ");
            String cmd = (tokens.length > 0) ? tokens[0] : "help";
            String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "login" -> login(params);
                case "register" -> register(params);
                case "logout" -> logout();
                case "creategame" -> createGame(params);
                case "listgames" -> listGames();
                // join game
                // observe game
                case "quit" -> "quit";
                default -> help();
            };
        } catch (DataAccessException ex) {
            return ex.getMessage();
        }
    }

    public String login(String [] params) throws DataAccessException {
        if (params.length < 2) {
            throw new DataAccessException("username or password is missing.");
        }
        String username = params[0];
        String password = params[1];

        LoginResponse response = server.login(username, password);

        state = State.SIGNEDIN;
        playerName = username;
        authToken = response.authToken();

        return String.format("You signed in as %s.", playerName);
    }

    public String register(String [] params) throws DataAccessException {
        if (params.length < 3) {
            throw new DataAccessException("One or more fields are missing.");
        }

        String username = params[0];
        String password = params[1];
        String email = params[2];

        RegisterResponse response = server.register(username, password, email);

        state = State.SIGNEDIN;
        playerName = username;
        authToken = response.authToken();

        return String.format("Registered and signed in as %s.", playerName);
    }

    public String logout() throws DataAccessException {
        server.logout(authToken);

        state = State.SIGNEDOUT;
        authToken = null;

        return "You have been signed out.";
    }

    public String createGame(String[] params) throws DataAccessException {
        if (params.length < 1) {
            throw new DataAccessException("Please enter a name for the game.");
        }

        String gameName = params[0];

        server.createGame(gameName, authToken);

        return "Created game successfully.";
    }

    public String listGames() throws DataAccessException {
        ListGamesResponse response = server.listGames(authToken);

        int gameNum = 1;
        for (GameData data : response.games()) {
            System.out.println(String.format("%d. Game name: %s\nWhite player: %s\nBlack player: %s\n", gameNum, data.gameName(), data.whiteUsername(), data.blackUsername()));
        }

        return "End of games list.";
    }

    public String help() {
        if (state == State.SIGNEDOUT) {
            return """
                    - login <username> <password>
                    - register <username> <password> <email>
                    - quit
                    - help
                    """;
        }
        return """
                - createGame <game name>
                - joinGame <game number> <desired color (black/white)>
                - observeGame <game number>
                - listGames
                - logout
                - quit
                """;
    }
}