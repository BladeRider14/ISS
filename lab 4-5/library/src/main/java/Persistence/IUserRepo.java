package Persistence;

import Model.User;
import Persistence.Exception.RepositoryException;

public interface IUserRepo {
    User findUserByUsername(String username) throws RepositoryException;
}
