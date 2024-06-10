package client;

import dataaccess.AlreadyTakenException;
import dataaccess.DataAccessException;
import org.junit.jupiter.api.*;
import response.LoginResponse;
import response.RegisterResponse;
import server.Server;
import ui.ServerFacade;

import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade serverFacade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(8080);
        System.out.println("Started test HTTP server on " + port);

        serverFacade = new ServerFacade("http://localhost:8080");
        try {
            serverFacade.clearDatabase();
        } catch (DataAccessException e) {
            System.out.println("Something went wrong: " + e.getMessage());
        }
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    @DisplayName("test register success")
    public void testRegisterSuccess() {
        RegisterResponse response = assertDoesNotThrow(() -> serverFacade.register("username", "password", "email@email.com"));
        assertEquals("username", response.username());
        assertEquals(36, response.authToken().length());
    }

    @Test
    @DisplayName("test register username taken")
    public void testRegisterUsernameTaken() {
        RegisterResponse response = assertDoesNotThrow(() -> serverFacade.register("username", "password", "email@email.com"));
        assertThrows(DataAccessException.class, () -> serverFacade.register("username", "duplicate", "anotheremail@email.com"));
    }

    @Test
    @DisplayName("test login user success")
    public void testLoginUserSuccess() {
        assertDoesNotThrow(() -> serverFacade.register("username", "password", "email@email.com"));
        LoginResponse response = assertDoesNotThrow(() -> serverFacade.login("username", "password"));
        assertEquals("username", response.username());
        assertEquals(36, response.authToken().length());
    }

    @Test
    @DisplayName("test login user wrong password")
    public void testLoginUserWrongPassword() {
        assertDoesNotThrow(() -> serverFacade.register("username", "password", "email@email.com"));
        assertThrows(DataAccessException.class, () -> serverFacade.login("username", "wrongPassword"));
    }

    @Test
    @DisplayName("test logout user success")
    public void testLogoutUserSuccess() {
        assertDoesNotThrow(() -> serverFacade.register("username", "password", "email@email.com"));
        LoginResponse response = assertDoesNotThrow(() -> serverFacade.login("username", "password"));
        assertDoesNotThrow(() -> serverFacade.logout(response.authToken()));
        assertThrows(DataAccessException.class, () -> serverFacade.logout(response.authToken()));
    }

    @Test
    @DisplayName("test logout user bad auth token")
    public void testLogoutUserBadAuthToken() {
        assertDoesNotThrow(() -> serverFacade.register("username", "password", "email@email.com"));
        assertDoesNotThrow(() -> serverFacade.login("username", "password"));
        assertThrows(DataAccessException.class, () -> serverFacade.logout(null));
    }
}
