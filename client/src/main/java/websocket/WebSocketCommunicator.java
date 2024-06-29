package websocket;

import com.google.gson.Gson;
import serialization.GsonConfigurator;
import websocket.commands.ConnectCommand;
import websocket.messages.ServerMessage;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

//need to extend Endpoint for websocket to work properly
public class WebSocketCommunicator extends Endpoint {

    Session session;
    ServerMessageObserver serverMessageObserver;
    Gson gson = GsonConfigurator.makeSerializerDeserializer();


    public WebSocketCommunicator(String url, ServerMessageObserver serverMessageObserver) throws IOException {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/ws");
            this.serverMessageObserver = serverMessageObserver;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            //set message handler
            this.session.addMessageHandler((MessageHandler.Whole<String>) message -> {
                ServerMessage serverMessage = gson.fromJson(message, ServerMessage.class);
                serverMessageObserver.notify(serverMessage);
            });
        } catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new IOException(ex.getMessage());
        }
    }

    //Endpoint requires this method, but you don't have to do anything
    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }

    public void joinGame(String authToken, Integer gameID) throws IOException {
        try {
            ConnectCommand connect = new ConnectCommand(authToken, gameID);
            this.session.getBasicRemote().sendText(gson.toJson(connect));
        } catch (IOException ex) {
            throw new IOException(ex.getMessage());
        }
    }
}