# Terminalowy Menedżer Biblioteki

Konsolowa aplikacja Java do zarządzania biblioteką, z podziałem na role użytkowników (ADMIN / USER).

## Funkcjonalności

### Użytkownik (USER)
- Logowanie / wylogowanie
- Przeglądanie listy książek
- Wyszukiwanie książek po autorze lub tytule
- Wypożyczanie i zwracanie książek
- Rezerwowanie książek

### Administrator (ADMIN)
- Wszystkie funkcje użytkownika
- Dodawanie nowej książki
- Usuwanie książki
- Edytowanie istniejącej książki

## Technologie
- **Java 17**
- **Maven** – zarządzanie projektem i zależnościami
- **Lombok** – redukcja boilerplate'u (gettery, settery, builder itp.)
- **MD5 hashing** – hashowanie haseł
- **Dependency Injection** – ręczne wstrzykiwanie zależności przez konstruktor (interfejsy + implementacje)

## Architektura

Dane przechowywane są w pamięci (listy `ArrayList`) – symulacja bazy danych.

```
src/main/java/pl/edu/wszib/library/
├── LibraryApplication.java          # punkt wejścia, wiring DI
├── model/                           # encje: User, Book, Rental, Reservation, Role
├── repository/                      # interfejsy repozytoriów (dostęp do danych)
├── service/                         # interfejsy serwisów (logika biznesowa)
├── configuration/                   # DataInitializer – seed danych startowych
├── authentication/                  # (impl) logika logowania
└── menu/                            # (impl) obsługa menu w terminalu
```

## Domyślne konta

| Login   | Hasło   | Rola  |
|---------|---------|-------|
| `admin` | `admin` | ADMIN |
| `user`  | `user`  | USER  |

## Uruchomienie

```bash
mvn clean compile exec:java -Dexec.mainClass="pl.edu.wszib.library.LibraryApplication"
```

lub:

```bash
mvn clean package
java -jar target/library-manager-1.0-SNAPSHOT.jar
```

## Status projektu

- [x] Struktura projektu (Maven, pom.xml)
- [x] Modele danych (User, Book, Rental, Reservation)
- [x] Interfejsy repozytoriów
- [x] Interfejsy serwisów
- [x] DataInitializer (seed danych)
- [ ] Implementacje repozytoriów (in-memory)
- [ ] Implementacja hashowania haseł (MD5)
- [ ] Implementacja serwisu autentykacji
- [ ] Implementacja serwisów książek, wypożyczeń, rezerwacji
- [ ] Menu terminala (UI)
- [ ] Wiring DI w LibraryApplication
