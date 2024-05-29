package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class MemoryGameDAO implements GameDAO {

    private int nextId = 1;
    final private HashMap<Integer, GameData> games = new HashMap<>();

    @Override
    public void clearGames() {
        games.clear();
    }

    @Override
    public Collection<GameData> listGames() throws DataAccessException {
        return games.values();
    }

    @Override
    public Integer createGame(String gameName) throws DataAccessException {
        GameData gameData = new GameData(nextId, null, null, gameName, new ChessGame());
        games.put(nextId, gameData);
        Integer gameID = nextId;
        nextId++;
        return gameID;
    }
}
