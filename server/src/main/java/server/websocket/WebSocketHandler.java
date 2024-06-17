package server.websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import serialization.GsonConfigurator;
import websocket.commands.*;
import websocket.commands.UserGameCommand;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.io.IOException;


@WebSocket
public class WebSocketHandler {

    private final ConnectionManager connections = new ConnectionManager();
    private final Gson gson = GsonConfigurator.makeSerializerDeserializer();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        try {
            UserGameCommand command = gson.fromJson(message, UserGameCommand.class);

            // Throws a custom UnauthorizedException. Yours may work differently.
            String username = getUsername(command.getAuthString());

//            saveSession(command.getGameID(), session);

            switch (command.getCommandType()) {
                case CONNECT -> connect(session, username, (Connect) command);
                case MAKE_MOVE -> makeMove(session, username, (MakeMove) command);
                case LEAVE -> leaveGame(session, username, (Leave) command);
                case RESIGN -> resign(session, username, (Resign) command);
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());        //SEND AN ACTUAL ERROR MESSAGE
        }
    }

    private String getUsername(String authToken) {
        return null;
    }

    private Integer getGameID() {
        return null;
    }

    private void saveSession(Integer gameID, Session session) {

    }

    private void connect(Session session, String username, Connect command) throws IOException {
        connections.add(command.getGameID(), username, session);
        String message = String.format("%s just joined the game.", username);
        NotificationMessage notification = new NotificationMessage(message);
        connections.broadcast(command.getGameID(), username, notification);
    }

    private void leaveGame(Session session, String username, Leave command) {}

    private void resign(Session session, String username, Resign command) {}

    private void makeMove(Session session, String username, MakeMove command) {}

//    private void enter(String visitorName, Session session) throws IOException {
//        connections.add(visitorName, session);
//        var message = String.format("%s is in the shop", visitorName);
//        var notification = new Notification(Notification.Type.ARRIVAL, message);
//        connections.broadcast(visitorName, notification);
//    }
//
//    private void exit(String visitorName) throws IOException {
//        connections.remove(visitorName);
//        var message = String.format("%s left the shop", visitorName);
//        var notification = new Notification(Notification.Type.DEPARTURE, message);
//        connections.broadcast(visitorName, notification);
//    }
//
//    public void makeNoise(String petName, String sound) throws ResponseException {
//        try {
//            var message = String.format("%s says %s", petName, sound);
//            var notification = new Notification(Notification.Type.NOISE, message);
//            connections.broadcast("", notification);
//        } catch (Exception ex) {
//            throw new ResponseException(500, ex.getMessage());
//        }
//    }
}