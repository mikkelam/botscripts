package util;

import org.tbot.methods.Camera;
import org.tbot.methods.Mouse;
import org.tbot.methods.Random;

public final class Antiban {
    private Antiban() {

    }

    /**
     * Executes a random action, such as opening a tab, moving the camera or mouse, etc.
     * @return The choice made, if any.
     */
    public static AntibanChoice doSomethingRandom() {
        switch(Random.nextInt(0, 10)) {
            case 1:
                openRandomTab();
                return AntibanChoice.OPEN_RANDOM_TAB;
            case 2:
                Camera.rotateAndTiltRandomly();
                return AntibanChoice.CAMERA_TILT;
            case 3:
                Mouse.moveRandomly();
                return AntibanChoice.MOUSE_MOVE;
        }
        return AntibanChoice.NO_CHOICE;
    }

    /**
     * This method simply opens a random tab from all selectable tabs on the right
     * @return Whether the tab was opened or not
     */
    public static boolean openRandomTab() {
        int randomTab = Random.randomFromArray(Widgets2.tabIDs());
        return Widgets2.openTab(randomTab);
    }
}
