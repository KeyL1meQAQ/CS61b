package byog.Core;

import java.util.ArrayList;
import java.util.List;

/**
 * The class of room, the rooms are assumed to be rectangle.
 */
public class Room {

    /**
     * The start position(Left-Bottom point) of the room.
     */
    private Position startPosition;

    /**
     * The end position(Right-Top point) of the room.
     */
    private Position endPosition;

    /**
     * The position of for corners of the room.
     */
    private Position[] corners = new Position[4];

    public Position[] getCorners() {
        return corners;
    }

    private List<Integer> doorAllowedSides = new ArrayList<>();

    public void setDoorAllowedSides(List<Integer> doorAllowedSides) {
        this.doorAllowedSides = doorAllowedSides;
    }

    public List<Integer> getDoorAllowedSides() {
        return doorAllowedSides;
    }

    /**
     * CONSTRUCTOR
     */
    public Room(Position startPosition, Position endPosition) {
        this.startPosition = startPosition;
        int sXPosition = startPosition.getXPosition();
        int sYPosition = startPosition.getYPosition();
        int eXPosition = endPosition.getXPosition();
        int eYPosition = endPosition.getYPosition();
        corners[0] = startPosition;
        corners[1] = new Position(eXPosition, sYPosition);
        corners[2] = new Position(sXPosition, eYPosition);
        corners[3] = endPosition;
    }
}
