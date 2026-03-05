package pl.edu.wszib.library.database;

import pl.edu.wszib.library.config.DatabaseConfig;
import pl.edu.wszib.library.model.Borrowing;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcBorrowingRepository implements BorrowingRepository {

    @Override
    public boolean borrow(int userId, int bookId) {
        String checkSql = "SELECT available FROM books WHERE id = ?";
        String insertSql = "INSERT INTO borrowings (user_id, book_id) VALUES (?, ?)";
        String updateSql = "UPDATE books SET available = FALSE WHERE id = ?";

        try (Connection conn = DatabaseConfig.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(checkSql)) {
                ps.setInt(1, bookId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next() || !rs.getBoolean("available")) {
                        return false;
                    }
                }
            }

            try (PreparedStatement ps = conn.prepareStatement(insertSql)) {
                ps.setInt(1, userId);
                ps.setInt(2, bookId);
                ps.executeUpdate();
            }

            try (PreparedStatement ps = conn.prepareStatement(updateSql)) {
                ps.setInt(1, bookId);
                ps.executeUpdate();
            }

            return true;
        } catch (SQLException e) {
            System.err.println("Błąd podczas wypożyczania: " + e.getMessage());
            return false;
        }
    }

    @Override
    public void returnBook(int borrowingId) {
        String selectSql = "SELECT book_id FROM borrowings WHERE id = ? AND returned = FALSE";
        String updateBorrowingSql = "UPDATE borrowings SET returned = TRUE, return_date = CURRENT_TIMESTAMP WHERE id = ?";
        String updateBookSql = "UPDATE books SET available = TRUE WHERE id = ?";

        try (Connection conn = DatabaseConfig.getConnection()) {
            int bookId;

            try (PreparedStatement ps = conn.prepareStatement(selectSql)) {
                ps.setInt(1, borrowingId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        System.out.println("Nie znaleziono aktywnego wypożyczenia o id: " + borrowingId);
                        return;
                    }
                    bookId = rs.getInt("book_id");
                }
            }

            try (PreparedStatement ps = conn.prepareStatement(updateBorrowingSql)) {
                ps.setInt(1, borrowingId);
                ps.executeUpdate();
            }

            try (PreparedStatement ps = conn.prepareStatement(updateBookSql)) {
                ps.setInt(1, bookId);
                ps.executeUpdate();
            }

        } catch (SQLException e) {
            System.err.println("Błąd podczas zwracania: " + e.getMessage());
        }
    }

    @Override
    public List<Borrowing> getActiveBorrowingsByUser(int userId) {
        List<Borrowing> borrowings = new ArrayList<>();
        String sql = """
            SELECT br.id, br.user_id, br.book_id, b.title, br.borrow_date, br.return_date, br.returned
            FROM borrowings br
            JOIN books b ON br.book_id = b.id
            WHERE br.user_id = ? AND br.returned = FALSE
        """;

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    borrowings.add(new Borrowing(
                            rs.getInt("id"),
                            rs.getInt("user_id"),
                            rs.getInt("book_id"),
                            rs.getString("title"),
                            rs.getTimestamp("borrow_date").toLocalDateTime(),
                            rs.getTimestamp("return_date") != null ? rs.getTimestamp("return_date").toLocalDateTime() : null,
                            rs.getBoolean("returned")
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("Błąd podczas pobierania wypożyczeń: " + e.getMessage());
        }
        return borrowings;
    }
}
