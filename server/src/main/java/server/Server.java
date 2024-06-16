package server;

import dataaccess.DataAccessException;
import spark.*;

public class Server {

    private final Handler handler;

    public Server() {
        Handler handlerTemp;
        try {
            handlerTemp = new Handler();
        } catch (DataAccessException e) {
            handlerTemp = null;
            System.out.println("Something went wrong: " + e.getMessage());
            System.exit(1);
        }
        handler = handlerTemp;
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
            // Spark.webSocket("/ws", webSocketHandler);

            Spark.post("/user", handler::register);
            Spark.post("/session", handler::login);
            Spark.delete("/session", handler::logout);
            Spark.get("/game", handler::listGames);
            Spark.post("/game", handler::createGame);
            Spark.put("/game", handler::joinGame);
            Spark.delete("/db", handler::clearDb);
            Spark.exception(DataAccessException.class, this::exceptionHandler);


        Spark.awaitInitialization();
        return Spark.port();
    }

    private Object exceptionHandler(DataAccessException ex, Request req, Response res) {
        res.status(500);
        res.type("application/json");
        return "{ \"message\" : \"Error : " + ex.getMessage() + "\" }";
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

}
