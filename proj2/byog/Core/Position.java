package byog.Core;

/**
 * This class indicates a position in the map.
 */
public class Position {

    /**
     * The x position of the point.
     */
    private int xPosition;

    /**
     * The y position of the point.
     */
    private int yPosition;

    /**
     * Getter of xPosition
     */
    public int getXPosition() {
        return xPosition;
    }

    /**
     * Getter of yPosition
     */
    public int getYPosition() {
        return yPosition;
    }

    /**
     * CONSTRUCTOR
     */
    public Position(int xPosition, int yPosition) {
        this.xPosition = xPosition;
        this.yPosition = yPosition;
    }
}
