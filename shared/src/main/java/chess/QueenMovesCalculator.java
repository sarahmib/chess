package chess;

import java.util.ArrayList;
import java.util.Collection;

import static chess.PieceMovesCalculator.*;

public class QueenMovesCalculator {

    public static Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> validMoves = new ArrayList<>();

        straightUp(board, position, validMoves);
        upRightDiagonal(board, position, validMoves);
        straightLeft(board, position, validMoves);
        downRightDiagonal(board, position, validMoves);
        straightDown(board, position, validMoves);
        downLeftDiagonal(board, position, validMoves);
        straightRight(board, position, validMoves);
        upLeftDiagonal(board, position, validMoves);

        return validMoves;
    }
}
