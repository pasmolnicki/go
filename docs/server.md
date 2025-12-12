
- `ClientHandler`:
    - gracz się właśnie dołączył, jako wartość zwrotną powienien wysłać "id" gracza i oczekiwać, ąż klasa Server przyznaczy temu graczowi grę, przy użyciu klasy `Match`
- `ClientManager`:
    - Trzyma wszystkie obiekty `ClientHandler` i ewentualnie zwraca, czy można stworzyć mecz
- `Match`:
    - Stworzony po połączeniu 2 graczy. Zwracany jest kolor dla obydwóch graczy i zaczyna czarny
    - Obsługa komend do gry (prawdopodobnie inna klasa ma się tym zając)
    - Obsługa zerwania połączenia (bezpośrednio w `Match`)
    - Obsługa zakończenia gry
- `MatchManager`:
    - Trzyma wszystkie obiekty `Match`, umożliwia dodanie nowego meczu
- `Server`:
    - Przetrzymuje mapę wszystkich gier (może być zdelegowane do innej klasy) oraz oczekujących klientów (chyba też inna klasa może się tym zająć)
    - Po znalezieniu 2 gracza, odbierane są sockety od `ClientHandler` oraz przerywany jest status oczekiwania. Tworzony jest pokój gry `Match`

- `json`: