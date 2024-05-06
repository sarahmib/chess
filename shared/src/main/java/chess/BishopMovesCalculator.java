package chess;

import java.util.ArrayList;
import java.util.Collection;

import static chess.PieceMovesCalculator.upLeftDiagonal;
import static chess.PieceMovesCalculator.downLeftDiagonal;
import static chess.PieceMovesCalculator.upRightDiagonal;
import static chess.PieceMovesCalculator.downRightDiagonal;


public class BishopMovesCalculator {
    ChessBoard board;
    public static Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        int row = position.getRow() - 1;
        int column = position.getColumn() - 1;
        Collection<ChessMove> validMoves;
        validMoves = new ArrayList<>();

        upLeftDiagonal(board, row, column, validMoves);
        downLeftDiagonal(board, row, column, validMoves);
        upRightDiagonal(board, row, column, validMoves);
        downRightDiagonal(board, row, column, validMoves);

        return validMoves;
    }
}
