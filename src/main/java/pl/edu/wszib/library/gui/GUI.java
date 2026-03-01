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
        System.out.print("Haslo: ");
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

    public String readSearchQuery(String field) {
        System.out.print("Wpisz " + field + ": ");
        return scanner.nextLine();
    }

    public void displayBooks(List<Book> books) {
        if (books.isEmpty()) {
            System.out.println("Nie znaleziono żadnych książek.");
            return;
        }
        System.out.println("\n--- Lista książek (" + books.size() + ") ---");
        for (Book book : books) {
            System.out.println(book);
        }
    }

    public void showMessage(String message) {
        System.out.println(message);
    }

    public void showWrongOptionMessage() {
        System.out.println("Nieprawidłowa opcja. Spróbuj ponownie.");
    }
}
