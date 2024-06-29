package ui;

import chess.ChessGame;
import model.GameData;
import response.ListGamesResponse;
import response.LoginResponse;
import response.RegisterResponse;
import websocket.ServerMessageObserver;
import websocket.WebSocketCommunicator;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.*;

public class ChessClient implements ServerMessageObserver {

    private String playerName = null;
    private String authToken = null;
    private Collection<GameData> currentGames;
    private final ServerFacade server;
    private final WebSocketCommunicator wsCommunicator;
    private State state = State.SIGNEDOUT;
    private boolean inGame = false;
    private Scanner scanner;

    public ChessClient(String serverUrl, Scanner scanner) throws IOException {
        server = new ServerFacade(serverUrl);
        wsCommunicator = new WebSocketCommunicator(serverUrl, this);
        this.scanner = scanner;
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
                loadGame(((LoadGameMessage) message).getGame());
                break;
        }
    }

    private void displayNotification(String message) {
        System.out.println("Server message: " + message);
    }

    private void displayError(String errorMessage) {
        System.out.println("Error: " + errorMessage);
    }

    private void loadGame(ChessGame game) {
        if (Objects.equals(playerName, game.getBlackUsername())) {
            OutputChessboard.main(game.getBoard().board, ChessGame.TeamColor.BLACK);
        } else {
            OutputChessboard.main(game.getBoard().board, ChessGame.TeamColor.WHITE);
        }
    }

    public String eval(String input) {
        try {
            String[] tokens = input.toLowerCase().split(" ");
            String cmd = (tokens.length > 0) ? tokens[0] : "help";
            String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "login" -> login();
                case "register" -> register();
                case "logout" -> logout();
                case "creategame" -> createGame();
                case "listgames" -> listGames();
                case "joingame" -> joinGame();
                case "observegame" -> observeGame();
                case "quit" -> "quit";
                default -> help();
            };
        } catch (IOException ex) {
            return ex.getMessage();
        }
    }

    public String login() throws IOException {
        System.out.println("Username: ");
        String username = scanner.nextLine();

        System.out.println("Password: ");
        String password = scanner.nextLine();

        LoginResponse response = server.login(username, password);

        state = State.SIGNEDIN;
        playerName = username;
        authToken = response.authToken();

        return String.format("You signed in as %s.", playerName);
    }

    public String register() throws IOException {
        System.out.println("Username: ");
        String username = scanner.nextLine();

        System.out.println("Password: ");
        String password = scanner.nextLine();

        System.out.println("Email: ");
        String email = scanner.nextLine();

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

    public String createGame() throws IOException {
        System.out.println("Enter a name for your game:");
        String gameName = scanner.nextLine();

        server.createGame(gameName, authToken);

        return "Created game successfully.";
    }

    public String listGames() throws IOException{
        ListGamesResponse response = server.listGames(authToken);

        currentGames = response.games();

        int gameNum = 1;
        for (GameData data : response.games()) {
            System.out.printf("%d. Game name: %s\nWhite player: %s\nBlack player: %s\n%n", gameNum, data.gameName(), data.whiteUsername(), data.blackUsername());
            gameNum++;
        }

        return "End of games list.";
    }

    private void listGamesNoPrinting() throws IOException{
        ListGamesResponse response = server.listGames(authToken);
        currentGames = response.games();
    }

    public String joinGame() throws IOException {
        System.out.println("Enter the number of the game you want to join:");
        int gameIndex = Integer.parseInt(scanner.nextLine()) - 1;

        System.out.println("Enter the color you want to play as (black/white");
        String playerColorString = scanner.nextLine();

        if (currentGames == null) {
            listGamesNoPrinting();
        }

        if (gameIndex < 0 || gameIndex >= currentGames.size()) {
            throw new IOException("This game does not exist.");
        }

        ChessGame.TeamColor playerColor;

        if (Objects.equals(playerColorString, "white")) {
            playerColor = ChessGame.TeamColor.WHITE;
        } else {
            playerColor = ChessGame.TeamColor.BLACK;
        }

        List<GameData> gamesList = (List<GameData>) currentGames;
        server.joinGame(playerColor, gamesList.get(gameIndex).gameID(), playerName, authToken);
        inGame = true;

        wsCommunicator.joinGame(authToken, gamesList.get(gameIndex).gameID());

        return String.format("Successfully joined game %s.", gamesList.get(gameIndex).gameName());
    }

    public String observeGame() throws IOException {

        System.out.println("Enter the number of the game you want to join:");
        int gameIndex = Integer.parseInt(scanner.nextLine()) - 1;

        if (currentGames == null) {
            listGamesNoPrinting();
        }

        if (gameIndex < 0 || gameIndex >= currentGames.size()) {
            throw new IOException("This game does not exist.");
        }

        List<GameData> gamesList = (List<GameData>) currentGames;

        // server.observeGame(authToken, gamesList.get(gameIndex).gameID());

        return String.format("Now observing game %s.", gamesList.get(gameIndex).gameName());

    }

    public String help() {
        if (state == State.SIGNEDOUT) {
            return """
                    - login
                    - register
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
        } else {
            return """
                    - createGame
                    - joinGame
                    - observeGame
                    - listGames
                    - logout
                    - quit
                    """;
        }
    }
}