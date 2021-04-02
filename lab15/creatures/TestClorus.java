package creatures;

import static org.junit.Assert.*;

import huglife.Direction;
import huglife.Occupant;
import huglife.Impassible;
import huglife.Empty;
import huglife.Action;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class TestClorus {

    @Test
    public void testName() {
        Clorus clorus = new Clorus(1.2);
        String name = clorus.name();
        assertEquals(name, "clorus");
    }

    @Test
    public void testAction() {
        Clorus clorus = new Clorus(0.8);
        Map<Direction, Occupant> surround = new HashMap<>();
        surround.put(Direction.TOP, new Impassible());
        surround.put(Direction.BOTTOM, new Impassible());
        surround.put(Direction.LEFT, new Empty());
        surround.put(Direction.RIGHT, new Impassible());
        assertEquals(new Action(Action.ActionType.MOVE, Direction.LEFT),
                clorus.chooseAction(surround));
    }
}
