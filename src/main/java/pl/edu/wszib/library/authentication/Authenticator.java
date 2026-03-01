package pl.edu.wszib.library.authentication;

import pl.edu.wszib.library.database.UserRepository;
import pl.edu.wszib.library.model.User;

public class Authenticator {

    private final UserRepository userRepository;

    public Authenticator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User authenticate(String username, String password) {
        User user = userRepository.findByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }
}
