package service;
import dataaccess.MemoryDAO.MemoryAuthDAO;
import dataaccess.MemoryDAO.MemoryGameDAO;
import dataaccess.MemoryDAO.MemoryUserDAO;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;


public class ServiceTests {

    private static AuthService authService;
    private static GameService gameService;
    private static UserService userService;

    @BeforeAll
    public static void init() {
        authService = new AuthService(new MemoryAuthDAO());
        gameService = new GameService(new MemoryGameDAO(), authService);
        userService = new UserService(new MemoryUserDAO());
    }

    @BeforeEach
    public void setup() {
        try {
            authService.clearAuths();
            gameService.clearGames();
            userService.clearUsers();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    @DisplayName("Auth clear")
    public void testAuthClear() {
        assertDoesNotThrow(() -> authService.clearAuths());
    }

    @Test
    @DisplayName("Game clear")
    public void testGameClear() {
        assertDoesNotThrow(() -> gameService.clearGames());
    }

    @Test
    @DisplayName("User clear")
    public void testUserClear() {
        assertDoesNotThrow(() -> userService.clearUsers());
    }

}
