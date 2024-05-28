package server;

import dataaccess.DataAccessException;
import spark.*;

public class Server {

    private final Handler handler;

    public Server() {
        handler = new Handler();
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
            Spark.post("/user", handler::register);
            Spark.delete("/db", handler::clearDb);
            Spark.exception(DataAccessException.class, this::exceptionHandler);


        Spark.awaitInitialization();
        return Spark.port();
    }

    private void exceptionHandler(DataAccessException ex, Request req, Response res) {
        res.status(500);
        res.body("{ \"message\" : \"Error : " + ex.getMessage() + "\" }");
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

}
