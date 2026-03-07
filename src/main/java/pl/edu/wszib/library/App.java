package pl.edu.wszib.library;

import pl.edu.wszib.library.authentication.Authenticator;
import pl.edu.wszib.library.config.DatabaseInitializer;
import pl.edu.wszib.library.database.BookRepository;
import pl.edu.wszib.library.database.BorrowingRepository;
import pl.edu.wszib.library.database.CategoryRepository;
import pl.edu.wszib.library.database.JdbcBookRepository;
import pl.edu.wszib.library.database.JdbcBorrowingRepository;
import pl.edu.wszib.library.database.JdbcCategoryRepository;
import pl.edu.wszib.library.database.JdbcStatisticsRepository;
import pl.edu.wszib.library.database.JdbcUserRepository;
import pl.edu.wszib.library.database.StatisticsRepository;
import pl.edu.wszib.library.database.UserRepository;
import pl.edu.wszib.library.exceptions.BookNotFoundException;
import pl.edu.wszib.library.gui.GUI;
import pl.edu.wszib.library.model.User;
import pl.edu.wszib.library.model.Book;
import pl.edu.wszib.library.model.Category;
import pl.edu.wszib.library.model.Role;

public class App {

    public static void main(String[] args) {
        DatabaseInitializer.initialize();

        UserRepository userRepository = new JdbcUserRepository();
        BookRepository bookRepository = new JdbcBookRepository();
        BorrowingRepository borrowingRepository = new JdbcBorrowingRepository();
        CategoryRepository categoryRepository = new JdbcCategoryRepository();
        StatisticsRepository statisticsRepository = new JdbcStatisticsRepository();
        Authenticator authenticator = new Authenticator(userRepository);
        GUI gui = new GUI();

        System.out.println("Terminalowy Menedzer Biblioteki");

        User credentials = gui.readLoginAndPassword();
        User loggedUser = authenticator.authenticate(credentials.getUsername(), credentials.getPassword());

        if (loggedUser == null) {
            gui.showMessage("Nieprawidłowy login lub hasło.");
            return;
        }

        gui.showMessage("Zalogowano jako: " + loggedUser.getUsername() + " [" + loggedUser.getRole() + "]");

        if (loggedUser.getRole() == Role.ADMIN) {
            runAdminMenu(gui, bookRepository, categoryRepository, statisticsRepository);
        } else {
            runUserMenu(gui, bookRepository, borrowingRepository, categoryRepository, loggedUser);
        }

        gui.showMessage("Do zobaczenia!");
    }

    private static void runUserMenu(GUI gui, BookRepository bookRepository,
                                    BorrowingRepository borrowingRepository,
                                    CategoryRepository categoryRepository, User loggedUser) {
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
                    try {
                        gui.displayCategories(categoryRepository.getAll());
                        int categoryId = gui.readCategoryId();
                        gui.displayBooks(bookRepository.findByCategory(categoryId));
                    } catch (NumberFormatException e) {
                        gui.showMessage("Nieprawidłowe ID.");
                    }
                    break;
                case "5":
                    try {
                        gui.displayBooks(bookRepository.getAll());
                        int bookId = gui.readBookId();
                        if (borrowingRepository.borrow(loggedUser.getId(), bookId)) {
                            gui.showMessage("Książka wypożyczona pomyślnie.");
                        } else {
                            gui.showMessage("Książka jest niedostępna.");
                        }
                    } catch (NumberFormatException e) {
                        gui.showMessage("Nieprawidłowe ID.");
                    }
                    break;
                case "6":
                    try {
                        gui.displayBorrowings(borrowingRepository.getActiveBorrowingsByUser(loggedUser.getId()));
                        int borrowingId = gui.readBorrowingId();
                        borrowingRepository.returnBook(borrowingId);
                        gui.showMessage("Książka zwrócona pomyślnie.");
                    } catch (NumberFormatException e) {
                        gui.showMessage("Nieprawidłowe ID.");
                    }
                    break;
                case "7":
                    gui.displayBorrowings(borrowingRepository.getActiveBorrowingsByUser(loggedUser.getId()));
                    break;
                case "8":
                    running = false;
                    break;
                default:
                    gui.showWrongOptionMessage();
            }
        }
    }

    private static void runAdminMenu(GUI gui, BookRepository bookRepository,
                                     CategoryRepository categoryRepository,
                                     StatisticsRepository statisticsRepository) {
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
                    runCategoryMenu(gui, categoryRepository);
                    break;
                case "6":
                    gui.displayStatistics(
                            statisticsRepository.getTotalBooks(),
                            statisticsRepository.getBorrowedBooksCount(),
                            statisticsRepository.getMostPopularBooks(5),
                            statisticsRepository.getActiveUsers()
                    );
                    break;
                case "7":
                    running = false;
                    break;
                default:
                    gui.showWrongOptionMessage();
            }
        }
    }

    private static void runCategoryMenu(GUI gui, CategoryRepository categoryRepository) {
        boolean running = true;
        while (running) {
            String choice = gui.showCategoryMenuAndReadChoice();
            switch (choice) {
                case "1":
                    gui.displayCategories(categoryRepository.getAll());
                    break;
                case "2":
                    Category newCategory = new Category(0, gui.readCategoryName());
                    categoryRepository.save(newCategory);
                    gui.showMessage("Kategoria dodana pomyślnie.");
                    break;
                case "3":
                    try {
                        gui.displayCategories(categoryRepository.getAll());
                        categoryRepository.deleteById(gui.readCategoryId());
                        gui.showMessage("Kategoria usunięta pomyślnie.");
                    } catch (NumberFormatException e) {
                        gui.showMessage("Nieprawidłowe ID.");
                    }
                    break;
                case "4":
                    running = false;
                    break;
                default:
                    gui.showWrongOptionMessage();
            }
        }
    }
}
