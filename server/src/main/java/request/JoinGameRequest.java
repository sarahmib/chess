package request;

import chess.ChessGame;

public record JoinGameRequest(ChessGame.TeamColor playerColor, Integer gameID, String username) {
    public JoinGameRequest setUsername(String username) {
        return new JoinGameRequest(playerColor, gameID, username);
    }
}
