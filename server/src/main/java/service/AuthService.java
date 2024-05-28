package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import model.AuthData;

public class AuthService {
    private final AuthDAO authDataAccess;


    public AuthService(AuthDAO authDataAccess) {
        this.authDataAccess = authDataAccess;
    }

    public void clearAuths() throws DataAccessException {
        authDataAccess.clearAuths();
    }

    public AuthData createAuth(String username) throws DataAccessException {
        return authDataAccess.createAuth(username);
    }
}
