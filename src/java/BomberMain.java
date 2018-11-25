package Java;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.lang.Integer;
import java.io.*;

/**
 * File:BomberMain.java
 */

/**
 * Đây là điểm bắt đầu game.
 */
public class BomberMain extends JFrame {
    /** relative path for files */
    public static String RP = "./";
    /** menu */
    private BomberMenu menu = null;
    /** game */
    private BomberGame game = null;

    /** tiếng động */
    public static BomberSndEffect sndEffectPlayer = null;
    /** tính kích thước game */
    public static final int shiftCount = 4;
    /** kích cỡ 1 ô vuông */
    public static final int size = 1 << shiftCount;

    static {
        sndEffectPlayer = new BomberSndEffect();
    }

    /**
     * Dựng frame chính.
     */
    public BomberMain() {
        /** add window event handler */
        addWindowListener(new WindowAdapter() {
            /**
             * Handles window closing events.
             * @param evt window event
             */
            public void windowClosing(WindowEvent evt) {
                /** terminate the program */
                System.exit(0);
            }
        });

        /** add keyboard event handler */
        addKeyListener(new KeyAdapter() {
            /**
             * Handles key pressed events.
             * @param evt keyboard event
             */
            public void keyPressed(KeyEvent evt) {
                if (menu != null) menu.keyPressed(evt);
                if (game != null) game.keyPressed(evt);
            }

            /**
             * Handles key released events.
             * @param evt keyboard event
             */
            public void keyReleased(KeyEvent evt) {
                if (game != null) game.keyReleased(evt);
            }
        });

        /** set tiêu đề */
        setTitle("Bom-bờ-men");

        /** set icon ngoài */
        try {
            setIconImage(Toolkit.getDefaultToolkit().getImage(
                new File(RP + "Images/Bomberman.gif").getCanonicalPath()));
        }
        catch (Exception e) { new ErrorDialog(e); }

        /** Tạo menu frame */
        getContentPane().add(menu = new BomberMenu(this));

        /** Ngăn resize frame */
        setResizable(false);
        /** Tối thiểu frame */
        pack();

        /** Get size màn hình */
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();

        int x = (d.width - getSize().width) / 2;
        int y = (d.height - getSize().height) / 2;

        /** đẩy frame về trung tâm */
        setLocation(x, y);
        /** show frame */
        show();
        /** cho nó ra trước màn hình chính */
        toFront();
    }

    /**
     * Tạo game mới.
     */
    public void newGame(int players)
    {
        JDialog dialog = new JDialog(this, "Đang nạp game...", false);
        dialog.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        dialog.setSize(new Dimension(200, 0));
        dialog.setResizable(false);
        int x = getLocation().x + (getSize().width - 200) / 2;
        int y = getLocation().y + getSize().height / 2;
        dialog.setLocation(x, y);
        /** show thông báo */
        dialog.show();

        /** loại panel khỏi pane nội dung */
        getContentPane().removeAll();
        getLayeredPane().removeAll();
        /** bỏ menu */
        menu = null;
        /** tạo bản đồ mới */
        BomberMap map = new BomberMap(this);

        /** tạo game */
        game = new BomberGame(this, map, players);

        /** loại bỏ thông báo loading */
        dialog.dispose();
        /** show frame */
        show();
        /** nếu có frame Java 2 sẵn sàng */
        if (Main.J2) {
           BomberBGM.unmute();
           /** bật nhạc bay lắc */
           BomberBGM.change("Battle");
        }
    }

    /**
     *  Bắt đầu chương trình
     */
    public static void main(String[] args) {
        BomberMain bomberMain1 = new BomberMain();
    }
}
