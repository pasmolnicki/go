# TODO


Klient JSON:
```json
{
    "playerid": "",
    "command": "",
    "payload": {} // W zależności od kontekstu 
}
```

Komendy:
- "make-move"
- "connect"


Serwer JSON:
```json
{
    "status": 0 | 1, // Czy zapytanie zostało wykonane pomyślnie
    "message": "", // Ewentualna wiadomość z np. błędem
}
```

Logika aplikacji:

### Serwer

- [ ] Serwer:
    - [ ] Stawia serwer z Socketem, czeka na zapytania
    - [ ] Odbiera zapytania z Socketa, w formacie JSON

- Klasa Board:
    - Repezentacja aktualnej pozycji:
        - 2d tablica typu int - wartości 1,0,-1, gdzie 1 to biały, -1 to czarny, 0 pusty
        - tura gracza
    - Metoda do wykonywania ruchów
        - Zaimplementowane też zbijania itd.

- MoveGenerator:
    - Możliwość generacji legalnych ruchów

- Reprezentacja ruchu:
    - koordynaty ruchu
    - formatowanie: xxxx, gdzie x to cyfra gdzie xx to liczba, jeżeli xx <= 9, uyżywamy formatu to 0x

### Klient

- (abstrakcyjna) Klasa Backend:
    - Łączy całą logike zapytań od klienta, implementuje metody do zapytań


- [ ] Klient:
    - [ ] Jako użytkownik chcę się zalogować do gry
        - [ ] Używamy serweru z Socketem do zapytań, w formacie JSON (do ustalenia)
        - [ ] Połączenie z serwerem i sprawdzenie 
    - [ ] Dobrze by było mieć MENU:
        - [ ] Wyświetlenie dostępnych komend
        - [ ] Dołączenie do rozgrywki i opdowiednie komunikaty oczekiwania na rywala
        - [ ] Ograniczenie graczy do 2
        - [ ] Dowiedzieć się o zasadach gry (np. link do zasad na wikipedii)

