package chess;

import java.util.ArrayList;
import java.util.Collection;

import static chess.PieceMovesCalculator.addMove;
import static chess.PieceMovesCalculator.isValidMove;

public class KingMovesCalculator {

    public static Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> validMoves = new ArrayList<>();
        int row = position.getRow() - 1;
        int column = position.getColumn() - 1;

        if (isValidMove(board, row, column, row - 1, column - 1)) {
            addMove(validMoves, position, row - 1, column - 1);
        }
        if (isValidMove(board, row, column, row - 1, column)) {
            addMove(validMoves, position, row - 1, column);
        }
        if (isValidMove(board, row, column, row - 1, column + 1)) {
            addMove(validMoves, position, row - 1, column + 1);
        }
        if (isValidMove(board, row, column, row, column - 1)) {
            addMove(validMoves, position, row, column - 1);
        }
        if (isValidMove(board, row, column, row, column + 1)) {
            addMove(validMoves, position, row, column + 1);
        }
        if (isValidMove(board, row, column, row + 1, column - 1)) {
            addMove(validMoves, position, row + 1, column - 1);
        }
        if (isValidMove(board, row, column, row + 1, column)) {
            addMove(validMoves, position, row + 1, column);
        }
        if (isValidMove(board, row, column, row + 1, column + 1)) {
            addMove(validMoves, position, row + 1, column + 1);
        }


        return validMoves;
    }


}
