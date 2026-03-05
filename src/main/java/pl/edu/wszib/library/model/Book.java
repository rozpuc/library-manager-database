package pl.edu.wszib.library.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    private int id;
    private String title;
    private String author;
    private String isbn;
    private int releaseYear;
    private boolean rent;
    private Integer categoryId;

    @Override
    public String toString() {
        return String.format("[%d] \"%s\" - %s (%d) | ISBN: %s | %s",
                id, title, author, releaseYear, isbn, rent ? "WYPOŻYCZONA" : "DOSTĘPNA");
    }
}
