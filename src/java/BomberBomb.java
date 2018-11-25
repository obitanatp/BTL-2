package Java;
import java.awt.*;
import javax.swing.*;
import java.io.*;

/**
 * File:BomberBomb
 */

/**
 * Lớp này tạo bom.
 */
public class BomberBomb extends Thread {
    /** map object */
    private BomberMap map = null;
    /** vị trí */
    private int x = 0;
    private int y = 0;
    /** đếm frame */
    private int frame = 0;
    /** alive flag */
    private boolean alive = true;
    /** owner */
    private int owner = 0;
    /** đếm ngược : 3000 ms */
    private int countDown = 3000;
    /** xử lí bom sprite */
    private static Image[] images = null;
    /** render */
    private static Object hints = null;

    static {
        /** nếu frame 2 đang chạy */
        if (Main.J2) {
            /** tạo và render đồ họa */
            RenderingHints h = null;
            h = new RenderingHints(null);
            h.put(RenderingHints.KEY_TEXT_ANTIALIASING,
             RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            h.put(RenderingHints.KEY_FRACTIONALMETRICS,
             RenderingHints.VALUE_FRACTIONALMETRICS_ON);
            h.put(RenderingHints.KEY_ALPHA_INTERPOLATION,
             RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
            h.put(RenderingHints.KEY_ANTIALIASING,
             RenderingHints.VALUE_ANTIALIAS_ON);
            h.put(RenderingHints.KEY_COLOR_RENDERING,
             RenderingHints.VALUE_COLOR_RENDER_QUALITY);
            hints = (RenderingHints)h;
        }
    }

    /**
     * Dựng 1 quả bom
     */
    public BomberBomb(BomberMap map, int x, int y, int owner) {
        this.map = map;
        this.x = x;
        this.y = y;
        this.owner = owner - 1;
        this.images = BomberMap.bombImages;

        map.grid[x >> BomberMain.shiftCount][y >> BomberMain.shiftCount] =
        BomberMap.BOMB;
        setPriority(Thread.MAX_PRIORITY);
        start();
    }

    /**
     * loop main.
     */
    public synchronized void run() {
        while (alive) {
            /** vẽ bom */
            //paint();
            map.paintImmediately(x, y, BomberMain.size, BomberMain.size);
            /** rotate frame */
            frame = (frame + 1) % 2;
            /** nghỉ 130 ms */
            try { sleep(130); } catch (Exception e) {}
            if (!alive) break;
            /** đếm ngược l */
            countDown -= 130;
            /** nếu đếm ngược về 0 thì thoát */
            /** loop và short bom */
            if (countDown <= 0) break;
        }
        /** loại ra khỏi lưới */
        map.grid[x >> BomberMain.shiftCount][y >> BomberMain.shiftCount] =
        BomberMap.NOTHING;
        /** đưa bom về player */
        BomberGame.players[owner].usedBombs -= 1;
        map.bombGrid[x >> BomberMain.shiftCount][y >> BomberMain.shiftCount] =
        null;
        BomberGame.players[owner].bombGrid
        [x >> BomberMain.shiftCount][y >> BomberMain.shiftCount] = false;
        map.removeBomb(x, y);
        BomberMain.sndEffectPlayer.playSound("Explosion");
        /** tạo lửa */
        map.createFire(x, y, owner, BomberMap.FIRE_CENTER);
    }

    /**
     * Cho nổ
     */
    public void shortBomb() {
        alive = false;
        interrupt();
    }

    /**
     * phương pháp vẽ.
     */
    public void paint(Graphics g) {
        /** nếu frame 2 chạy */
        if (Main.J2) { paint2D(g); }
        /** nếu frame 2 không chạy */
        else {
             g.drawImage(images[frame], x, y,
             BomberMain.size, BomberMain.size, null);
        }
    }

    /**
     * phương thức vẽ 2D
     */
    public void paint2D(Graphics graphics) {
        Graphics2D g2 = (Graphics2D)graphics;
        /** set render */
        g2.setRenderingHints((RenderingHints)hints);
        g2.drawImage(images[frame], x, y,
        BomberMain.size, BomberMain.size, null);
    }
}