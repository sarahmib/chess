package chess;

import java.util.ArrayList;
import java.util.Collection;

public class PieceMovesCalculator {

    public static Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        return switch (board.getPiece(position).getPieceType()) {
            case KING -> KingMovesCalculator.pieceMoves(board, position);
            case QUEEN -> QueenMovesCalculator.pieceMoves(board, position);
            case BISHOP -> BishopMovesCalculator.pieceMoves(board, position);
            case KNIGHT -> KnightMovesCalculator.pieceMoves(board, position);
            case ROOK -> RookMovesCalculator.pieceMoves(board, position);
            case PAWN -> PawnMovesCalculator.pieceMoves(board, position);
        };
    }

    public static Collection<ChessMove> upLeftDiagonal(ChessBoard board, int row, int column, Collection<ChessMove> validMoves) {
        ChessPosition startPosition = new ChessPosition(row + 1, column + 1);
        int startRow = row;
        int startCol = column;
        boolean isEnd = false;
        row--;
        column--;

        while (!isEnd){
            if (0 > row || row > 7 || 0 > column || column > 7) {
                isEnd = true;
                continue;
            }
            if (board.board[row][column] instanceof ChessPiece) {
                isEnd = true;

                if (((ChessPiece) board.board[row][column]).getTeamColor() == ((ChessPiece) board.board[startRow][startCol]).getTeamColor()) {
                    continue;
                }
            }
            ChessPosition endPosition = new ChessPosition(row + 1, column + 1);
            ChessMove newMove = new ChessMove(startPosition, endPosition, null);
            validMoves.add(newMove);
            row--;
            column--;

        }
        return validMoves;
    }

    public static Collection<ChessMove> upRightDiagonal(ChessBoard board, int row, int column, Collection<ChessMove> validMoves) {
        ChessPosition startPosition = new ChessPosition(row + 1, column + 1);
        int startRow = row;
        int startCol = column;
        boolean isEnd = false;
        row--;
        column++;

        while (!isEnd){
            if (0 > row || row > 7 || 0 > column || column > 7) {
                isEnd = true;
                continue;
            }
            if (board.board[row][column] instanceof ChessPiece) {
                isEnd = true;

                if (((ChessPiece) board.board[row][column]).getTeamColor() == ((ChessPiece) board.board[startRow][startCol]).getTeamColor()) {
                    continue;
                }
            }
            ChessPosition endPosition = new ChessPosition(row + 1, column + 1);
            ChessMove newMove = new ChessMove(startPosition, endPosition, null);
            validMoves.add(newMove);
            row--;
            column++;

        }
        return validMoves;
    }

    public static Collection<ChessMove> downLeftDiagonal(ChessBoard board, int row, int column, Collection<ChessMove> validMoves) {
        ChessPosition startPosition = new ChessPosition(row + 1, column + 1);
        int startRow = row;
        int startCol = column;
        boolean isEnd = false;
        row++;
        column--;

        while (!isEnd){
            if (0 > row || row > 7 || 0 > column || column > 7) {
                isEnd = true;
                continue;
            }
            if (board.board[row][column] instanceof ChessPiece) {
                isEnd = true;

                if (((ChessPiece) board.board[row][column]).getTeamColor() == ((ChessPiece) board.board[startRow][startCol]).getTeamColor()) {
                    continue;
                }
            }
            ChessPosition endPosition = new ChessPosition(row + 1, column + 1);
            ChessMove newMove = new ChessMove(startPosition, endPosition, null);
            validMoves.add(newMove);
            row++;
            column--;

        }
        return validMoves;
    }

    public static Collection<ChessMove> downRightDiagonal(ChessBoard board, int row, int column, Collection<ChessMove> validMoves) {
        ChessPosition startPosition = new ChessPosition(row + 1, column + 1);
        int startRow = row;
        int startCol = column;
        boolean isEnd = false;
        row++;
        column++;

        while (!isEnd){
            if (0 > row || row > 7 || 0 > column || column > 7) {
                isEnd = true;
                continue;
            }
            if (board.board[row][column] instanceof ChessPiece) {
                isEnd = true;

                if (((ChessPiece) board.board[row][column]).getTeamColor() == ((ChessPiece) board.board[startRow][startCol]).getTeamColor()) {
                    continue;
                }
            }
            ChessPosition endPosition = new ChessPosition(row + 1, column + 1);
            ChessMove newMove = new ChessMove(startPosition, endPosition, null);
            validMoves.add(newMove);
            row++;
            column++;

        }
        return validMoves;
    }
}
