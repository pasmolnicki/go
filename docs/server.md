
# Konstrukcja [`server`](../src/main/java/go/project/server/server/)

## [`Server`](../src/main/java/go/project/server/server/Server.java)

Jedynie nasłuchuje na przychodzące połączenia, następnie deleguje je do `ClientPool`

## [`ClientPool`](../src/main/java/go/project/server/server/ClientPool.java)

Trzyma 'ThreadPool' oraz `ClientManager`, odbiera połączenia od serwera i dodanie nowe zadanie w thread pool'u - co uruchamia `ClientHandler`

## [`ClientHandler`](../src/main/java/go/project/server/server/ClientHandler.java)

Uruchamiany tuż po połączeniu użytkownika, wysyła id gracza i czeka, aż zostanie przydzielony do meczu.

Zwracany format json:
[`Connection`](../src/main/java/go/project/common/json/Connection.java)

## [`ClientManager`](../src/main/java/go/project/server/server/ClientManager.java)

Trzyma wszystkie obiekty `ClientHandler` i umożliwia sprawdzenie czy można stworzyć mecz

## [`MatchMaker`](../src/main/java/go/project/server/server/MatchMaker.java)

Działa jako oddzielny wątek (w ramach [`MatchMakerThread`](../src/main/java/go/project/server/server/MatchMakerThread.java)), cyklicznie sprawdza, czy są oczekujący klienci, a następnie, przy wystarczającej ilości, łączy ich w pary w ramach meczu (`Match`)

## [`Match`](../src/main/java/go/project/server/server/Match.java)

Stworzony po połączeniu 2 graczy. 

Zwracany jest do klientów kolor dla obydwóch graczy i zaczyna czarny

Całą obsługę komend impelemntuje `Match.GameThread`

## [`Match.GameThread`](../src/main/java/go/project/server/server/Match.java)

Obsługuje zapytania użytkownia podczas gry (tzn. komendy). Używa do tego formatu [`GameCommand<T>`](../src/main/java/go/project/common/json/GameCommand.java)

Umożliwia odsyłanie odpowiedzi, nawet gdy nie jest tura użytkownika

## [`MatchManager`](../src/main/java/go/project/server/server/MatchManager.java)

Trzyma wszystkie obiekty `Match`, umożliwia dodanie nowego meczu

## [`Logger`](../src/main/java/go/project/server/server/Logger.java)
Globaly logger (co tu więcej dodawać?)

## [`MockClient`](../src/test/java/go/project/server/MockClient.java)

Testowy klient, minimalna implementacja klienta