package serialization;

import chess.ChessBoard;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonConfigurator {

    public static Gson makeSerializerDeserializer() {
        GsonBuilder gsonBuilder = new GsonBuilder().
                setPrettyPrinting().
                registerTypeAdapter(ChessBoard.class, new ChessBoardDeserializer());

        return gsonBuilder.create();
    }
}
