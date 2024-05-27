package server;

import service.AuthService;
import service.GameService;
import service.UserService;
import spark.*;

public class Server {

    private final UserService userService;
    private final GameService gameService;
    private final AuthService authService;

    public Server() {
        userService = new UserService();
        gameService = new GameService();
        authService = new AuthService();

    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.delete("/db", this::clearDb);

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
