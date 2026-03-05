package pl.edu.wszib.library.config;

import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {

    public static void initialize() {
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement()) {

            createTables(stmt);
            insertInitialData(stmt);
            System.out.println("Baza danych została zainicjalizowana pomyślnie.");

        } catch (SQLException e) {
            System.err.println("Błąd podczas inicjalizacji bazy danych: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void createTables(Statement stmt) throws SQLException {
        stmt.execute("""
            CREATE TABLE IF NOT EXISTS users (
                id INT AUTO_INCREMENT PRIMARY KEY,
                username VARCHAR(50) NOT NULL UNIQUE,
                password VARCHAR(255) NOT NULL,
                role VARCHAR(20) NOT NULL
            )
        """);

        stmt.execute("""
            CREATE TABLE IF NOT EXISTS categories (
                id INT AUTO_INCREMENT PRIMARY KEY,
                name VARCHAR(100) NOT NULL UNIQUE
            )
        """);

        stmt.execute("""
            CREATE TABLE IF NOT EXISTS books (
                id INT AUTO_INCREMENT PRIMARY KEY,
                title VARCHAR(255) NOT NULL,
                author VARCHAR(255) NOT NULL,
                isbn VARCHAR(50),
                release_year INT,
                available BOOLEAN DEFAULT TRUE,
                category_id INT,
                FOREIGN KEY (category_id) REFERENCES categories(id)
            )
        """);

        stmt.execute("""
            CREATE TABLE IF NOT EXISTS borrowings (
                id INT AUTO_INCREMENT PRIMARY KEY,
                user_id INT NOT NULL,
                book_id INT NOT NULL,
                borrow_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                return_date TIMESTAMP,
                returned BOOLEAN DEFAULT FALSE,
                FOREIGN KEY (user_id) REFERENCES users(id),
                FOREIGN KEY (book_id) REFERENCES books(id)
            )
        """);
    }

    private static void insertInitialData(Statement stmt) throws SQLException {
        var rs = stmt.executeQuery("SELECT COUNT(*) FROM users");
        rs.next();
        if (rs.getInt(1) > 0) {
            return;
        }

        String adminPassword = BCrypt.hashpw("admin123", BCrypt.gensalt());
        String userPassword = BCrypt.hashpw("user123", BCrypt.gensalt());
        String user2Password = BCrypt.hashpw("user2123", BCrypt.gensalt());

        stmt.execute(String.format("""
            INSERT INTO users (username, password, role) VALUES
            ('admin', '%s', 'ADMIN'),
            ('user', '%s', 'USER'),
            ('user2', '%s', 'USER')
        """, adminPassword, userPassword, user2Password));

        stmt.execute("""
            INSERT INTO categories (name) VALUES
            ('Dystopia'),
            ('Fantasy'),
            ('Klasyka'),
            ('Kryminał'),
            ('Literatura dziecięca'),
            ('Science Fiction')
        """);

        stmt.execute("""
            INSERT INTO books (title, author, isbn, release_year, available, category_id) VALUES
            ('Wiedzmin', 'Andrzej Sapkowski', '978-83-7578-001-1', 1986, TRUE, 1),
            ('Solaris', 'Stanislaw Lem', '978-83-7578-002-2', 1961, TRUE, 2),
            ('Ferdydurke', 'Witold Gombrowicz', '978-83-7578-003-3', 1937, TRUE, 3),
            ('Hobbit', 'J.R.R. Tolkien', '978-83-7578-004-4', 1937, TRUE, 1),
            ('Dune', 'Frank Herbert', '978-83-7578-005-5', 1965, TRUE, 2),
            ('Rok 1984', 'George Orwell', '978-83-7578-006-6', 1949, TRUE, 4),
            ('Nowy wspanialy swiat', 'Aldous Huxley', '978-83-7578-007-7', 1932, TRUE, 4),
            ('Harry Potter i Kamien Filozoficzny', 'J.K. Rowling', '978-83-7578-008-8', 1997, TRUE, 1),
            ('Wladca Pierscieni', 'J.R.R. Tolkien', '978-83-7578-009-9', 1954, TRUE, 1),
            ('Zbrodnia i kara', 'Fiodor Dostojewski', '978-83-7578-010-0', 1866, TRUE, 3),
            ('Fahrenheit 451', 'Ray Bradbury', '978-83-7578-011-1', 1953, TRUE, 4),
            ('Maly Ksiaze', 'Antoine de Saint-Exupery', '978-83-7578-012-2', 1943, TRUE, 6),
            ('Gra o Tron', 'George R.R. Martin', '978-83-7578-013-3', 1996, TRUE, 1)
        """);
    }
}
