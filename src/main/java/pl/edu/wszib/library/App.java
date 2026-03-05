package pl.edu.wszib.library;

import pl.edu.wszib.library.authentication.Authenticator;
import pl.edu.wszib.library.config.DatabaseInitializer;
import pl.edu.wszib.library.database.BookRepository;
import pl.edu.wszib.library.database.InMemoryBookRepository;
import pl.edu.wszib.library.database.InMemoryUserRepository;
import pl.edu.wszib.library.database.UserRepository;
import pl.edu.wszib.library.exceptions.BookNotFoundException;
import pl.edu.wszib.library.gui.GUI;
import pl.edu.wszib.library.model.User;
import pl.edu.wszib.library.model.Book;
import pl.edu.wszib.library.model.Role;

public class App {

    public static void main(String[] args) {
        DatabaseInitializer.initialize();
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

        if (loggedUser.getRole() == Role.ADMIN) {
            runAdminMenu(gui, bookRepository);
        } else {
            runUserMenu(gui, bookRepository);
        }

        gui.showMessage("Do zobaczenia!");
    }

    private static void runUserMenu(GUI gui, BookRepository bookRepository) {
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
    }

    private static void runAdminMenu(GUI gui, BookRepository bookRepository) {
        boolean running = true;
        while (running) {
            String choice = gui.showAdminMenuAndReadChoice();
            switch (choice) {
                case "1":
                    gui.displayBooks(bookRepository.getAll());
                    break;
                case "2":
                    try {
                        Book newBook = gui.readNewBook();
                        bookRepository.save(newBook);
                        gui.showMessage("Książka dodana pomyślnie.");
                    } catch (NumberFormatException e) {
                        gui.showMessage("Nieprawidłowy rok wydania.");
                    }
                    break;
                case "3":
                    try {
                        gui.displayBooks(bookRepository.getAll());
                        bookRepository.deleteById(gui.readBookId());
                        gui.showMessage("Książka usunięta pomyślnie.");
                    } catch (BookNotFoundException e) {
                        gui.showMessage(e.getMessage());
                    }
                    break;
                case "4":
                    try {
                        gui.displayBooks(bookRepository.getAll());
                        Book existing = bookRepository.findById(gui.readBookId());
                        Book updated = gui.readBookUpdate(existing);
                        bookRepository.save(updated);
                        gui.showMessage("Książka zaktualizowana pomyślnie.");
                    } catch (BookNotFoundException e) {
                        gui.showMessage(e.getMessage());
                    } catch (NumberFormatException e) {
                        gui.showMessage("Nieprawidłowy rok wydania.");
                    }
                    break;
                case "5":
                    running = false;
                    break;
                default:
                    gui.showWrongOptionMessage();
            }
        }
    }
}
