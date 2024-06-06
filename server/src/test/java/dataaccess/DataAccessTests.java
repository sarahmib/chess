package dataaccess;

import dataaccess.Exceptions.DataAccessException;
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
}

