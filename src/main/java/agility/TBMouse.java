package agility;

import org.tbot.bot.TBot;
import org.tbot.methods.Mouse;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Created by Jonross on 6/4/2015.
 */
public class TBMouse {

    public static void press(int button) {
        press(Mouse.getX(), Mouse.getY(), button);
    }

    public static void press(int x, int y, int button) {
        forward(x, y, button, MouseEvent.MOUSE_PRESSED);
    }

    public static void release(int button) {
        release(Mouse.getX(), Mouse.getY(), button);
    }

    public static void release(int x, int y, int button) {
        forward(x, y, button, MouseEvent.MOUSE_RELEASED);
    }

    public static void forward(int x, int y, int button, int type) {
        MouseListener listener = Mouse.getMouseListener();
        if(Mouse.getMouse() != null && listener != null) {
            MouseEvent event = new MouseEvent(TBot.getBot().getCanvas(), type, System.currentTimeMillis(), 0, x, y, 1, false, button);
            if (type == MouseEvent.MOUSE_PRESSED)
                listener.mousePressed(event);
            if (type == MouseEvent.MOUSE_RELEASED)
                listener.mouseReleased(event);
            if (type == MouseEvent.MOUSE_CLICKED)
                listener.mouseClicked(event);
        }
    }

}
