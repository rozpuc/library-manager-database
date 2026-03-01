package pl.edu.wszib.library;

import pl.edu.wszib.library.authentication.Authenticator;
import pl.edu.wszib.library.database.BookRepository;
import pl.edu.wszib.library.database.InMemoryBookRepository;
import pl.edu.wszib.library.database.InMemoryUserRepository;
import pl.edu.wszib.library.database.UserRepository;
import pl.edu.wszib.library.gui.GUI;
import pl.edu.wszib.library.model.User;

public class App {

    public static void main(String[] args) {
        UserRepository userRepository = new InMemoryUserRepository();
        BookRepository bookRepository = new InMemoryBookRepository();
        Authenticator authenticator = new Authenticator(userRepository);
        GUI gui = new GUI();

        System.out.println("Terminalowy Menedzer Biblioteki");

        // Login
        User credentials = gui.readLoginAndPassword();
        User loggedUser = authenticator.authenticate(credentials.getUsername(), credentials.getPassword());

        if (loggedUser == null) {
            gui.showMessage("Nieprawidłowy login lub hasło.");
            return;
        }

        gui.showMessage("Zalogowano jako: " + loggedUser.getUsername() + " [" + loggedUser.getRole() + "]");

        // Menu loop
        boolean running = true;
        while (running) {
            String choice = gui.showUserMenuAndReadChoice();
            switch (choice) {
                case "1":
                    gui.displayBooks(bookRepository.getAll());
                    break;
                case "2":
                    gui.displayBooks(bookRepository.findByTitle(gui.readSearchQuery("tytuł")));
                    break;
                case "3":
                    gui.displayBooks(bookRepository.findByAuthor(gui.readSearchQuery("autora")));
                    break;
                case "4":
                    running = false;
                    break;
                default:
                    gui.showWrongOptionMessage();
            }
        }

        gui.showMessage("Do zobaczenia!");
    }
}
