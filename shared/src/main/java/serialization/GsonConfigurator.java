package serialization;

import chess.ChessBoard;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

public class GsonConfigurator {

    public static Gson makeSerializerDeserializer() {
        GsonBuilder gsonBuilder = new GsonBuilder().
                setPrettyPrinting().
                registerTypeAdapter(ChessBoard.class, new ChessBoardDeserializer())
                .registerTypeAdapter(ServerMessage.class, new ServerMessageAdapter())
                .registerTypeAdapter(UserGameCommand.class, new UserCommandAdapter());

        return gsonBuilder.create();
    }
}
