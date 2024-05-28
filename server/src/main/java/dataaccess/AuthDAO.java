package dataaccess;

import model.AuthData;

public interface AuthDAO {

    void clearAuths() throws DataAccessException;

    AuthData createAuth(String username) throws DataAccessException;
}
