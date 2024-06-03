package dataaccess;

import dataaccess.Exceptions.DataAccessException;
import model.GameData;

import java.util.Collection;

public interface GameDAO {

    void clearGames() throws DataAccessException;

    Collection<GameData> listGames() throws DataAccessException;

    Integer createGame(String gameName) throws DataAccessException;

    GameData getGame(int gameId) throws DataAccessException;

    void updateGame(GameData gameData) throws DataAccessException;
}
