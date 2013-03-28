package stage1;




import java.util.Random;


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import javax.microedition.lcdui.Graphics;

/**
 * 程序说明：
 * 本程序作者为王阿晶，版权所有，请勿翻版。
 */
/**
 *
 * @author WangJing
 * @createtime 10:35:02
 * @modifiedTime 10:35:02
 *
 */
public class Maze {

    private int mazeMap[][];
    private Random random = new Random();
    public static final int MAZE_WIDTH = 12;
    public static final int MAZE_HEIGHT = 12;

    public int[][] getMazeMap() {
        return mazeMap;
    }
    
    
    public Maze() {
        init();
    }

    public void init() {
        
            int x, y, n, d;
            int dx[] = {0, 0, -1, 1};
            int dy[] = {-1, 1, 0, 0};
            int todo[] = new int[MAZE_HEIGHT * MAZE_WIDTH], todonum = 0;

            /* We want to create a maze on a grid. */
            mazeMap = new int[MAZE_WIDTH][MAZE_HEIGHT];

            /* We start with a grid full of walls. */
            for (x = 0; x < MAZE_WIDTH; ++x) {
                for (y = 0; y < MAZE_HEIGHT; ++y) {
                    if (x == 0 || x == MAZE_WIDTH - 1 || y == 0 || y == MAZE_HEIGHT - 1) {
                        mazeMap[x][y] = 32;
                    } else {
                        mazeMap[x][y] = 63;
                    }
                    /* Select any square of the grid, to start with. */
                }
            }
            x = (int) (1 + random.nextDouble() * (MAZE_WIDTH - 2));
            y = (int) (1 + random.nextDouble() * (MAZE_HEIGHT - 2));

            /* Mark this square as connected to the maze. */
            mazeMap[x][y] &= ~48;

            /* Remember the surrounding squares, as we will */
            for (d = 0; d < 4; ++d) {
                if ((mazeMap[x + dx[d]][y + dy[d]] & 16) != 0) {
                    /* want to connect them to the maze. */

                    /* alternately, you could use a struct to store the two integers
                     * this would result in easier to read code, though not as speedy
                     * of course, if you were worried about speed, you wouldn't be using Java
                     * you could also use a single integer which represents (x + y * width)
                     * this would actually be faster than the current approach
                     * - quin/10-24-06
                     *    Actually, the former wouldn't work in Java- there's no such thing as a
                     *    struct. It's a class or nothing, I'm afraid.
                     *    - Jae Armstrong/23-03-07
                     */

                    todo[todonum++] = ((x + dx[d]) << 16) | (y + dy[d]);
                    mazeMap[x + dx[d]][y + dy[d]] &= ~16;
                }
                /* We won't be finished until all is connected. */
            }
            
            while (todonum > 0) {
                /* We select one of the squares next to the maze. */
                n = (int) (random.nextDouble() * todonum);
                x = todo[n] >> 16; /* the top 2 bytes of the data */
                y = todo[n] & 65535; /* the bottom 2 bytes of the data */

                /* We will connect it, so remove it from the queue. */
                todo[n] = todo[--todonum];

                /* Select a direction, which leads to the maze. */
                do {
                    d = (int) (random.nextDouble() * 4);
                } while ((mazeMap[x + dx[d]][y + dy[d]] & 32) != 0);

                /* Connect this square to the maze. */
                mazeMap[x][y] &= ~((1 << d) | 32);
                mazeMap[x + dx[d]][y + dy[d]] &= ~(1 << (d ^ 1));

                /* Remember the surrounding squares, which aren't */
                for (d = 0; d < 4; ++d) {
                    if ((mazeMap[x + dx[d]][y + dy[d]] & 16) != 0) {

                        /* connected to the maze, and aren't yet queued to be. */
                        todo[todonum++] = ((x + dx[d]) << 16) | (y + dy[d]);
                        mazeMap[x + dx[d]][y + dy[d]] &= ~16;
                    }
                    /* Repeat until finished. */
                }
            }
    }

//    public  void paint(Graphics g) {
//
//        int x, y;
//        for (x = 1; x < MAZE_WIDTH; ++x) {
//            for (y = 1; y < MAZE_HEIGHT; ++y) {
//                if ((mazeMap[x][y] & 1) != 0) /* This cell has a top wall */ {
//                    g.drawLine(x * 16, y * 16, x * 16 + 16, y * 16);
//                }
//                if ((mazeMap[x][y] & 2) != 0) /* This cell has a bottom wall */ {
//                    g.drawLine(x * 16, y * 16 + 16, x * 16 + 16, y * 16 + 16);
//                }
//                if ((mazeMap[x][y] & 4) != 0) /* This cell has a left wall */ {
//                    g.drawLine(x * 16, y * 16, x * 16, y * 16 + 16);
//                }
//                if ((mazeMap[x][y] & 8) != 0) /* This cell has a right wall */ {
//                    g.drawLine(x * 16 + 16, y * 16, x * 16 + 16, y * 16 + 16);
//                }
//                System.out.print(mazeMap[x][y] + "  ");
//            }
//            //("  ");
//        }
//    }
}
