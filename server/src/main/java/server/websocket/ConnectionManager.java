package server.websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import serialization.GsonConfigurator;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public final ConcurrentHashMap<Integer, Set<Session>> connections = new ConcurrentHashMap<>();
    private final Gson gson = GsonConfigurator.makeSerializerDeserializer();

    public void add(Integer gameID, Session session) {
        if (connections.containsKey(gameID)) {
            connections.get(gameID).add(session);
        } else {
            Set<Session> sessions = new java.util.HashSet<>(Set.of());
            sessions.add(session);
            connections.put(gameID, sessions);
        }
    }

    public void remove(Integer gameID, Session session) {
        connections.get(gameID).remove(session);
    }

    public void broadcast(Integer gameID, Session session, ServerMessage notification) throws IOException {
        var removeList = new ArrayList<Session>();
        for (Session connectionSession : connections.get(gameID)) {
            if (connectionSession.isOpen()) {
                if (!connectionSession.equals(session)) {
                    connectionSession.getRemote().sendString(gson.toJson(notification));
                }
            } else {
                removeList.add(connectionSession);
            }
        }

        // Clean up any connections that were left open.
        for (var c : removeList) {
            remove(gameID, session);
        }
    }
}