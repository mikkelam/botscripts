package agility;

import org.tbot.methods.Camera;
import org.tbot.methods.Mouse;
import org.tbot.methods.Random;
import org.tbot.methods.Time;
import org.tbot.methods.input.mouse.target.DefaultStaticMouseTarget;

import java.awt.Point;
import java.awt.event.MouseEvent;

/**
 * Created by Novum
 * on 14/04/2015.
 */
public class MouseCam {

    public static int distanceToPitch(int target) {
        return (int) ((target - Camera.getPitch()) * 2.25);
    }

    public static int distanceToAngle(int target) {
        double difference = (target - Camera.getYaw()) * -1;
        if (difference > 180) difference = -360 + difference;
        if (difference < -180) difference = 360 + difference;
        return (int) (difference * 3);
    }

    public static boolean setPitch(int pitch) {
        return setCamera(Camera.getYaw() + Random.nextInt(-5, 5), pitch);
    }

    public static boolean setAngle(int angle) {
        return setCamera(angle, Camera.getPitch() + Random.nextInt(-5, 5));
    }

    public static boolean setCamera(int angle, int pitch) {
        int destinationX = Mouse.getX() + MouseCam.distanceToAngle(angle);
        int destinationY = Mouse.getY() + MouseCam.distanceToPitch(pitch);

        int offsetX = limit(0, 764, destinationX);
        int offsetY = limit(0, 503, destinationY);
        destinationX += offsetX;
        destinationY += offsetY;

        if (Mouse.isOnScreen()) {
            if (Mouse.move(Mouse.getX() + offsetX, Mouse.getY() + offsetY)) {
                Time.sleep(20, 110);
                TBMouse.press(MouseEvent.BUTTON2);
                Mouse.moveMouse(new DefaultStaticMouseTarget(new Point(destinationX, destinationY)));
                TBMouse.release(MouseEvent.BUTTON2);
                return true;
            }
        }
        return false;
    }

    public static int limit(int min, int max, int value) {
        return limit(min, max, value, Random.nextInt(10));
    }

    public static int limit(int min, int max, int value, int random) {
        int offsetX = min;
        if (value < min)
            offsetX = Math.abs(value) + random;
        if (value >= max)
            offsetX = (max - value) - random;
        return offsetX;
    }
}