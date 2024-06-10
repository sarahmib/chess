package ui;

import dataaccess.DataAccessException;
import response.LoginResponse;

import java.util.Arrays;

public class ChessClient {

    private String visitorName = null;
    private String authToken = null;
    private final ServerFacade server;
    private final String serverUrl;
    private State state = State.SIGNEDOUT;

    public ChessClient(String serverUrl) {
        server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
    }

    public String eval(String input) {
        try {
            String[] tokens = input.toLowerCase().split(" ");
            String cmd = (tokens.length > 0) ? tokens[0] : "help";
            String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "login" -> login(params);
                case "quit" -> "quit";
                default -> help();
            };
        } catch (DataAccessException ex) {
            return ex.getMessage();
        }
    }

    public String login(String [] params) throws DataAccessException {
        if (params.length < 2) {
            throw new DataAccessException("error: username or password is missing");
        }
        String username = params[0];
        String password = params[1];

        LoginResponse response = server.login(username, password);

        state = State.SIGNEDIN;
        visitorName = username;
        authToken = response.authToken();



        return String.format("You signed in as %s.", visitorName);

    }

    public String help() {
        if (state == State.SIGNEDOUT) {
            return """
                    - login <username> <password>
                    - register
                    - quit
                    """;
        }
        return """
                - createGame
                - joinGame
                - listGames
                - logout
                - quit
                """;
    }
}
