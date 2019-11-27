
public class Explorer {

    private final class Heading {
        public static final int NORTH = 0;
        public static final int EAST = 3;
        public static final int SOUTH = 6;
        public static final int WEST = 9;
    }

    private class Position {
        public int x = 0;
        public int y = 0;
    }

    private Maze maze;
    private Position pos;
    private int heading;
    private String[] mazeMap;

    public String exploreMaze(Maze z) {
        maze = z;
        pos = new Position();
        heading = Heading.SOUTH;
        boolean done = false;
        initStringArray(z);

        do {
            int exitCount = getExitCount();
            if (exitCount>2)
                done = doJunction(getExits(exitCount));
            else
                doPathway();
        } while (!done);

        return String.join("\n", mazeMap);
    }

    private void initStringArray(Maze z) {
        mazeMap = new String[z.height()];
        mazeMap[0] = mazeMap[mazeMap.length-1] = "#".repeat(z.width());
        for (int i=1; i<mazeMap.length-1;i++)
            mazeMap[i] = "#" + ".".repeat(z.width()-2) + "#";
    }

    private boolean doJunction(int[] exits) {
        if (sumOfMarks() == 0) {
            markAndWalk(exits[0]); // BACK ist in jedem Fall der letzte Exit
            return false;
        }
        if (maze.marks(Maze.Direction.BACK) == 0) {
            markAndWalk(Maze.Direction.BACK);
            return false;
        }
        int exit = minExit(exits);
        if (exit == -1) return true;
        markAndWalk(exit);
        return false;
    }

    private int sumOfMarks() {
        int result = 0;
        for (int i = Maze.Direction.LEFT; i <= Maze.Direction.BACK; i++)
            result += maze.marks(i);
        return result;
    }

    private int minExit(int[] exits) {
        int minMarkCount = 2;
        int result = -1;
        for (int i=0; i<exits.length; i++)
            if (maze.marks(exits[i])<minMarkCount) {
                minMarkCount = maze.marks(exits[i]);
                result = exits[i];
            }
        return result;
    }

    private void markAndWalk(int direction) {
        maze.mark(Maze.Direction.BACK);
        maze.mark(direction);
        walk(direction);
    }


    private void doPathway() {
        for (int i = Maze.Direction.LEFT; i <= Maze.Direction.BACK; i++)
            if (!isWall(i)) {
                walk(i);
                return;
            }
    }

    private int[] getExits(int exitCount) {
        int[] result = new int[exitCount];
        int count = 0;
        for (int i = Maze.Direction.LEFT; i <= Maze.Direction.BACK; i++)
            if (!isWall(i))
                result[count++] = i;
        return result;
    }

    private int getExitCount() {
        int exitCount = 0;
        for (int i = Maze.Direction.LEFT; i <= Maze.Direction.BACK; i++)
            if (!isWall(i))
                exitCount++;
        return exitCount;
    }

    private void walk(int direction) {
        pos = next(direction);
        heading = headingAfterTurn(direction);
        maze.walk(direction);
    }


    private Position next(int direction) {
        Position result = new Position();
        result.x = pos.x;
        result.y = pos.y;
        int heading = headingAfterTurn(direction);
        switch(heading) {
            case Heading.NORTH:
                result.y--;
                break;
            case Heading.EAST:
                result.x++;
                break;
            case Heading.SOUTH:
                result.y++;
                break;
            case Heading.WEST:
                result.x--;
                break;
        }
        return result;
    }

    private int turnLeft(int heading) {
        return (heading + 9) % 12;
    }

    private int turnRight(int heading) {
        return (heading + 3) % 12;
    }

    private int headingAfterTurn(int direction) {
        switch (direction) {
            case Maze.Direction.LEFT:
                return turnLeft(heading);
            case Maze.Direction.AHEAD:
                return heading;
            case Maze.Direction.RIGHT:
                return turnRight(heading);
            case Maze.Direction.BACK:
                return turnRight(turnRight(heading));
        }
        return heading;
    }

    private boolean isWall(int direction) {
        boolean result = maze.isWall(direction);
        Position pos = next(direction);
        StringBuilder sb = new StringBuilder(mazeMap[pos.y+1]);
        sb.setCharAt(pos.x+1, (result?'#':' '));
        mazeMap[pos.y+1] = sb.toString();
        return result;
    }


    public static void main(String[] args) {
        Maze m = new Maze();
        Explorer e = new Explorer();
        System.out.println(e.exploreMaze(m));
    }

}
