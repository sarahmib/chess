package chess;

import java.util.ArrayList;
import java.util.Collection;

public class MovesCalculator {

    public static Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        ChessPiece piece = board.getPiece(position);
        Collection<ChessMove> validMoves = new ArrayList<>();

        switch(piece.getPieceType()) {
            case KING -> KingMovesCalculator.pieceMoves(board, position, validMoves);
            case QUEEN -> QueenMovesCalculator.pieceMoves(board, position, validMoves);
            case PAWN -> PawnMovesCalculator.pieceMoves(board, position, validMoves);
            case ROOK -> RookMovesCalculator.pieceMoves(board, position, validMoves);
            case BISHOP -> BishopMovesCalculator.pieceMoves(board, position, validMoves);
            case KNIGHT -> KnightMovesCalculator.pieceMoves(board, position, validMoves);
        }

        return validMoves;
    }

    public static void moveUp(ChessBoard board, ChessPosition startPosition, Collection<ChessMove> validMoves) {
        int row = startPosition.getRow();

        row--;

        while(isValidMove(board, startPosition, new ChessPosition(row, startPosition.getColumn()))) {
            addMove(startPosition, new ChessPosition(row, startPosition.getColumn()), validMoves);

            if (board.getPiece(new ChessPosition(row, startPosition.getColumn())) != null) {
                break;
            }
            row--;
        }
    }

    public static void moveRight(ChessBoard board, ChessPosition startPosition, Collection<ChessMove> validMoves) {
        int col = startPosition.getColumn();

        col++;

        while(isValidMove(board, startPosition, new ChessPosition(startPosition.getRow(), col))) {
            addMove(startPosition, new ChessPosition(startPosition.getRow(), col), validMoves);

            if (board.getPiece(new ChessPosition(startPosition.getRow(), col)) != null) {
                break;
            }
            col++;
        }
    }

    public static void moveDown(ChessBoard board, ChessPosition startPosition, Collection<ChessMove> validMoves) {
        int row = startPosition.getRow();

        row++;

        while(isValidMove(board, startPosition, new ChessPosition(row, startPosition.getColumn()))) {
            addMove(startPosition, new ChessPosition(row, startPosition.getColumn()), validMoves);

            if (board.getPiece(new ChessPosition(row, startPosition.getColumn())) != null) {
                break;
            }
            row++;
        }
    }

    public static void moveLeft(ChessBoard board, ChessPosition startPosition, Collection<ChessMove> validMoves) {
        int col = startPosition.getColumn();

        col--;

        while(isValidMove(board, startPosition, new ChessPosition(startPosition.getRow(), col))) {
            addMove(startPosition, new ChessPosition(startPosition.getRow(), col), validMoves);

            if (board.getPiece(new ChessPosition(startPosition.getRow(), col)) != null) {
                break;
            }
            col--;
        }
    }

    public static void moveUpLeft(ChessBoard board, ChessPosition startPosition, Collection<ChessMove> validMoves) {
        int row = startPosition.getRow();
        int col = startPosition.getColumn();

        row--;
        col--;

        while(isValidMove(board, startPosition, new ChessPosition(row, col))) {
            addMove(startPosition, new ChessPosition(row, col), validMoves);

            if (board.getPiece(new ChessPosition(row, col)) != null) {
                break;
            }
            row--;
            col--;
        }
    }

    public static void moveUpRight(ChessBoard board, ChessPosition startPosition, Collection<ChessMove> validMoves) {
        int row = startPosition.getRow();
        int col = startPosition.getColumn();

        row--;
        col++;

        while(isValidMove(board, startPosition, new ChessPosition(row, col))) {
            addMove(startPosition, new ChessPosition(row, col), validMoves);

            if (board.getPiece(new ChessPosition(row, col)) != null) {
                break;
            }
            row--;
            col++;
        }
    }

    public static void moveDownLeft(ChessBoard board, ChessPosition startPosition, Collection<ChessMove> validMoves) {
        int row = startPosition.getRow();
        int col = startPosition.getColumn();

        row++;
        col--;

        while(isValidMove(board, startPosition, new ChessPosition(row, col))) {
            addMove(startPosition, new ChessPosition(row, col), validMoves);

            if (board.getPiece(new ChessPosition(row, col)) != null) {
                break;
            }
            row++;
            col--;
        }
    }

    public static void moveDownRight(ChessBoard board, ChessPosition startPosition, Collection<ChessMove> validMoves) {
        int row = startPosition.getRow();
        int col = startPosition.getColumn();

        row++;
        col++;

        while(isValidMove(board, startPosition, new ChessPosition(row, col))) {
            addMove(startPosition, new ChessPosition(row, col), validMoves);

            if (board.getPiece(new ChessPosition(row, col)) != null) {
                break;
            }
            row++;
            col++;
        }
    }

    public static boolean isValidMove(ChessBoard board, ChessPosition startPosition, ChessPosition endPosition) {
        if (endPosition.getColumn() > 8 || endPosition.getColumn() < 1 || endPosition.getRow() > 8 || endPosition.getRow() < 1) {
            return false;
        }
        if (board.getPiece(endPosition) != null) {
            return board.getPiece(startPosition).getTeamColor() != board.getPiece(endPosition).getTeamColor();
        }
        return true;
    }

    public static void addMove(ChessPosition startPosition, ChessPosition endPosition, Collection<ChessMove> validMoves) {
        validMoves.add(new ChessMove(startPosition, endPosition, null));
    }
}
