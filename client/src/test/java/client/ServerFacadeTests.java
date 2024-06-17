//package client;
//
//import chess.ChessGame;
//import model.GameData;
//import org.junit.jupiter.api.*;
//import response.CreateGameResponse;
//import response.ListGamesResponse;
//import response.LoginResponse;
//import response.RegisterResponse;
//import server.Server;
//import ui.ServerFacade;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//
//public class ServerFacadeTests {
//
//    private static Server server;
//    private static ServerFacade serverFacade;
//
//    @BeforeAll
//    public static void init() {
//        server = new Server();
//        var port = server.run(0);
//        System.out.println("Started test HTTP server on " + port);
//
//        serverFacade = new ServerFacade("http://localhost:" + port);
//    }
//
//    @BeforeEach
//    public void clearDatabase() {
//        try {
//            serverFacade.clearDatabase();
//        } catch (IOException e) {
//            System.out.println("Something went wrong: " + e.getMessage());
//        }
//    }
//
//    @AfterAll
//    static void stopServer() {
//        server.stop();
//    }
//
//
//    @Test
//    @DisplayName("test register success")
//    public void testRegisterSuccess() {
//        RegisterResponse response = assertDoesNotThrow(() -> serverFacade.register("username", "password", "email@email.com"));
//        assertEquals("username", response.username());
//        assertEquals(36, response.authToken().length());
//    }
//
//    @Test
//    @DisplayName("test register username taken")
//    public void testRegisterUsernameTaken() {
//        RegisterResponse response = assertDoesNotThrow(() -> serverFacade.register("username", "password", "email@email.com"));
//        assertThrows(IOException.class, () -> serverFacade.register("username", "duplicate", "anotheremail@email.com"));
//    }
//
//    @Test
//    @DisplayName("test login user success")
//    public void testLoginUserSuccess() {
//        assertDoesNotThrow(() -> serverFacade.register("username", "password", "email@email.com"));
//        LoginResponse response = assertDoesNotThrow(() -> serverFacade.login("username", "password"));
//        assertEquals("username", response.username());
//        assertEquals(36, response.authToken().length());
//    }
//
//    @Test
//    @DisplayName("test login user wrong password")
//    public void testLoginUserWrongPassword() {
//        assertDoesNotThrow(() -> serverFacade.register("username", "password", "email@email.com"));
//        assertThrows(IOException.class, () -> serverFacade.login("username", "wrongPassword"));
//    }
//
//    @Test
//    @DisplayName("test logout user success")
//    public void testLogoutUserSuccess() {
//        assertDoesNotThrow(() -> serverFacade.register("username", "password", "email@email.com"));
//        LoginResponse response = assertDoesNotThrow(() -> serverFacade.login("username", "password"));
//        assertDoesNotThrow(() -> serverFacade.logout(response.authToken()));
//        assertThrows(IOException.class, () -> serverFacade.logout(response.authToken()));
//    }
//
//    @Test
//    @DisplayName("test logout user bad auth token")
//    public void testLogoutUserBadAuthToken() {
//        assertDoesNotThrow(() -> serverFacade.register("username", "password", "email@email.com"));
//        assertDoesNotThrow(() -> serverFacade.login("username", "password"));
//        assertThrows(IOException.class, () -> serverFacade.logout(null));
//    }
//
//    @Test
//    @DisplayName("test create game success")
//    public void testCreateGameSuccess() {
//        RegisterResponse registerResponse = assertDoesNotThrow(() -> serverFacade.register("username", "password", "email@email.com"));
//        CreateGameResponse createGameResponse = assertDoesNotThrow(() -> serverFacade.createGame("newGame", registerResponse.authToken()));
//        assertEquals(1, createGameResponse.gameID());
//    }
//
//    @Test
//    @DisplayName("test create game bad input")
//    public void testCreateGameBadInput() {
//        RegisterResponse registerResponse = assertDoesNotThrow(() -> serverFacade.register("username", "password", "email@email.com"));
//        assertThrows(IOException.class, () -> serverFacade.createGame(null, registerResponse.authToken()));
//    }
//
//    @Test
//    @DisplayName("test list games success")
//    public void testListGamesSuccess() {
//        RegisterResponse registerResponse = assertDoesNotThrow(() -> serverFacade.register("username", "password", "email@email.com"));
//        assertDoesNotThrow(() -> serverFacade.createGame("newGame", registerResponse.authToken()));
//
//        ListGamesResponse listGamesResponse = assertDoesNotThrow(() -> serverFacade.listGames(registerResponse.authToken()));
//        Collection<GameData> games = listGamesResponse.games();
//        List<GameData> gameList = new ArrayList<>(games);
//
//        assertEquals(1, games.size());
//
//        GameData testGame = gameList.getFirst();
//
//        assertEquals("newGame", testGame.gameName());
//        assertNull(testGame.blackUsername());
//        assertNull(testGame.whiteUsername());
//        assertEquals(new ChessGame(), testGame.game());
//    }
//
//    @Test
//    @DisplayName("test list games bad auth")
//    public void testListGamesBadAuth() {
//        RegisterResponse registerResponse = assertDoesNotThrow(() -> serverFacade.register("username", "password", "email@email.com"));
//        assertDoesNotThrow(() -> serverFacade.createGame("newGame", registerResponse.authToken()));
//
//        assertThrows(IOException.class, () -> serverFacade.listGames(null));
//    }
//
//    @Test
//    @DisplayName("test join game success")
//    public void testJoinGameSuccess() {
//        RegisterResponse registerResponse = assertDoesNotThrow(() -> serverFacade.register("username", "password", "email@email.com"));
//        CreateGameResponse createGameResponse = assertDoesNotThrow(() -> serverFacade.createGame("newGame", registerResponse.authToken()));
//
//        assertDoesNotThrow(() -> serverFacade.joinGame(ChessGame.TeamColor.WHITE, createGameResponse.gameID(), "username", registerResponse.authToken()));
//
//        ListGamesResponse listGamesResponse = assertDoesNotThrow(() -> serverFacade.listGames(registerResponse.authToken()));
//        Collection<GameData> games = listGamesResponse.games();
//        List<GameData> gameList = new ArrayList<>(games);
//
//        assertEquals("username", gameList.getFirst().whiteUsername());
//    }
//
//    @Test
//    @DisplayName("test join game does not exist")
//    public void testJoinGameDoesNotExist() {
//        RegisterResponse registerResponse = assertDoesNotThrow(() -> serverFacade.register("username", "password", "email@email.com"));
//        assertDoesNotThrow(() -> serverFacade.createGame("newGame", registerResponse.authToken()));
//
//        assertThrows(IOException.class, () -> serverFacade.joinGame(ChessGame.TeamColor.WHITE, 2, "username", registerResponse.authToken()));
//    }
//}
