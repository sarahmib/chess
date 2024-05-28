package service;

import dataaccess.AlreadyTakenException;
import dataaccess.BadRequestException;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import model.AuthData;
import model.UserData;
import request.RegisterRequest;
import response.RegisterResponse;

public class UserService {
    private final UserDAO userDataAccess;


    public UserService(UserDAO userDataAccess) {
        this.userDataAccess = userDataAccess;
    }

    public void clearUsers() throws DataAccessException {
        userDataAccess.clearUsers();
    }

    public void register(RegisterRequest request) throws DataAccessException {
        if (request.username() == null || request.password() == null || request.email() == null) {
            throw new BadRequestException("Error: bad request");
        }

        UserData user = userDataAccess.getUser(request.username());

        if (user != null) {
            throw new AlreadyTakenException("Error: already taken");
        }

        userDataAccess.createUser(request.username(), request.password(), request.email());
    }
}
