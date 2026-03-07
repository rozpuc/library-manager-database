package pl.edu.wszib.library.database;

import java.util.List;

public interface StatisticsRepository {
    int getTotalBooks();
    int getBorrowedBooksCount();
    List<String> getMostPopularBooks(int limit);
    List<String> getActiveUsers();
}
