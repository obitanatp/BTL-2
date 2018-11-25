package Java;
import java.awt.*;
import javax.swing.*;
import java.io.*;

/**
 * File:BomberBonus
 */

/**
 * Lớp tạo phần thưởng.
 */
public class BomberBonus extends Thread {
    /** map object */
    private BomberMap map = null;
    /** vị trí */
    private int x = 0;
    private int y = 0;
    /** frame count */
    private int frame = 0;
    /** alive flag */
    private boolean alive = true;
    /** loại bonus */
    private int type = 0;
    /** xử lý bom sprite */
    private Image[] images = null;
    /** render */
    private static Object hints = null;

    private static int FIRE = 0;
    private static int BOMB = 1;

    static {
        /** nếu frame 2 chạy */
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
     * Dựng một bonus item.
     */
    public BomberBonus(BomberMap map, int x, int y, int type) {
        this.map = map;
        this.x = x;
        this.y = y;
        this.type = type;
        this.images = BomberMap.bonusImages[type];

        setPriority(Thread.MAX_PRIORITY);
        start();
    }

    /**
     * loop main.
     */
    public synchronized void run() {
        while (alive) {
            /** vẽ phần thưởng */
            map.paintImmediately(x, y, BomberMain.size, BomberMain.size);
            /** rotate frame */
            frame = (frame + 1) % 2;
            /** nghỉ 130 ms */
            try { sleep(130); } catch (Exception e) {}
            if (frame == 10) break;
        }
        /** loại bỏ khỏi lưới */
        map.removeBonus(x, y);
    }

    /**
     * Đưa phần thưởng cho người chơi rồi xóa nó.
     */
    public void giveToPlayer(int player) {
        BomberMain.sndEffectPlayer.playSound("Bonus");
        /** nếu là phần thưởng hỏa lực */
        if (type == FIRE) /** nâng hỏa lực lên 1 */
           BomberGame.players[player - 1].fireLength += 1;
        /** nếu là phần thưởng số lượng bom */
        else if (type == BOMB) /** nâng số lượng bom có thể đặt lên 1 */
             BomberGame.players[player - 1].totalBombs += 1;
        kill();
    }

    /**
     * Xóa item
     */
    public void kill() {
        alive = false;
        interrupt();
    }

    /**
     * phương thức vẽ.
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