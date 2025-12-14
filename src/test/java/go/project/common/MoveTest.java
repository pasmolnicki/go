package go.project.common;

import org.junit.Test;

import go.project.common.Color;
import go.project.common.Move;

public class MoveTest {
    
    static private enum FailReason {
        None,
        NumberFmt,
        IllegalArg,
    };

    @Test
    public void testMoveParse() {
        String moves[] = {
            "1212", "0108", "1919", "0000", 
            "2020", "0123", "nice", "pass", 
            "", "123", "a123", "0012"
        };
        Move validMoves[] = {
            new Move((short)11, (short)11, Color.NONE),
            new Move((short)0, (short)7, Color.NONE),
            new Move((short)18, (short)18, Color.NONE),
            new Move((short)-1, (short)-1, Color.NONE),
        };

        FailReason expectedResults[] = {
            FailReason.None, FailReason.None, FailReason.None, FailReason.None, 
            FailReason.IllegalArg, FailReason.IllegalArg, FailReason.NumberFmt, FailReason.NumberFmt,
            FailReason.IllegalArg, FailReason.IllegalArg, FailReason.NumberFmt, FailReason.IllegalArg
        };

        for (int i = 0; i < moves.length; i++) {
            String mvStr = moves[i];
            FailReason expected = expectedResults[i];
            try {
                Move m = Move.parseMove(mvStr, 19, Color.NONE);
                if (m == null) {
                    throw new AssertionError("Parsed move is null for move: " + mvStr);
                }

                if (m.getX() != validMoves[i].getX() ||
                    m.getY() != validMoves[i].getY()) {
                    throw new AssertionError("Parsed move does not match expected for move: " + mvStr);
                }

                if (expected != FailReason.None) {
                    throw new AssertionError("Expected failure for move: " + mvStr);
                }
            } catch (NumberFormatException nfe) {
                if (expected != FailReason.NumberFmt) {
                    throw new AssertionError("Unexpected NumberFormatException for move: " + mvStr);
                }
            } catch (IllegalArgumentException iae) {
                if (expected != FailReason.IllegalArg) {
                    throw new AssertionError("Unexpected IllegalArgumentException for move: " + mvStr);
                }
            }  
        }
    }
}
