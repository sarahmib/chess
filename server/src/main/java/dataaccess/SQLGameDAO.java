package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.Exceptions.DataAccessException;
import model.GameData;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import static dataaccess.SQLExecution.executeUpdate;

public class SQLGameDAO implements GameDAO {

    public SQLGameDAO() throws DataAccessException {
        configureGameTable();
    }

    @Override
    public void clearGames() throws DataAccessException {
        String statement = "DELETE games";
        executeUpdate(statement);
    }

    @Override
    public Collection<GameData> listGames() throws DataAccessException {
        return List.of();
    }

    @Override
    public Integer createGame(String gameName) throws DataAccessException {
        return 0;
    }

    @Override
    public GameData getGame(int gameId) throws DataAccessException {
        return null;
    }

    @Override
    public void updateGame(GameData gameData) throws DataAccessException {

    }

    private final String[] createGameStatements = {
            """
            CREATE TABLE IF NOT EXISTS  games (
              `GameID` int NOT NULL AUTO_INCREMENT,
              `whiteUsername` VARCHAR(255) DEFAULT NULL,
              `blackUsername` VARCHAR(255) DEFAULT NULL,
              `gameName` TEXT NOT NULL,
              `game` TEXT NOT NULL,
              PRIMARY KEY (`GameID`),
              FOREIGN KEY (`whiteUsername`) REFERENCES users(`username`)
              ON DELETE CASCADE
              ON UPDATE CASCADE,
              FOREIGN KEY (`blackUsername`) REFERENCES users(`username`)
              ON DELETE CASCADE
              ON UPDATE CASCADE
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };

    private ChessGame readChessGame(ResultSet rs) throws SQLException {
        var json = rs.getString("game");
        return new Gson().fromJson(json, ChessGame.class);
    }

    private void configureGameTable() throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : createGameStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException(String.format("Unable to configure database: %s", ex.getMessage()));
        }
    }
}
