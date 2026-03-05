package pl.edu.wszib.library.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Borrowing {
    private int id;
    private int userId;
    private int bookId;
    private String bookTitle;
    private LocalDateTime borrowDate;
    private LocalDateTime returnDate;
    private boolean returned;

    @Override
    public String toString() {
        return String.format("[%d] \"%s\" | Wypożyczono: %s",
                id, bookTitle, borrowDate.toLocalDate());
    }
}
