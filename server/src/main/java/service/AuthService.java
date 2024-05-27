package service;

import dataaccess.AuthDAO;

public class AuthService {
    private final AuthDAO authDataAccess;


    public AuthService() {
        authDataAccess = new AuthDAO();
    }
}
