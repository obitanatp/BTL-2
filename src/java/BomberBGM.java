package Java;
import java.io.*;

/**
 * File:BomberBGM
 */

/**
 * Lớp tạo nhạc nền.
 */
public class BomberBGM {

    /** SoundPlayer */
    private static Object player;
    /** nhạc vừa phát */
    private static int lastSelection = -1;

    static {
        /** nếu frame 2 sẵn sàng */
        if (Main.J2) {
           /** tạo và load file nhạc */
           try {
               player = new SoundPlayer(
           new File(BomberMain.RP + "Sounds/BomberBGM/").
           getCanonicalPath());
           }
           catch (Exception e) { new ErrorDialog(e); }
           ((SoundPlayer)player).open();
        }
    }

    /**
     * Thay nhạc nền
     */
    public static void change(String arg) {
        /** nếu frame 2 sẵn sàng */
        if (Main.J2) {
            /**
             * thay nhạc nếu nhạc hiện tại nếu không phù hợp
             */
            int i = 0;
            while (i < ((SoundPlayer)player).sounds.size() &&
            ((SoundPlayer)player).sounds.elementAt(i).
            toString().indexOf(arg) < 0) i += 1;
            if (i != lastSelection && i <
               ((SoundPlayer)player).sounds.size()) {
                lastSelection = i;
                ((SoundPlayer)player).change(lastSelection, true);
            }
        }
    }

    /**
     * Dừng phát nhạc.
     */
    public static void stop()
    {
        /** nếu frame 2 sẵn sàng */
        if (Main.J2) {
           ((SoundPlayer)player).controlStop();
        }
    }

    /**
     * Câm.
     */
    public static void mute()
    {
        /** nếu frame 2 sẵn sàng */
        if (Main.J2) {
           ((SoundPlayer)player).mute();
        }
    }

    /**
     * Bật tiếng.
     */
    public static void unmute()
    {
        /** nếu frame 2 sẵn sàng */
        if (Main.J2) {
            ((SoundPlayer)player).unmute();
        }
    }
}