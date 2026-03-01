package pl.edu.wszib.library.gui;

import pl.edu.wszib.library.model.Book;
import pl.edu.wszib.library.model.User;

import java.util.List;
import java.util.Scanner;

public class GUI {

    private final Scanner scanner = new Scanner(System.in);

    public User readLoginAndPassword() {
        System.out.print("Login: ");
        String username = scanner.nextLine();
        System.out.print("Hasło: ");
        String password = scanner.nextLine();
        return new User(0, username, password, null);
    }

    public String showUserMenuAndReadChoice() {
        System.out.println("\nMENU UŹYTKOWNIKA");
        System.out.println("1. Przeglądaj wszystkie książki");
        System.out.println("2. Szukaj po tytule");
        System.out.println("3. Szukaj po autorze");
        System.out.println("4. Wyloguj");
        System.out.print("Wybierz opcje: ");
        return scanner.nextLine();
    }

    public String showAdminMenuAndReadChoice() {
        System.out.println("\nMENU ADMINISTRATORA");
        System.out.println("1. Przeglądaj wszystkie ksiązki");
        System.out.println("2. Dodaj ksiazke");
        System.out.println("3. Usun ksiazke");
        System.out.println("4. Edytuj ksiazke");
        System.out.println("5. Wyloguj");
        System.out.print("Wybierz opcje: ");
        return scanner.nextLine();
    }

    public String readSearchQuery(String field) {
        System.out.print("Wpisz " + field + ": ");
        return scanner.nextLine();
    }

    public Book readNewBook() {
        System.out.print("Tytuł: ");
        String title = scanner.nextLine();
        System.out.print("Autor: ");
        String author = scanner.nextLine();
        System.out.print("ISBN: ");
        String isbn = scanner.nextLine();
        System.out.print("Rok wydania: ");
        int releaseYear = Integer.parseInt(scanner.nextLine());
        return new Book(0, title, author, isbn, releaseYear, false);
    }

    public int readBookId() {
        System.out.print("Podaj ID książki: ");
        return Integer.parseInt(scanner.nextLine());
    }

    public Book readBookUpdate(Book existing) {
        System.out.println("Edytujesz: " + existing);
        System.out.print("Nowy tytuł (Enter = bez zmian): ");
        String title = scanner.nextLine();
        System.out.print("Nowy autor (Enter = bez zmian): ");
        String author = scanner.nextLine();
        System.out.print("Nowy ISBN (Enter = bez zmian): ");
        String isbn = scanner.nextLine();
        System.out.print("Nowy rok wydania (Enter = bez zmian): ");
        String yearInput = scanner.nextLine();

        return new Book(
                existing.getId(),
                title.isEmpty() ? existing.getTitle() : title,
                author.isEmpty() ? existing.getAuthor() : author,
                isbn.isEmpty() ? existing.getIsbn() : isbn,
                yearInput.isEmpty() ? existing.getReleaseYear() : Integer.parseInt(yearInput),
                existing.isRent()
        );
    }

    public void displayBooks(List<Book> books) {
        if (books.isEmpty()) {
            System.out.println("Nie znaleziono zadnych ksiazek.");
            return;
        }
        System.out.println("\n--- Lista ksiazek (" + books.size() + ") ---");
        for (Book book : books) {
            System.out.println(book);
        }
    }

    public void showMessage(String message) {
        System.out.println(message);
    }

    public void showWrongOptionMessage() {
        System.out.println("Nieprawidlowa opcja. Sprobuj ponownie.");
    }
}