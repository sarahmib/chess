package request;

import chess.ChessGame;

public record JoinGameRequest(ChessGame.TeamColor teamColor, Integer gameID, String username) {
    public JoinGameRequest setUsername(String username) {
        return new JoinGameRequest(teamColor, gameID, username);
    }
}
