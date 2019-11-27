public class Maze {

    private class Position {
        public int x = 0;
        public int y = 0;
    }

    public final class Direction {
        public static final int LEFT = 0;
        public static final int AHEAD = 1;
        public static final int RIGHT = 2;
        public static final int BACK = 3;
    }

    public final class Bearing {
        public static final int NORTH = 0;
        public static final int EAST = 3;
        public static final int SOUTH = 6;
        public static final int WEST = 9;
    }


    private static final String[] example01 = {
            "           ",
            " ##########",
            " # #       ",
            " # # ##### ",
            " #   #   # ",
            " ### # # # ",
            "   # # # # ",
            "## # # # # ",
            "   #     # ",
            "## ####### ",
            "           "
    };

    private boolean[][] maze;
    private int[][] mark;
    private Position pos = new Position();
    private int heading = Bearing.SOUTH;

    public Maze() {
        init(example01);
    }

    private void init(String[] layout) {
        maze = new boolean[layout.length][layout[0].length()];
        mark = new int[layout.length][layout[0].length()];
        for (int row = 0; row < layout.length; row++)
            for (int col = 0; col < layout[row].length(); col++) {
                maze[row][col] = layout[row].charAt(col) == '#';
                mark[row][col] = 0;
            }
    }

    private int getMarks(Position p, int bearing) {
        int b = mark[p.y][p.x];
        switch(bearing) {
            case Bearing.NORTH:
                break;
            case Bearing.EAST:
                b = b>>2;
                break;
            case Bearing.SOUTH:
                b = b>>4;
                break;
            case Bearing.WEST:
                b = b>>6;
                break;
        }
        return b & 3;
    }

    private void setMarks(Position p, int bearing, int count) {
        int mask = 3;
        switch(bearing) {
            case Bearing.NORTH:
                break;
            case Bearing.EAST:
                count = count<<2;
                mask = mask<<2;
                break;
            case Bearing.SOUTH:
                count = count<<4;
                mask = mask<<4;
                break;
            case Bearing.WEST:
                count = count<<6;
                mask = mask<<6;
                break;
        }
        mask = ~mask;
        mark[p.y][p.x] = (mark[p.y][p.x] & mask) + count;
    }


    public int width() {
        return maze[0].length+2;
    }

    public int height() {
        return maze.length+2;
    }

    private int turnLeft(int startDir) {
        return (startDir + 9) % 12;
    }

    private int turnRight(int startDir) {
        return (startDir + 3) % 12;
    }

    private int turn(int direction) {
        switch (direction) {
            case Direction.LEFT:
                return turnLeft(heading);
            case Direction.AHEAD:
                return heading;
            case Direction.RIGHT:
                return turnRight(heading);
            case Direction.BACK:
                return turnRight(turnRight(heading));
        }
        return heading;
    }


    private Position next(int direction) {
        Position result = new Position();
        result.x = pos.x;
        result.y = pos.y;
        int bearing = turn(direction);
        switch(bearing) {
            case Bearing.NORTH:
                result.y--;
                break;
            case Bearing.EAST:
                result.x++;
                break;
            case Bearing.SOUTH:
                result.y++;
                break;
            case Bearing.WEST:
                result.x--;
                break;
        }
        return result;
    }

    public boolean isWall(int direction) {
        Position p = next(direction);
        return isWall(p);
    }

    private boolean isWall(Position p) {
        if (p.x<0 || p.x>=maze[0].length) return true;
        if (p.y<0 || p.y>=maze.length) return true;
        return maze[p.y][p.x];
    }

    public void walk(int direction) {
        Position p = next(direction);
        if (isWall(p)) {
            System.out.println("Boing");
            return;
        }
        heading = turn(direction);
        pos = p;
        // System.out.println("Maze x " + pos.x + " y " + pos.y + " heading " + heading);
    }

    public void mark(int direction) {
        int bearing = turn(direction);
        setMarks(pos, bearing, getMarks(pos, bearing)+1);
    }

    public int marks(int direction) {
        int bearing = turn(direction);
        return getMarks(pos, bearing);
    }


}
