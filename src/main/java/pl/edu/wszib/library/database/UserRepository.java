package pl.edu.wszib.library.database;

import pl.edu.wszib.library.model.User;

import java.util.List;

public interface UserRepository {
    User findByUsername(String username);
    List<User> getAll();
}
