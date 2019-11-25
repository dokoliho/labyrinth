public class Maze {

    private class Position {
        public int x = 0;
        public int y = 0;
    }

    private static final int LEFT = 0;
    private static final int AHEAD = 1;
    private static final int RIGHT = 2;
    private static final int BACK = 3;

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

    private String[] maze;
    private Position pos = new Position();
    private int heading = 6; // down

    public Maze() {
        maze = example01;
    }

    public int width() {
        return maze[0].length()+2;
    }

    public int height() {
        return maze.length+2;
    }

    private int turn(int direction) {
        int result = heading;
        switch (direction) {
            case LEFT:
                result += 9;
                break;
            case AHEAD:
                break;
            case RIGHT:
                result += 3;
                break;
            case BACK:
                result += 6;
                break;
        }
        result = result % 12;
        return result;
    }

    private Position next(int direction) {
        Position result = new Position();
        result.x = pos.x;
        result.y = pos.y;
        int dir = turn(direction);
        switch(dir) {
            case 0:
                result.y--;
                break;
            case 3:
                result.x++;
                break;
            case 6:
                result.y++;
                break;
            case 9:
                result.x--;
                break;
        }
    }

    public boolean isWall(int direction) {
        Position p = next(direction);
        return isWall(p);
    }

    public boolean isWall(Position p) {
        if (p.x<0 || p.x>=maze[0].length()) return true;
        if (p.y<0 || p.y>=maze.length) return true;
        return maze[p.y].charAt(p.x)=='#';
    }

    public void walk(int direction) {
        Position p = next(direction);
        if (isWall(p)) return;
        heading = turn(direction);
        pos = p;
    }



}
