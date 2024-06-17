package serialization;

import chess.ChessBoard;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import websocket.messages.ServerMessage;

public class GsonConfigurator {

    public static Gson makeSerializerDeserializer() {
        GsonBuilder gsonBuilder = new GsonBuilder().
                setPrettyPrinting().
                registerTypeAdapter(ChessBoard.class, new ChessBoardDeserializer())
                .registerTypeAdapter(ServerMessage.class, new ServerMessageAdapter());

        return gsonBuilder.create();
    }
}
