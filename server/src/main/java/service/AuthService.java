package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import model.AuthData;
import response.LoginResponse;
import response.RegisterResponse;

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

    public RegisterResponse register(String username) throws DataAccessException {
        AuthData response = createAuth(username);
        return new RegisterResponse(response.username(), response.authToken(), null);
    }

    public LoginResponse login(String username) throws DataAccessException {
        AuthData response = createAuth(username);
        return new LoginResponse(response.username(), response.authToken(), null);
    }
}
