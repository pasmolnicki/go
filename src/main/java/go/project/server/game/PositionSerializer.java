package go.project.server.game;

// Creates serialized representations of board positions:
// a FEN-like string:
// <size> <row1>/<row2>/.../<rowN> <side to move>
// where each row is represented by characters:
// 'b' for black stone, 'w' for white stone, and digits for consecutive empty intersections
// eg. "19 b3w2b1/.../19 b" for a 19x19 board with some stones and black to move
public class PositionSerializer {
    // TODO: uzgodnić z Wiktorem jak dokładnie ma wyglądać serializacja pozycji
    // W przypadku komunikacji z klientem, by zwrócić string reprezentujący aktualny stan planszy
}
