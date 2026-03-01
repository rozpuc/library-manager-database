package pl.edu.wszib.library.database;

import pl.edu.wszib.library.model.Role;
import pl.edu.wszib.library.model.User;

import java.util.ArrayList;
import java.util.List;

public class InMemoryUserRepository implements UserRepository {

    private final List<User> users = new ArrayList<>();

    public InMemoryUserRepository() {
        users.add(new User(1, "admin", "admin123", Role.ADMIN));
        users.add(new User(2, "user", "user123", Role.USER));
    }

    @Override
    public User findByUsername(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(users);
    }
}
