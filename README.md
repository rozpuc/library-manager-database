# Terminalowy Menedżer Biblioteki z Bazą Danych

Konsolowa aplikacja Java do zarządzania biblioteką z bazą danych H2 przez JDBC, z podziałem na role użytkowników (ADMIN / USER).

## Wymagania

- Java 17+
- Maven 3.6+

## Uruchomienie (IntelliJ IDEA)

1. Otwórz projekt w IntelliJ IDEA.
2. Czekaj na pobranie zależności Mavena (pasek postępu w prawym dolnym rogu).
3. W lewym panelu przejdź do: src/main/java/pl/edu/wszib/library/App.java
4. Kliknij prawym przyciskiem na App.java → *Run 'App.main()'*

Baza danych zostanie utworzona automatycznie przy pierwszym uruchomieniu.

## Konfiguracja bazy danych

Projekt korzysta z wbudowanej bazy **H2** (plik lokalny). Konfiguracja znajduje się w klasie `DatabaseConfig`:

| Parametr | Wartość |
|----------|---------|
| URL | `jdbc:h2:./library_db;DB_CLOSE_DELAY=-1` |
| Użytkownik | `sa` |
| Hasło | *(brak)* |

Schemat bazy danych (tabele) tworzony jest automatycznie przez klasę `DatabaseInitializer` przy starcie aplikacji. Skrypt SQL ze schematem dostępny jest również w pliku `src/main/resources/schema.sql`.

## Funkcjonalności

### Użytkownik (USER)
- Logowanie / wylogowanie
- Przeglądanie listy książek
- Wyszukiwanie książek po tytule lub autorze
- Filtrowanie książek według kategorii
- Wypożyczanie książek
- Zwracanie książek
- Podgląd aktywnych wypożyczeń

### Administrator (ADMIN)
- Logowanie / wylogowanie
- Przeglądanie listy książek
- Dodawanie, edytowanie i usuwanie książek
- Zarządzanie kategoriami (dodawanie, usuwanie)
- Statystyki biblioteki (liczba książek, wypożyczenia, najpopularniejsze tytuły, aktywni użytkownicy)

## Domyślne konta

| Login   | Hasło   | Rola  |
|---------|---------|-------|
| `admin` | `admin123` | ADMIN |
| `user`  | `user123`  | USER  |
| `user2` | `user2123` | USER  |

## Struktura projektu

```
src/main/java/pl/edu/wszib/library/
├── App.java                        
├── authentication/
│   └── Authenticator.java          
├── config/
│   ├── DatabaseConfig.java         
│   └── DatabaseInitializer.java    
├── database/
│   ├── BookRepository.java / JdbcBookRepository.java
│   ├── UserRepository.java / JdbcUserRepository.java
│   ├── BorrowingRepository.java / JdbcBorrowingRepository.java
│   ├── CategoryRepository.java / JdbcCategoryRepository.java
│   └── StatisticsRepository.java / JdbcStatisticsRepository.java
├── exceptions/
│   └── BookNotFoundException.java
├── gui/
│   └── GUI.java                    
└── model/
    ├── Book.java
    ├── Borrowing.java
    ├── Category.java
    ├── Role.java
    └── User.java
src/main/resources/
└── schema.sql                      
```