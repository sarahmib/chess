package chess;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    List<List<Object>> board  = new ArrayList<List<Object>>();

    public ChessBoard() {
        for (int i = 0; i < 8; i++) {
            List<Object> newRow = new ArrayList<Object>();
            for (int j = 0; j < 8; j++) {
                newRow.add(j, "");
            }
            board.add(i, newRow);
        }
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        int row = position.getRow();
        int column = position.getColumn();

        board.get(row).add(column, piece);
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        int row = position.getRow();
        int column = position.getColumn();

        if (board.get(row).get(column) != "") {
            return (ChessPiece) board.get(row).get(column);
        }

        return null;
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        throw new RuntimeException("Not implemented");
    }
}
