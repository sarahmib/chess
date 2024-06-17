package ui;

import chess.ChessGame;
import model.GameData;
import response.ListGamesResponse;
import response.LoginResponse;
import response.RegisterResponse;
import websocket.ServerMessageObserver;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class ChessClient implements ServerMessageObserver {

    private String playerName = null;
    private String authToken = null;
    private Collection<GameData> currentGames;
    private final ServerFacade server;
    private State state = State.SIGNEDOUT;
    private boolean inGame = false;

    public ChessClient(String serverUrl) throws IOException {
        server = new ServerFacade(serverUrl, this);
    }

    @Override
    public void notify(ServerMessage message) {
        switch (message.getServerMessageType()) {
            case NOTIFICATION:
                displayNotification(((NotificationMessage) message).getMessage());
                break;
            case ERROR:
                displayError(((ErrorMessage) message).getErrorMessage());
                break;
            case LOAD_GAME:
//                loadGame(((LoadGameMessage) message).getGame());
                break;
        }
    }

    private void displayNotification(String message) {
        System.out.println("Server message: " + message);
    }

    private void displayError(String errorMessage) {
        System.out.println("Error: " + errorMessage);
    }

//    private void loadGame(ChessGame game) {
//        OutputChessboard.main(game.getBoard().board);
//    }

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
                case "joingame" -> joinGame(params);
                case "observegame" -> observeGame(params);
                case "quit" -> "quit";
                default -> help();
            };
        } catch (IOException ex) {
            return ex.getMessage();
        }
    }

    public String login(String [] params) throws IOException {
        if (params.length < 2) {
            throw new IOException("username or password is missing.");
        }
        String username = params[0];
        String password = params[1];

        LoginResponse response = server.login(username, password);

        state = State.SIGNEDIN;
        playerName = username;
        authToken = response.authToken();

        return String.format("You signed in as %s.", playerName);
    }

    public String register(String [] params) throws IOException {
        if (params.length < 3) {
            throw new IOException("One or more fields are missing.");
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

    public String logout() throws IOException {
        server.logout(authToken);

        state = State.SIGNEDOUT;
        authToken = null;

        return "You have been signed out.";
    }

    public String createGame(String[] params) throws IOException {
        if (params.length < 1) {
            throw new IOException("Please enter a name for the game.");
        }

        String gameName = params[0];

        server.createGame(gameName, authToken);

        return "Created game successfully.";
    }

    public String listGames() throws IOException{
        ListGamesResponse response = server.listGames(authToken);

        currentGames = response.games();

        int gameNum = 1;
        for (GameData data : response.games()) {
            System.out.println(String.format("%d. Game name: %s\nWhite player: %s\nBlack player: %s\n", gameNum, data.gameName(), data.whiteUsername(), data.blackUsername()));
            gameNum++;
        }

        return "End of games list.";
    }

    public String joinGame(String[] params) throws IOException {
        if (params.length < 2) {
            throw new IOException("Please the number of the game you want to join and the color you want to play as.");
        }
        if (currentGames == null) {
            throw new IOException("Please be sure to view the list of available games first!");
        }

        int gameIndex = Integer.parseInt(params[0]) - 1;

        if (gameIndex < 0 || gameIndex >= currentGames.size()) {
            throw new IOException("This game does not exist.");
        }

        String playerColorString = params[1];

        ChessGame.TeamColor playerColor;

        if (Objects.equals(playerColorString, "white")) {
            playerColor = ChessGame.TeamColor.WHITE;
        } else {
            playerColor = ChessGame.TeamColor.BLACK;
        }

        List<GameData> gamesList = (List<GameData>) currentGames;
        server.joinGame(playerColor, gamesList.get(gameIndex).gameID(), playerName, authToken);

        OutputChessboard.main(gamesList.get(gameIndex).game().getBoard().board, playerColor);

        inGame = true;

        return String.format("Successfully joined game %s.", gamesList.get(gameIndex).gameName());
    }

    public String observeGame(String[] params) throws IOException {
        if (params.length < 1) {
            throw new IOException("Please the number of the game you want to join.");
        }
        if (currentGames == null) {
            throw new IOException("Please be sure to view the list of available games first!");
        }

        int gameIndex = Integer.parseInt(params[0]) - 1;

        if (gameIndex < 0 || gameIndex >= currentGames.size()) {
            throw new IOException("This game does not exist.");
        }

        List<GameData> gamesList = (List<GameData>) currentGames;

        OutputChessboard.main(gamesList.get(gameIndex).game().getBoard().board, ChessGame.TeamColor.WHITE);

        return String.format("Now observing game %s.", gamesList.get(gameIndex).gameName());

    }

    public String help() {
        if (state == State.SIGNEDOUT) {
            return """
                    - login <username> <password>
                    - register <username> <password> <email>
                    - quit
                    - help
                    """;
        } else if (inGame) {
            return """
                    - movePiece <starting position> <ending position> (ex. format: B2 B3)
                    - leaveGame
                    - resignGame
                    - displayMoves <piece position> (ex. format: B2)
                    - redrawBoard
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