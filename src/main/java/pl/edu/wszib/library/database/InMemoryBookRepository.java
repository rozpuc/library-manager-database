package pl.edu.wszib.library.database;

import pl.edu.wszib.library.exceptions.BookNotFoundException;
import pl.edu.wszib.library.model.Book;

import java.util.ArrayList;
import java.util.List;

public class InMemoryBookRepository implements BookRepository {

    private final List<Book> books = new ArrayList<>();
    private int nextId = 1;

    public InMemoryBookRepository() {
        books.add(new Book(nextId++, "Wiedzmin", "Andrzej Sapkowski", "2001", 1986, false));
        books.add(new Book(nextId++, "Solaris", "Stanislaw Lem", "2002", 1961, false));
        books.add(new Book(nextId++, "Ferdydurke", "Witold Gombrowicz", "2003", 1937, false));
        books.add(new Book(nextId++, "Hobbit", "J.R.R. Tolkien", "2004", 1937, false));
        books.add(new Book(nextId++, "Dune", "Frank Herbert", "2005", 1965, false));
        books.add(new Book(nextId++, "Rok 1984", "George Orwell", "2006", 1949, false));
        books.add(new Book(nextId++, "Nowy wspanialy swiat", "Aldous Huxley", "2007", 1932, false));
        books.add(new Book(nextId++, "Harry Potter i Kamien Filozoficzny", "J.K. Rowling", "2008", 1997, false));
        books.add(new Book(nextId++, "Wladca Pierscieni", "J.R.R. Tolkien", "2009", 1954, false));
        books.add(new Book(nextId++, "Zbrodnia i kara", "Fiodor Dostojewski", "2010", 1866, false));
        books.add(new Book(nextId++, "Fahrenheit 451", "Ray Bradbury", "2011", 1953, false));
        books.add(new Book(nextId++, "Maly Ksiaze", "Antoine de Saint-Exupery", "2012", 1943, false));
        books.add(new Book(nextId++, "Gra o Tron", "George R.R. Martin", "2013", 1996, false));
    }

    @Override
    public List<Book> getAll() {
        return new ArrayList<>(books);
    }

    @Override
    public List<Book> findByTitle(String title) {
        List<Book> result = new ArrayList<>();
        for (Book book : books) {
            if (book.getTitle().toLowerCase().contains(title.toLowerCase())) {
                result.add(book);
            }
        }
        return result;
    }

    @Override
    public List<Book> findByAuthor(String author) {
        List<Book> result = new ArrayList<>();
        for (Book book : books) {
            if (book.getAuthor().toLowerCase().contains(author.toLowerCase())) {
                result.add(book);
            }
        }
        return result;
    }

    @Override
    public Book findById(int id) {
        for (Book book : books) {
            if (book.getId() == id) {
                return book;
            }
        }
        throw new BookNotFoundException("Nie znaleziono ksiazki o id: " + id);
    }

    @Override
    public Book save(Book book) {
        if (book.getId() == 0) {
            book.setId(nextId++);
            books.add(book);
        } else {
            for (int i = 0; i < books.size(); i++) {
                if (books.get(i).getId() == book.getId()) {
                    books.set(i, book);
                    return book;
                }
            }
        }
        return book;
    }

    @Override
    public void deleteById(int id) {
        boolean removed = books.removeIf(book -> book.getId() == id);
        if (!removed) {
            throw new BookNotFoundException("Nie znaleziono ksiazki o id: " + id);
        }
    }
}
