package server.websocket;

import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ErrorMessage;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public final ConcurrentHashMap<Integer, Set<Connection>> connections = new ConcurrentHashMap<>();

    public void add(Integer gameID, String playerName, Session session) throws IOException {
        Connection connection = new Connection(playerName, session);
        Set<Connection> tempConnections = connections.get(gameID);
        if (tempConnections == null) {
            ErrorMessage message = new ErrorMessage("Error: game ID does not exist");
            session.getRemote().sendString(message.toString());
        } else {
            tempConnections.add(connection);
            connections.put(gameID, tempConnections);
        }
    }

    public void remove(Integer gameID, String playerName) {
        Set<Connection> tempConnections = connections.get(gameID);
        for (Connection c : tempConnections) {
            if (c.playerName == playerName) {
                tempConnections.remove(c);
                connections.put(gameID, tempConnections);
                break;
            }
        }
    }

    public void broadcast(Integer gameID, String excludeUsername, ServerMessage notification) throws IOException {
        var removeList = new ArrayList<Connection>();
        Set<Connection> connectionList = connections.get(gameID);
        for (var connection : connectionList) {
            if (connection.session.isOpen()) {
                if (!connection.playerName.equals(excludeUsername)) {
                    connection.send(notification.toString());
                }
            } else {
                removeList.add(connection);
            }
        }

        for (var c : removeList) {
            remove(gameID, c.playerName);
        }
    }
}