package go.project.server.base;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import go.project.server.game.base.Board;
import go.project.server.game.base.Color;

public class BoardTest {

    @Test
    public void testInitialization() {
        Board board = new Board(19);
        assertEquals(19, board.getSize());
        Color[][] grid = board.getGrid();

        for (int y = 0; y < 19; y++) {
            for (int x = 0; x < 19; x++) {
                assertEquals(Color.NONE, grid[y][x]);
            }
        }
    }
}
