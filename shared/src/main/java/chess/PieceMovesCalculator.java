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

    // below are moves shared by multiple pieces -- for example, the diagonal movement of the queen and bishops

    public static void upLeftDiagonal(ChessBoard board, ChessPosition position, Collection<ChessMove> validMoves) {
        int startRow = position.getRow() - 1;
        int startCol = position.getColumn() - 1;
        int row = position.getRow() - 1;
        int column = position.getColumn() - 1;
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
            ChessMove newMove = new ChessMove(position, endPosition, null);
            validMoves.add(newMove);
            row--;
            column--;

        }
    }

    public static void upRightDiagonal(ChessBoard board, ChessPosition position, Collection<ChessMove> validMoves) {
        int startRow = position.getRow() - 1;
        int startCol = position.getColumn() - 1;
        int row = position.getRow() - 1;
        int column = position.getColumn() - 1;
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
            ChessMove newMove = new ChessMove(position, endPosition, null);
            validMoves.add(newMove);
            row--;
            column++;

        }
    }

    public static void downLeftDiagonal(ChessBoard board, ChessPosition position, Collection<ChessMove> validMoves) {
        int startRow = position.getRow() - 1;
        int startCol = position.getColumn() - 1;
        int row = position.getRow() - 1;
        int column = position.getColumn() - 1;
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
            ChessMove newMove = new ChessMove(position, endPosition, null);
            validMoves.add(newMove);
            row++;
            column--;

        }
    }

    public static void downRightDiagonal(ChessBoard board, ChessPosition position, Collection<ChessMove> validMoves) {
        int startRow = position.getRow() - 1;
        int startCol = position.getColumn() - 1;
        int row = position.getRow() - 1;
        int column = position.getColumn() - 1;
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
            ChessMove newMove = new ChessMove(position, endPosition, null);
            validMoves.add(newMove);
            row++;
            column++;

        }
    }

    public static void straightUp(ChessBoard board, ChessPosition position, Collection<ChessMove> validMoves) {
        int startRow = position.getRow() - 1;
        int startCol = position.getColumn() - 1;
        int row = position.getRow() - 1;
        boolean isEnd = false;
        row--;

        while (!isEnd) {
            if (0 > row || row > 7) {
                isEnd = true;
                continue;
            }
            if (board.board[row][startCol] instanceof ChessPiece) {
                isEnd = true;

                if (((ChessPiece) board.board[startRow][startCol]).getTeamColor() == ((ChessPiece) board.board[row][startCol]).getTeamColor()) {
                    continue;
                }
            }
            ChessPosition endPosition = new ChessPosition(row + 1, startCol + 1);
            ChessMove newMove = new ChessMove(position, endPosition, null);
            validMoves.add(newMove);
            row--;
        }
    }

    public static void straightDown(ChessBoard board, ChessPosition position, Collection<ChessMove> validMoves) {
        int startRow = position.getRow() - 1;
        int startCol = position.getColumn() - 1;
        int row = position.getRow() - 1;
        boolean isEnd = false;
        row++;

        while (!isEnd) {
            if (0 > row || row > 7) {
                isEnd = true;
                continue;
            }
            if (board.board[row][startCol] instanceof ChessPiece) {
                isEnd = true;

                if (((ChessPiece) board.board[startRow][startCol]).getTeamColor() == ((ChessPiece) board.board[row][startCol]).getTeamColor()) {
                    continue;
                }
            }
            ChessPosition endPosition = new ChessPosition(row + 1, startCol + 1);
            ChessMove newMove = new ChessMove(position, endPosition, null);
            validMoves.add(newMove);
            row++;
        }
    }

    public static void straightLeft(ChessBoard board, ChessPosition position, Collection<ChessMove> validMoves) {
        int startRow = position.getRow() - 1;
        int startCol = position.getColumn() - 1;
        int col = position.getColumn() - 1;
        boolean isEnd = false;
        col--;

        while (!isEnd) {
            if (0 > col || col > 7) {
                isEnd = true;
                continue;
            }
            if (board.board[startRow][col] instanceof ChessPiece) {
                isEnd = true;

                if (((ChessPiece) board.board[startRow][startCol]).getTeamColor() == ((ChessPiece) board.board[startRow][col]).getTeamColor()) {
                    continue;
                }
            }
            ChessPosition endPosition = new ChessPosition(startRow + 1, col + 1);
            ChessMove newMove = new ChessMove(position, endPosition, null);
            validMoves.add(newMove);
            col--;
        }
    }

    public static void straightRight(ChessBoard board, ChessPosition position, Collection<ChessMove> validMoves) {
        int startRow = position.getRow() - 1;
        int startCol = position.getColumn() - 1;
        int col = position.getColumn() - 1;
        boolean isEnd = false;
        col++;

        while (!isEnd) {
            if (0 > col || col > 7) {
                isEnd = true;
                continue;
            }
            if (board.board[startRow][col] instanceof ChessPiece) {
                isEnd = true;

                if (((ChessPiece) board.board[startRow][startCol]).getTeamColor() == ((ChessPiece) board.board[startRow][col]).getTeamColor()) {
                    continue;
                }
            }
            ChessPosition endPosition = new ChessPosition(startRow + 1, col + 1);
            ChessMove newMove = new ChessMove(position, endPosition, null);
            validMoves.add(newMove);
            col++;
        }
    }
}
