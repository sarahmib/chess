package chess;

import com.google.gson.Gson;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ChessBoardTests {

    @Test
    @DisplayName("chess board being silly :/")
    public void testChessBoardSerialization() {
        String jsonChessboard = new Gson().toJson(new ChessBoard());
        ChessBoard fromJsonChessboard = new Gson().fromJson(jsonChessboard, ChessBoard.class);

        assertEquals(new ChessBoard(), fromJsonChessboard);
    }

    @Test
    @DisplayName("chess game")
    public void testChessGameSerialization() {
        String jsonChessGame = new Gson().toJson(new ChessGame());
        ChessGame fromJsonChessGame = new Gson().fromJson(jsonChessGame, ChessGame.class);

        assertEquals(new ChessGame(), fromJsonChessGame);
    }
}
