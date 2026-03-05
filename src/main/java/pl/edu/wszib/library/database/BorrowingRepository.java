package pl.edu.wszib.library.database;

import pl.edu.wszib.library.model.Borrowing;

import java.util.List;

public interface BorrowingRepository {
    boolean borrow(int userId, int bookId);
    void returnBook(int borrowingId);
    List<Borrowing> getActiveBorrowingsByUser(int userId);
}
