package service;

import dataaccess.*;
import dataaccess.Exceptions.AlreadyTakenException;
import dataaccess.Exceptions.BadRequestException;
import dataaccess.Exceptions.DataAccessException;
import dataaccess.Exceptions.UnauthorizedException;
import model.UserData;
import request.LoginRequest;
import request.RegisterRequest;
import response.RegisterResponse;

public class UserService {
    private final UserDAO userDataAccess;
    private final AuthService authService;


    public UserService(UserDAO userDataAccess, AuthService authService) {
        this.userDataAccess = userDataAccess;
        this.authService = authService;
    }

    public void clearUsers() throws DataAccessException {
        userDataAccess.clearUsers();
    }

    public RegisterResponse register(RegisterRequest request) throws DataAccessException {
        if (request.username() == null || request.password() == null || request.email() == null) {
            throw new BadRequestException("Error: bad request");
        }

        UserData user = userDataAccess.getUser(request.username());

        if (user != null) {
            throw new AlreadyTakenException("Error: already taken");
        }

        userDataAccess.createUser(request.username(), request.password(), request.email());
        return authService.register(request.username());
    }

    public void login(LoginRequest request) throws DataAccessException {
        if (!userDataAccess.findUser(request.username(), request.password())) {
            throw new UnauthorizedException("Error: unauthorized");
        }
    }
}
