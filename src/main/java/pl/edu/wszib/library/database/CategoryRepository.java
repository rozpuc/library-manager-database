package pl.edu.wszib.library.database;

import pl.edu.wszib.library.model.Category;

import java.util.List;

public interface CategoryRepository {
    List<Category> getAll();
    Category save(Category category);
    void deleteById(int id);
}
