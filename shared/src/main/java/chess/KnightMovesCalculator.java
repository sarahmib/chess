package chess;

import java.util.ArrayList;
import java.util.Collection;

import static chess.PieceMovesCalculator.*;

public class KnightMovesCalculator {

    public static Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> validMoves = new ArrayList<>();
        int startRow = position.getRow() - 1;
        int startCol = position.getColumn() - 1;

        if (isValidMove(board, startRow, startCol, startRow - 2, startCol + 1)) {
            addMove(validMoves, position, startRow - 2, startCol + 1);
        }
        if (isValidMove(board, startRow, startCol, startRow - 2, startCol - 1)) {
            addMove(validMoves, position, startRow - 2, startCol - 1);
        }
        if (isValidMove(board, startRow, startCol, startRow - 1, startCol - 2)) {
            addMove(validMoves, position, startRow - 1, startCol - 2);
        }
        if (isValidMove(board, startRow, startCol, startRow - 1, startCol + 2)) {
            addMove(validMoves, position, startRow - 1, startCol + 2);
        }
        if (isValidMove(board, startRow, startCol, startRow + 1, startCol - 2)) {
            addMove(validMoves, position, startRow + 1, startCol - 2);
        }
        if (isValidMove(board, startRow, startCol, startRow + 1, startCol + 2)) {
            addMove(validMoves, position, startRow + 1, startCol + 2);
        }
        if (isValidMove(board, startRow, startCol, startRow + 2, startCol - 1)) {
            addMove(validMoves, position, startRow + 2, startCol - 1);
        }
        if (isValidMove(board, startRow, startCol, startRow + 2, startCol + 1)) {
            addMove(validMoves, position, startRow + 2, startCol + 1);
        }

        return validMoves;
    }
}
