package pl.edu.wszib.library.database;

import pl.edu.wszib.library.model.Book;

import java.util.List;

public interface BookRepository {
    List<Book> getAll();
    List<Book> findByTitle(String title);
    List<Book> findByAuthor(String author);
    List<Book> findByCategory(int categoryId);
    Book findById(int id);
    Book save(Book book);
    void deleteById(int id);
}
