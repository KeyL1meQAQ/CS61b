package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class WorldGenerator {

    /**
     * The nested class indicates the property of each point in the map.
     */
    private static class Point extends Position {

        /**
         * This variable indicates that whether this point is available to be the start
         * point(The Left-Bottom point) for a new room or not.
         */
        private boolean available;

        /**
         * The available end positions(Right-Top point) of this point.
         */
        private final List<Position> endPositions = new ArrayList<>();

        /**
         * The list of points that can be connected with this point.
         */
        private List<Point> canConnectPoints;

        /**
         * CONSTRUCTOR.
         */
        Point(int xPosition, int yPosition, boolean available) {
            super(xPosition, yPosition);
            this.available = available;
        }
    }

    /**
     * This array is used to save all the points in the map.
     */
    private final Point[][] points;

    /**
     * This list saves all the rooms created.
     */
    private final List<Room> rooms;

    /**
     * The random number generator.
     */
    private final Random random;

    /**
     * The map of the game that the room will be placed on.
     */
    private final TETile[][] map;

    /**
     * The width of the map.
     */
    private final int width;

    /**
     * The height of the map.
     */
    private final int height;

    /**
     * The points that is available for some processing(like place a room or a hallway)
     */
    private List<Point> available;

    /**
     * CONSTRUCTOR.
     */
    public WorldGenerator(TETile[][] map, long seed) {
        this.map = map;
        width = map.length;
        height = map[0].length;
        points = new Point[width][height];
        rooms = new ArrayList<>();
        random = new Random(seed);
        available = new LinkedList<>();
        // Calculate the points available.
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (i % 2 == 0 | j % 2 == 0) {
                    map[i][j] = Tileset.WALL;
                    points[i][j] = new Point(i, j, false);
                } else {
                    map[i][j] = Tileset.NOTHING;
                    if (i == width - 2 | j == height - 2) {
                        points[i][j] = new Point(i, j, false);
                    } else {
                        points[i][j] = new Point(i, j, true);
                        available.add(points[i][j]);
                    }
                }
            }
        }
    }

    /**
     * Generate the world with a random number of rooms and several hallways.
     */
    public Position generate() {
        int roomNumber = random.nextInt(10) + 20;
        for (int i = 0; i < roomNumber; i++) {
            if (available.isEmpty()) {
                break;
            }
            createRooms();
        }
        available = getMazeAvailable();
        createHallways();
        return placeInteractive();
    }

    /**
     * Create a room.
     */
    private void createRooms() {
        int startPointCode = random.nextInt(available.size());
        Point point = available.get(startPointCode);
        getAvailableSize(point);
        int numberOfSize = point.endPositions.size();
        int endPointCode = random.nextInt(numberOfSize);
        Room room = new Room(new Position(point.getXPosition(), point.getYPosition()),
                point.endPositions.get(endPointCode));
        rooms.add(room);
        drawRoom(room);
        setOutlineUnavailable(room);
        available = getAvailablePoints();
    }

    /**
     * Create the hallways.
     */
    private void createHallways() {
        createMaze();
        openTheDoor();
        removeDeadEnds();
        removeTooThickWall();
        removeUnusefulWalls();
    }

    /**
     * Calculate the available size of the room starting from a specific point.
     * This method will find out all the possible endpoints and save them into the
     * list in the Point object.
     * @param point The starting point of the room.
     */
    private void getAvailableSize(Point point) {
        int xPosition = point.getXPosition();
        int yPosition = point.getYPosition();
        int xLimit = (width - 1 - xPosition) / 2;
        int yLimit = (height - 1 - yPosition) / 2;
        if (xLimit > 3) {
            xLimit = 3;
        }
        if (yLimit > 3) {
            yLimit = 3;
        }
        if (rooms.size() == 0) {
            for (int i = 0; i < xLimit; i++) {
                for (int j = 0; j < yLimit; j++) {
                    point.endPositions.add(new Position(xPosition + (i + 1) * 2,
                            yPosition + (j + 1) * 2));
                }
            }
        } else {
            boolean flag;
            for (int i = xLimit; i > 0; i--) {
                flag = false;
                for (int j = yLimit; j > 0; j--) {
                    if (flag) {
                        point.endPositions.add(new Position(xPosition + i * 2, yPosition + j * 2));
                        continue;
                    }
                    if (!isInterference(point, new Position(xPosition + i * 2,
                            yPosition + j * 2))) {
                        point.endPositions.add(new Position(xPosition + i * 2, yPosition + j * 2));
                        flag = true;
                    }
                }
            }
        }
    }

    /**
     * Find out if there is a room exists in a specific area.
     * @param startPosition The start position of the area(Left-Bottom point)
     * @param endPosition The end position of the area(Right-Top point)
     * @return True if there is a room exists in the area, false otherwise.
     */
    private boolean isInterference(Position startPosition, Position endPosition) {
        Room room = new Room(startPosition, endPosition);
        for (Room thisRoom : rooms) {
            Position[] corners = thisRoom.getCorners();
            Position[] thisCorners = room.getCorners();
            for (Position position : corners) {
                if ((position.getXPosition() >= thisCorners[0].getXPosition()
                        && position.getXPosition() <= thisCorners[3].getXPosition())
                        && (position.getYPosition() >= thisCorners[0].getYPosition()
                        && position.getYPosition() <= thisCorners[3].getYPosition())) {
                    return true;
                }
            }
            for (Position position : thisCorners) {
                if ((position.getXPosition() >= corners[0].getXPosition()
                        && position.getXPosition() <= corners[3].getXPosition())
                        && (position.getYPosition() >= corners[0].getYPosition()
                        && position.getYPosition() <= corners[3].getYPosition())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Draw the wall and the floor of a room.
     * @param room The room need to be painted.
     */
    private void drawRoom(Room room) {
        Position[] corners = room.getCorners();
        int sXPosition = corners[0].getXPosition();
        int sYPosition = corners[0].getYPosition();
        int eXPosition = corners[3].getXPosition();
        int eYPosition = corners[3].getYPosition();
        for (int i = sXPosition; i < eXPosition + 1; i++) {
            for (int j = sYPosition; j < eYPosition + 1; j++) {
                map[i][j] = Tileset.FLOOR;
                points[i][j].available = false;
            }
        }
    }

    /**
     * Each room exists has an area outside that all the points in the area is not available
     * for placing a new room. This method is used to find the area and set all the point
     * in it unavailable.
     * @param room The room need to be processed.
     */
    private void setOutlineUnavailable(Room room) {
        Position[] corners = room.getCorners();
        int sXPosition = corners[0].getXPosition();
        int sYPosition = corners[0].getYPosition();
        int eXPosition = corners[3].getXPosition();
        int eYPosition = corners[3].getYPosition();
        if (sXPosition - 2 >= 0) {
            int yDif = (eYPosition - sYPosition) / 2;
            for (int i = 0; i < yDif + 1; i++) {
                points[sXPosition - 2][sYPosition + i * 2].available = false;
            }
        }
        if (sYPosition - 2 >= 0) {
            int xDif = (eXPosition - sXPosition) / 2;
            for (int i = 0; i < xDif + 1; i++) {
                points[sXPosition + i * 2][sYPosition - 2].available = false;
            }
        }
        if (sXPosition - 2 >= 0 && sYPosition - 2 >= 0) {
            points[sXPosition - 2][sYPosition - 2].available = false;
        }
    }

    /**
     * This method is used to find out all the points available for placing a new room.
     * @return A new available points list.
     */
    private List<Point> getAvailablePoints() {
        List<Point> availablePoints = new LinkedList<>();
        for (Point x : available) {
            if (x.available) {
                availablePoints.add(x);
            }
        }
        return availablePoints;
    }

    /**
     * Create maze on the places other than the area of room, using flood fill.
     */
    private void createMaze() {
        while (!available.isEmpty()) {
            int startPointCode = random.nextInt(available.size());
            LinkedList<Point> pointsSequence = new LinkedList<>();
            Point startPoint = available.get(startPointCode);
            pointsSequence.add(startPoint);
            while (!pointsSequence.isEmpty()) {
                Point point = pointsSequence.getLast();
                map[point.getXPosition()][point.getYPosition()] = Tileset.FLOOR;
                point.available = false;
                setCanConnectPoints(point);
                int size = point.canConnectPoints.size();
                if (size == 0) {
                    pointsSequence.removeLast();
                    continue;
                }
                int nextCode = random.nextInt(size);
                Point nextPoint = point.canConnectPoints.get(nextCode);
                connect(point, nextPoint);
                pointsSequence.add(nextPoint);
            }
            available = getMazeAvailable();
        }
    }

    /**
     * Connect two points with Tileset.FLOOR
     * @param thisPoint The start point.
     * @param targetPoint The target point.
     */
    private void connect(Point thisPoint, Point targetPoint) {
        int xDif = targetPoint.getXPosition() - thisPoint.getXPosition();
        int yDif = targetPoint.getYPosition() - thisPoint.getYPosition();
        if (xDif == 0) {
            if (yDif < 0) {
                map[thisPoint.getXPosition()][thisPoint.getYPosition() - 1] = Tileset.FLOOR;
            }
            if (yDif > 0) {
                map[thisPoint.getXPosition()][thisPoint.getYPosition() + 1] = Tileset.FLOOR;
            }
        }
        if (yDif == 0) {
            if (xDif < 0) {
                map[thisPoint.getXPosition() - 1][thisPoint.getYPosition()] = Tileset.FLOOR;
            }
            if (xDif > 0) {
                map[thisPoint.getXPosition() + 1][thisPoint.getYPosition()] = Tileset.FLOOR;
            }
        }
    }

    /**
     * Find the points that can be connected with this point and set the can connect point list.
     * @param point This point.
     */
    private void setCanConnectPoints(Point point) {
        int x = point.getXPosition();
        int y = point.getYPosition();
        List<Point> canConnect = new ArrayList<>();
        if (y + 2 < height) {
            if (points[x][y + 2].available) {
                canConnect.add(points[x][y + 2]);
            }
        }
        if (y - 2 > 0) {
            if (points[x][y - 2].available) {
                canConnect.add(points[x][y - 2]);
            }
        }
        if (x + 2 < width) {
            if (points[x + 2][y].available) {
                canConnect.add(points[x + 2][y]);
            }
        }
        if (x - 2 > 0) {
            if (points[x - 2][y].available) {
                canConnect.add(points[x - 2][y]);
            }
        }
        point.canConnectPoints = canConnect;
    }

    /**
     * Get the points available for being the start point of a maze.
     * @return The list of available points.
     */
    private List<Point> getMazeAvailable() {
        List<Point> newAvailable = new LinkedList<>();
        for (int i = 1; i < 81; i += 2) {
            for (int j = 1; j < 31; j += 2) {
                if (map[i][j] == Tileset.NOTHING) {
                    points[i][j].available = true;
                    newAvailable.add(points[i][j]);
                }
            }
        }
        return newAvailable;
    }

    /**
     * Create several doors on the walls of the rooms.
     */
    private void openTheDoor() {
        for (Room room : rooms) {
            findDoorAllowedSides(room);
            int allowedSidesSize = room.getDoorAllowedSides().size();
            int topSide = room.getCorners()[3].getYPosition();
            int leftSide = room.getCorners()[0].getXPosition();
            int rightSide = room.getCorners()[3].getXPosition();
            int downSide = room.getCorners()[0].getYPosition();
            for (int j = 0; j < 4; j++) {
                int sideCode = random.nextInt(allowedSidesSize);
                int side = room.getDoorAllowedSides().get(sideCode);
                if (isOpened(room, side)) {
                    continue;
                }
                int sideLength;
                int position;
                switch (side) {
                    case 0:
                        sideLength = rightSide - leftSide + 1;
                        position = random.nextInt(sideLength);
                        while (map[leftSide + position][topSide + 2] != Tileset.FLOOR) {
                            position = random.nextInt(sideLength);
                        }
                        map[leftSide + position][topSide + 1] = Tileset.FLOOR;
                        break;
                    case 1:
                        sideLength = topSide - downSide + 1;
                        position = random.nextInt(sideLength);
                        while (map[rightSide + 2][downSide + position] != Tileset.FLOOR) {
                            position = random.nextInt(sideLength);
                        }
                        map[rightSide + 1][downSide + position] = Tileset.FLOOR;
                        break;
                    case 2:
                        sideLength = rightSide - leftSide + 1;
                        position = random.nextInt(sideLength);
                        while (map[leftSide + position][downSide - 2] != Tileset.FLOOR) {
                            position = random.nextInt(sideLength);
                        }
                        map[leftSide + position][downSide - 1] = Tileset.FLOOR;
                        break;
                    case 3:
                        sideLength = topSide - downSide + 1;
                        position = random.nextInt(sideLength);
                        while (map[leftSide - 2][downSide + position] != Tileset.FLOOR) {
                            position = random.nextInt(sideLength);
                        }
                        map[leftSide - 1][downSide + position] = Tileset.FLOOR;
                        break;
                    default:
                        break;
                }
            }
        }
    }

    /**
     * Determine if there is a door created on a specific side of a specific room.
     * @param room The room
     * @param side The side of the room(0 for top side, 1 for right side, 2 for bottom side,
     *             3 for left side)
     * @return True if there is already a door, false otherwise.
     */
    private boolean isOpened(Room room, int side) {
        int topSide = room.getCorners()[3].getYPosition() + 1;
        int leftSide = room.getCorners()[0].getXPosition() - 1;
        int rightSide = room.getCorners()[3].getXPosition() + 1;
        int bottomSide = room.getCorners()[0].getYPosition() - 1;
        switch (side) {
            case 0:
                for (int i = leftSide; i < rightSide + 1; i++) {
                    if (map[i][topSide] == Tileset.FLOOR) {
                        return true;
                    }
                }
                return false;
            case 1:
                for (int i = bottomSide; i < topSide + 1; i++) {
                    if (map[rightSide][i] == Tileset.FLOOR) {
                        return true;
                    }
                }
                return false;
            case 2:
                for (int i = leftSide; i < rightSide + 1; i++) {
                    if (map[i][bottomSide] == Tileset.FLOOR) {
                        return true;
                    }
                }
                return false;
            case 3:
                for (int i = bottomSide; i < topSide + 1; i++) {
                    if (map[leftSide][i] == Tileset.FLOOR) {
                        return true;
                    }
                }
                return false;
            default:
                return false;
        }
    }

    /**
     * Find that on which side a door can be created for a specific room.
     * @param room The room.
     */
    private void findDoorAllowedSides(Room room) {
        int topSide = room.getCorners()[3].getYPosition();
        int leftSide = room.getCorners()[0].getXPosition();
        int rightSide = room.getCorners()[3].getXPosition();
        int bottomSide = room.getCorners()[0].getYPosition();
        List<Integer> doorAllowedSides = new ArrayList<>();
        if (topSide + 2 != height) {
            doorAllowedSides.add(0);
        }
        if (rightSide + 2 != width) {
            doorAllowedSides.add(1);
        }
        if (bottomSide - 2 > 0) {
            doorAllowedSides.add(2);
        }
        if (leftSide - 2 > 0) {
            doorAllowedSides.add(3);
        }
        room.setDoorAllowedSides(doorAllowedSides);
    }

    /**
     * This method is used to remove all the dead ends in the maze, the dead end
     * means that there are 3 or more walls around a Tileset.FLOOR.
     */
    public void removeDeadEnds() {
        available = new LinkedList<>();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (map[i][j] == Tileset.FLOOR) {
                    points[i][j].available = true;
                    available.add(points[i][j]);
                } else {
                    points[i][j].available = false;
                }
            }
        }
        for (Point point : available) {
            if (!point.available) {
                continue;
            }
            Point thisPoint = point;
            while (isDeadEnd(thisPoint)) {
                Point nextPoint = getNextPoint(thisPoint);
                map[thisPoint.getXPosition()][thisPoint.getYPosition()] = Tileset.WALL;
                thisPoint.available = false;
                if (nextPoint != null) {
                    thisPoint = nextPoint;
                } else {
                    break;
                }
            }
        }
    }

    /**
     * Determine that if this Tileset.FLOOR is a dead end.
     * @param point The point to be determined.
     * @return True if the Tileset.FLOOR is a dead end, false otherwise.
     */
    private boolean isDeadEnd(Point point) {
        Point[] nearPoints = getNearPoints(point);
        int count = 0;
        for (Point thisPoint : nearPoints) {
            if (map[thisPoint.getXPosition()][thisPoint.getYPosition()] == Tileset.WALL) {
                count++;
            }
        }
        return count > 2;
    }

    /**
     * If a Tileset.FLOOR is a dead end, this method will be called to find the Tileset.FLOOR
     * near it.
     * @param point This point.
     * @return The point near this point.
     */
    private Point getNextPoint(Point point) {
        Point[] nearPoints = getNearPoints(point);
        for (Point thisPoint : nearPoints) {
            if (map[thisPoint.getXPosition()][thisPoint.getYPosition()] == Tileset.FLOOR) {
                return thisPoint;
            }
        }
        return null;
    }

    /**
     * This method is used to get all the points near a specific point("near" means that
     * the distance between two points is exactly 1).
     * @param point The point.
     * @return An array of points saving all the near points.
     */
    private Point[] getNearPoints(Point point) {
        Point[] nearPoints = new Point[4];
        int x = point.getXPosition();
        int y = point.getYPosition();
        nearPoints[0] = points[x + 1][y];
        nearPoints[1] = points[x][y + 1];
        nearPoints[2] = points[x - 1][y];
        nearPoints[3] = points[x][y - 1];
        return nearPoints;
    }

    /**
     * After the dead ends are removed, there will exist some really thick walls(thickness is
     * more than 2), this method is used to remove these too thick walls and replace
     * them with Tileset.NOTHING.
     */
    private void removeTooThickWall() {
        for (int i = 0; i < width; i++) {
            if (i == 0 | i == width - 1) {
                continue;
            }
            for (int j = 0; j < height; j++) {
                if (j == 0 | j == height - 1) {
                    continue;
                }
                if (map[i][j] == Tileset.WALL) {
                    if (map[i - 1][j + 1] == Tileset.FLOOR) {
                        continue;
                    }
                    if (map[i + 1][j + 1] == Tileset.FLOOR) {
                        continue;
                    }
                    if (map[i - 1][j - 1] == Tileset.FLOOR) {
                        continue;
                    }
                    if (map[i + 1][j - 1] == Tileset.FLOOR) {
                        continue;
                    }
                    Point[] nearPoints = getNearPoints(points[i][j]);
                    int count = 0;
                    for (Point point : nearPoints) {
                        if (map[point.getXPosition()][point.getYPosition()] == Tileset.WALL
                            | map[point.getXPosition()][point.getYPosition()] == Tileset.NOTHING) {
                            count++;
                        }
                    }
                    if (count == 4) {
                        map[i][j] = Tileset.NOTHING;
                    }
                }
            }
        }
    }

    /**
     * Remove some unuseful walls, all of them are on the side of the map and they have no
     * floor nearby.
     */
    private void removeUnusefulWalls() {
        for (int i = 0; i < width; i++) {
            if (map[i][1] == Tileset.NOTHING) {
                map[i][0] = Tileset.NOTHING;
            }
            if (map[i][height - 2] == Tileset.NOTHING) {
                map[i][height - 1] = Tileset.NOTHING;
            }
        }
        for (int j = 0; j < height; j++) {
            if (map[1][j] == Tileset.NOTHING) {
                map[0][j] = Tileset.NOTHING;
            }
            if (map[width - 2][j] == Tileset.NOTHING) {
                map[width - 1][j] = Tileset.NOTHING;
            }
        }
    }

    /**
     * Place some interactive elements, such as the player and the locked door.
     * @return The position of the player.
     */
    private Position placeInteractive() {
        List<Point> pointsCanBeExit = new ArrayList<>();
        List<Point> pointsCanBeSpawned = new ArrayList<>();
        for (int i = 1; i < width - 1; i++) {
            for (int j = 1; j < height - 1; j++) {
                if (map[i][j] == Tileset.WALL) {
                    Point[] nearPoints = getNearPoints(points[i][j]);
                    int countNothing = 0;
                    int countFloor = 0;
                    for (Point point : nearPoints) {
                        if (map[point.getXPosition()][point.getYPosition()] == Tileset.NOTHING) {
                            countNothing++;
                        }
                        if (map[point.getXPosition()][point.getYPosition()] == Tileset.FLOOR) {
                            countFloor++;
                        }
                    }
                    if (countNothing == 1 && countFloor == 1) {
                        pointsCanBeExit.add(points[i][j]);
                    }
                }
                if (map[i][j] == Tileset.FLOOR) {
                    pointsCanBeSpawned.add(points[i][j]);
                }
            }
        }
        int size = pointsCanBeExit.size();
        int exitCode = random.nextInt(size);
        Point point = pointsCanBeExit.get(exitCode);
        map[point.getXPosition()][point.getYPosition()] = Tileset.LOCKED_DOOR;
        size = pointsCanBeSpawned.size();
        int spawnCode = random.nextInt(size);
        point = pointsCanBeSpawned.get(spawnCode);
        map[point.getXPosition()][point.getYPosition()] = Tileset.PLAYER;
        return point;
    }
}
