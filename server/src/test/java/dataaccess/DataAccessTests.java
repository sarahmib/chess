package dataaccess;

import chess.ChessGame;
import dataaccess.Exceptions.DataAccessException;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.*;
import org.mindrot.jbcrypt.BCrypt;
import server.Handler;

import static dataaccess.SQLExecution.configureDatabase;
import static org.junit.jupiter.api.Assertions.*;


public class DataAccessTests {

    private static SQLAuthDAO authDAO;
    private static SQLGameDAO gameDAO;
    private static SQLUserDAO userDAO;

    @BeforeAll
    public static void init() {
        try {
            configureDatabase();
            authDAO = new SQLAuthDAO();
            gameDAO = new SQLGameDAO();
            userDAO = new SQLUserDAO();
        } catch (DataAccessException e) {
            System.out.println("Something went wrong: " + e.getMessage());
            System.exit(1);
        }
    }

    @BeforeEach
    public void setup() {
        try {
            authDAO.clearAuths();
            gameDAO.clearGames();
            userDAO.clearUsers();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    @DisplayName("Auth clear")
    public void testAuthClear() {
        assertDoesNotThrow(() -> authDAO.clearAuths());
    }

    @Test
    @DisplayName("Game clear")
    public void testGameClear() {
        assertDoesNotThrow(() -> gameDAO.clearGames());
    }

    @Test
    @DisplayName("User clear")
    public void testUserClear() {
        assertDoesNotThrow(() -> userDAO.clearUsers());
    }

    @Test
    @DisplayName("Create user success")
    public void testCreateUserSuccess() {
        assertDoesNotThrow(() -> userDAO.createUser("username", "password", "email@email.com"));
    }

    @Test
    @DisplayName("Create user error")
    public void testCreateUserError() {
        assertThrows(DataAccessException.class, () -> {userDAO.createUser(null, "password", "email@email.com");});
    }

    @Test
    @DisplayName("Get user success")
    public void testGetUserSuccess() {
        assertDoesNotThrow(() -> userDAO.createUser("username", "password", "email@email.com"));

        UserData user = assertDoesNotThrow(() -> userDAO.getUser("username"));


        assertNotNull(user, "User should not be null");
        assertEquals("username", user.username(), "Username should match");
        assertTrue(BCrypt.checkpw("password", user.password()), "Password should match");
        assertEquals("email@email.com", user.email(), "Email should match");
    }

    @Test
    @DisplayName("Get nonexistent user")
    public void testGetUserDoesNotExist() {
        UserData user = assertDoesNotThrow(() -> userDAO.getUser("notARegisteredUser"));

        assertNull(user, "User should return as null");
    }

    @Test
    @DisplayName("Find user success")
    public void testFindUserSuccess() {
        assertDoesNotThrow(() -> userDAO.createUser("username", "password", "email@email.com"));
        assertDoesNotThrow(() -> {
            boolean userExists = userDAO.findUser("username", "password");
            assertTrue(userExists, "The user should be found and the method should return true.");
        });
    }

    @Test
    @DisplayName("Find user wrong password")
    public void testFindUserWrongPassword() {
        assertDoesNotThrow(() -> userDAO.createUser("username", "password", "email@email.com"));
        assertDoesNotThrow(() -> {
            boolean userExists = userDAO.findUser("username", "pasword");
            assertFalse(userExists, "The password is incorrect and the method should return false.");
        });
    }

    @Test
    @DisplayName("Create game success")
    public void createGameSuccess() {
        assertDoesNotThrow(() -> {Integer gameId = gameDAO.createGame("newGame");
            assertEquals(1, gameId);});

    }

    @Test
    @DisplayName("Create game invalid input")
    public void createGameInvalidInput() {
        assertThrows(DataAccessException.class, () -> {gameDAO.createGame(null);});
    }

    @Test
    @DisplayName("Get game success")
    public void getGameSuccess() {
        Integer gameId = assertDoesNotThrow(() -> gameDAO.createGame("newGame"));
        GameData game = assertDoesNotThrow(() -> gameDAO.getGame(gameId));

        assertEquals(gameId, game.gameID());
        assertNull(game.whiteUsername());
        assertNull(game.blackUsername());
        assertEquals("newGame", game.gameName());
        ChessGame testGame = new ChessGame();
        assertEquals(testGame, game.game());
    }

    @Test
    @DisplayName("Get nonexistent game")
    public void getGameDoesNotExist() {
        assertDoesNotThrow(() ->gameDAO.createGame("newGame"));
        GameData game = assertDoesNotThrow(() -> gameDAO.getGame(2));

        assertNull(game, "Game should return as null");
    }

    @Test
    @DisplayName("Update game successfully")
    public void updateGameSuccess() {
        assertDoesNotThrow(() -> userDAO.createUser("wUsername", "password", "email@email.com"));
        assertDoesNotThrow(() -> userDAO.createUser("bUsername", "password", "email@email.com"));
        int gameId = assertDoesNotThrow(() ->gameDAO.createGame("newGame"));

        GameData game = assertDoesNotThrow(() -> gameDAO.getGame(gameId));
        GameData updatedGame = new GameData(game.gameID(), "wUsername", "bUsername", game.gameName(), game.game());
        assertDoesNotThrow(() ->gameDAO.updateGame(updatedGame));
        game = assertDoesNotThrow(() -> gameDAO.getGame(gameId));

        assertEquals("wUsername", game.whiteUsername());
        assertEquals("bUsername", game.blackUsername());
    }

    @Test
    @DisplayName("Update game bad input")
    public void updateGameBadInput() {
        int gameId = assertDoesNotThrow(() ->gameDAO.createGame("newGame"));

        GameData game = assertDoesNotThrow(() -> gameDAO.getGame(gameId));
        GameData updatedGame = new GameData(2, "wUsername", "bUsername", game.gameName(), game.game());
        assertDoesNotThrow(() ->gameDAO.updateGame(updatedGame), "Does not throw even with bad input");
    }
}

