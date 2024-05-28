package dataaccess;

import model.AuthData;

public interface AuthDAO {

    void clearAuths() throws DataAccessException;

    AuthData createAuth(String username) throws DataAccessException;

    void deleteAuth(String authToken) throws DataAccessException;

    AuthData getAuth(String authToken) throws DataAccessException;
}
