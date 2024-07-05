package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.UnauthorizedException;
import model.AuthData;
import request.LogoutRequest;
import response.LoginResponse;
import response.LogoutResponse;
import response.RegisterResponse;

public class AuthService {
    private final AuthDAO authDataAccess;


    public AuthService(AuthDAO authDataAccess) {
        this.authDataAccess = authDataAccess;
    }

    public void clearAuths() throws DataAccessException {
        authDataAccess.clearAuths();
    }

    private AuthData createAuth(String username) throws DataAccessException {
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

    public LogoutResponse logout(LogoutRequest request) throws DataAccessException {
        isAuthorized(request.authToken());
        authDataAccess.deleteAuth(request.authToken());
        return new LogoutResponse(null);
    }

    public AuthData isAuthorized(String authToken) throws DataAccessException {
        AuthData authData = authDataAccess.getAuth(authToken);
        if (authData == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }
        return authData;
    }

    public boolean isNotAuthorizedWs(String authToken) throws DataAccessException {
        AuthData authData = authDataAccess.getAuth(authToken);
        return authData == null;
    }
}
