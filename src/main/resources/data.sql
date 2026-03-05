-- Dane początkowe dla Library Manager Database

-- Użytkownicy (hasła zostaną zahashowane przez aplikację)
-- admin/admin123, user/user123
INSERT INTO users (username, password, role) VALUES
('admin', '$2a$10$placeholder_admin_hash', 'ADMIN'),
('user', '$2a$10$placeholder_user_hash', 'USER');

-- Kategorie książek
INSERT INTO categories (name) VALUES
('Fantasy'),
('Science Fiction'),
('Klasyka'),
('Dystopia'),
('Kryminał'),
('Literatura dziecięca');

-- Książki
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
('Gra o Tron', 'George R.R. Martin', '978-83-7578-013-3', 1996, TRUE, 1);
