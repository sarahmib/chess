package dataaccess;

import chess.ChessBoard;
import chess.ChessPiece;
import chess.ChessPosition;
import com.google.gson.*;

import java.lang.reflect.Type;

public class ChessBoardDeserializer implements JsonDeserializer<ChessBoard> {

    @Override
    public ChessBoard deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        JsonArray boardArray = jsonObject.getAsJsonArray("board");

        ChessBoard board = new ChessBoard();

        for (int i = 0; i < boardArray.size(); i++) {
            JsonArray row = boardArray.get(i).getAsJsonArray();
            for (int j = 0; j < row.size(); j++) {
                JsonElement element = row.get(j);
                if (element != null && !element.isJsonNull()) {
                    ChessPiece piece = context.deserialize(element, ChessPiece.class);
                    board.addPiece(new ChessPosition(i + 1, j + 1), piece);
                }
            }
        }

        return board;
    }
}