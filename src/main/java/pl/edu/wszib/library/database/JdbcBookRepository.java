package pl.edu.wszib.library.database;

import pl.edu.wszib.library.config.DatabaseConfig;
import pl.edu.wszib.library.exceptions.BookNotFoundException;
import pl.edu.wszib.library.model.Book;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcBookRepository implements BookRepository {

    @Override
    public List<Book> getAll() {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                books.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.err.println("Błąd podczas pobierania książek: " + e.getMessage());
        }
        return books;
    }

    @Override
    public List<Book> findByTitle(String title) {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books WHERE LOWER(title) LIKE LOWER(?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + title + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    books.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Błąd podczas wyszukiwania: " + e.getMessage());
        }
        return books;
    }

    @Override
    public List<Book> findByAuthor(String author) {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books WHERE LOWER(author) LIKE LOWER(?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + author + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    books.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Błąd podczas wyszukiwania: " + e.getMessage());
        }
        return books;
    }

    @Override
    public List<Book> findByCategory(int categoryId) {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books WHERE category_id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, categoryId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    books.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Błąd podczas wyszukiwania: " + e.getMessage());
        }
        return books;
    }

    @Override
    public Book findById(int id) {
        String sql = "SELECT * FROM books WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Błąd podczas wyszukiwania: " + e.getMessage());
        }
        throw new BookNotFoundException("Nie znaleziono książki o id: " + id);
    }

    @Override
    public Book save(Book book) {
        if (book.getId() == 0) {
            return insert(book);
        } else {
            return update(book);
        }
    }

    private Book insert(Book book) {
        String sql = "INSERT INTO books (title, author, isbn, release_year, available, category_id) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, book.getTitle());
            ps.setString(2, book.getAuthor());
            ps.setString(3, book.getIsbn());
            ps.setInt(4, book.getReleaseYear());
            ps.setBoolean(5, !book.isRent()); // available = !rent
            if (book.getCategoryId() != null) {
                ps.setInt(6, book.getCategoryId());
            } else {
                ps.setNull(6, Types.INTEGER);
            }
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    book.setId(keys.getInt(1));
                }
            }
        } catch (SQLException e) {
            System.err.println("Błąd podczas dodawania książki: " + e.getMessage());
        }
        return book;
    }

    private Book update(Book book) {
        String sql = "UPDATE books SET title = ?, author = ?, isbn = ?, release_year = ?, available = ?, category_id = ? WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, book.getTitle());
            ps.setString(2, book.getAuthor());
            ps.setString(3, book.getIsbn());
            ps.setInt(4, book.getReleaseYear());
            ps.setBoolean(5, !book.isRent()); // available = !rent
            if (book.getCategoryId() != null) {
                ps.setInt(6, book.getCategoryId());
            } else {
                ps.setNull(6, Types.INTEGER);
            }
            ps.setInt(7, book.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Błąd podczas aktualizacji książki: " + e.getMessage());
        }
        return book;
    }

    @Override
    public void deleteById(int id) {
        String sql = "DELETE FROM books WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            int affected = ps.executeUpdate();
            if (affected == 0) {
                throw new BookNotFoundException("Nie znaleziono książki o id: " + id);
            }
        } catch (SQLException e) {
            System.err.println("Błąd podczas usuwania książki: " + e.getMessage());
        }
    }

    private Book mapRow(ResultSet rs) throws SQLException {
        int categoryIdVal = rs.getInt("category_id");
        boolean categoryNull = rs.wasNull();
        return new Book(
                rs.getInt("id"),
                rs.getString("title"),
                rs.getString("author"),
                rs.getString("isbn"),
                rs.getInt("release_year"),
                !rs.getBoolean("available"), // rent = !available
                categoryNull ? null : categoryIdVal
        );
    }
}
