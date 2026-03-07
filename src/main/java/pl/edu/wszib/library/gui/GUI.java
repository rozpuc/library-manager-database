package pl.edu.wszib.library.gui;

import pl.edu.wszib.library.model.Book;
import pl.edu.wszib.library.model.Borrowing;
import pl.edu.wszib.library.model.Category;
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
        System.out.println("\nMENU UŻYTKOWNIKA");
        System.out.println("1. Przeglądaj wszystkie książki");
        System.out.println("2. Szukaj po tytule");
        System.out.println("3. Szukaj po autorze");
        System.out.println("4. Filtruj po kategorii");
        System.out.println("5. Wypożycz książkę");
        System.out.println("6. Zwróć książkę");
        System.out.println("7. Moje wypożyczenia");
        System.out.println("8. Wyloguj");
        System.out.print("Wybierz opcje: ");
        return scanner.nextLine();
    }

    public String showAdminMenuAndReadChoice() {
        System.out.println("\nMENU ADMINISTRATORA");
        System.out.println("1. Przeglądaj wszystkie ksiązki");
        System.out.println("2. Dodaj książkę");
        System.out.println("3. Usuń książkę");
        System.out.println("4. Edytuj książkę");
        System.out.println("5. Zarządzaj kategoriami");
        System.out.println("6. Statystyki");
        System.out.println("7. Wyloguj");
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
        return new Book(0, title, author, isbn, releaseYear, false, null);
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
                existing.isRent(),
                existing.getCategoryId()
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

    public void displayBorrowings(List<Borrowing> borrowings) {
        if (borrowings.isEmpty()) {
            System.out.println("Brak aktywnych wypożyczeń.");
            return;
        }
        System.out.println("\nTwoje wypożyczenia (" + borrowings.size() + ")");
        for (Borrowing b : borrowings) {
            System.out.println(b);
        }
    }

    public int readBorrowingId() {
        System.out.print("Podaj ID wypożyczenia: ");
        return Integer.parseInt(scanner.nextLine());
    }

    public void displayCategories(List<Category> categories) {
        if (categories.isEmpty()) {
            System.out.println("Brak kategorii.");
            return;
        }
        System.out.println("\nKategorie (" + categories.size() + ")");
        for (Category c : categories) {
            System.out.println(c);
        }
    }

    public String showCategoryMenuAndReadChoice() {
        System.out.println("\nZARZĄDZANIE KATEGORIAMI");
        System.out.println("1. Lista kategorii");
        System.out.println("2. Dodaj kategorię");
        System.out.println("3. Usuń kategorię");
        System.out.println("4. Powrót");
        System.out.print("Wybierz opcje: ");
        return scanner.nextLine();
    }

    public String readCategoryName() {
        System.out.print("Nazwa kategorii: ");
        return scanner.nextLine();
    }

    public int readCategoryId() {
        System.out.print("Podaj ID kategorii: ");
        return Integer.parseInt(scanner.nextLine());
    }

    public void displayStatistics(int totalBooks, int borrowedBooks, List<String> popularBooks, List<String> activeUsers) {
        System.out.println("\nSTATYSTYKI BIBLIOTEKI ");
        System.out.println("Wszystkich książek: " + totalBooks);
        System.out.println("Wypożyczone: " + borrowedBooks);
        System.out.println("Dostępne: " + (totalBooks - borrowedBooks));

        System.out.println("\nNajpopularniejsze książki:");
        if (popularBooks.isEmpty()) {
            System.out.println("Brak danych.");
        } else {
            popularBooks.forEach(System.out::println);
        }

        System.out.println("\nAktywni użytkownicy:");
        if (activeUsers.isEmpty()) {
            System.out.println("Brak aktywnych wypożyczeń.");
        } else {
            activeUsers.forEach(System.out::println);
        }
    }
}