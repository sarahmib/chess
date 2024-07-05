package ui;

import chess.ChessGame;
import chess.ChessPiece;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static ui.EscapeSequences.*;

public class OutputChessboard {

    private static final int BOARD_SIZE_IN_SQUARES = 8;
    private static final String EMPTY = " ";
    private static final String PAWN = "P";
    private static final String ROOK = "R";
    private static final String BISHOP = "B";
    private static final String KNIGHT = "N";
    private static final String QUEEN = "Q";
    private static final String KING = "K";

    public static void main(Object[][] board, ChessGame.TeamColor teamColor) {
        PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        out.print(ERASE_SCREEN);

        if (teamColor == ChessGame.TeamColor.BLACK) {
            drawHeadersAndFooters(out, ChessGame.TeamColor.BLACK);

            drawChessBoard(out, board, ChessGame.TeamColor.BLACK);

            drawHeadersAndFooters(out, ChessGame.TeamColor.BLACK);
        } else {

            drawHeadersAndFooters(out, ChessGame.TeamColor.WHITE);

            drawChessBoard(out, board, ChessGame.TeamColor.WHITE);

            drawHeadersAndFooters(out, ChessGame.TeamColor.WHITE);
        }

        setBackground(out);
        out.println();

        out.print(RESET_BG_COLOR);
        out.print(SET_TEXT_COLOR_WHITE);
    }

    private static void drawHeadersAndFooters(PrintStream out, ChessGame.TeamColor teamColor) {
        setBorder(out);

        String[] headers = {"a", "b", "c", "d", "e", "f", "g", "h",};

        out.print(EMPTY.repeat(3));
        if (teamColor == ChessGame.TeamColor.WHITE) {
            for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; boardCol++) {
                drawHeaderandFooter(out, headers[boardCol]);
            }
        } else {
            for (int boardCol = BOARD_SIZE_IN_SQUARES - 1; boardCol > -1; boardCol--) {
                drawHeaderandFooter(out, headers[boardCol]);
            }
        }
        out.print(EMPTY.repeat(3));
        setBackground(out);

        out.println();
    }

    private static void drawHeaderandFooter(PrintStream out, String headerText) {
        out.print(EMPTY);
        out.print(headerText);
        out.print(EMPTY);
    }

    private static void drawChessBoard(PrintStream out, Object[][] board, ChessGame.TeamColor teamColor) {
        if (teamColor == ChessGame.TeamColor.WHITE) {
            for (int row = BOARD_SIZE_IN_SQUARES; row > 0; row--) {
                if (row == 8 || row == 6 || row == 4 || row == 2) {
                    drawRow(out, board[row - 1], String.valueOf(row), true);
                } else {
                    drawRow(out, board[row - 1], String.valueOf(row), false);
                }
            }
        } else {
            for (int row = 1; row < BOARD_SIZE_IN_SQUARES + 1; row++) {
                reverseRow(board[row - 1]);
                if (row == 1 || row == 3 || row == 5 || row == 7) {
                    drawRow(out, board[row - 1], String.valueOf(row), true);
                } else {
                    drawRow(out, board[row - 1], String.valueOf(row), false);
                }
            }
        }
    }

    private static void reverseRow(Object[] row) {
        int left = 0;
        int right = row.length - 1;

        while (left < right) {
            Object temp = row[left];
            row[left] = row[right];
            row[right] = temp;

            left++;
            right--;
        }
    }

    private static void drawRow(PrintStream out, Object[] row, String prefixAndAffix, boolean whiteSquareStart) {
        drawPrefixAndAffix(out, prefixAndAffix);

        boolean isWhite;
        if (whiteSquareStart) {
            isWhite = true;
        } else {
            isWhite = false;
        }

        for (int column = 0; column < BOARD_SIZE_IN_SQUARES; column++) {
            String piece = null;
            boolean pieceColorIsWhite = true;

            if (row[column] != null) {
                piece = getPieceType((ChessPiece) row[column]);

                if (((ChessPiece) row[column]).getTeamColor() == ChessGame.TeamColor.BLACK) {
                    pieceColorIsWhite = false;
                }
            }

            drawSquare(out, isWhite, piece, pieceColorIsWhite);

            if (isWhite) {
                isWhite = false;
            } else {
                isWhite = true;
            }
        }

        drawPrefixAndAffix(out, prefixAndAffix);
        setBackground(out);

        out.println();
    }

    private static String getPieceType(ChessPiece piece) {
        switch(piece.getPieceType()) {
            case QUEEN -> {
                return QUEEN;
            }
            case KING -> {
                return KING;
            }
            case ROOK -> {
                return ROOK;
            }
            case BISHOP -> {
                return BISHOP;
            }
            case KNIGHT -> {
                return KNIGHT;
            }
            case PAWN -> {
                return PAWN;
            }
        }
        return "";
    }

    private static void drawPrefixAndAffix(PrintStream out, String text) {
        setBorder(out);
        out.print(EMPTY);
        out.print(text);
        out.print(EMPTY);
    }

    private static void drawSquare(PrintStream out, boolean isWhite, String piece, boolean pieceColorIsWhite) {
        if (pieceColorIsWhite) {
            out.print(SET_TEXT_COLOR_BLUE);
        } else {
            out.print(SET_TEXT_COLOR_RED);
        }

        if (isWhite) {
            drawWhiteSquare(out, piece);
        } else {
            drawBlackSquare(out, piece);
        }
    }

    private static void drawWhiteSquare(PrintStream out, String piece) {
        out.print(SET_BG_COLOR_WHITE);
        if (piece != null) {
            out.print(EMPTY);
            out.print(piece);
            out.print(EMPTY);
        } else {
            out.print(EMPTY.repeat(3));
        }
    }

    private static void drawBlackSquare(PrintStream out, String piece) {
        out.print(SET_BG_COLOR_BLACK);
        if (piece != null) {
            out.print(EMPTY);
            out.print(piece);
            out.print(EMPTY);
        } else {
            out.print(EMPTY.repeat(3));
        }
    }

    private static void setBorder(PrintStream out) {
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(SET_TEXT_COLOR_BLACK);
    }

    private static void setBackground(PrintStream out) {
        out.print(SET_BG_COLOR_DARK_GREY);
    }
}
