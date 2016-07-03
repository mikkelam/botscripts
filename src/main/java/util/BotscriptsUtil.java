package util;

import org.tbot.bot.TBot;
import org.tbot.internal.handlers.LogHandler;
import org.tbot.methods.*;
import org.tbot.methods.tabs.Inventory;
import org.tbot.util.Condition;
import org.tbot.wrappers.*;
import org.tbot.methods.walking.Path;
import org.tbot.methods.walking.Walking;
import sun.rmi.runtime.Log;

import java.awt.*;

public final class BotscriptsUtil {
    public static GameObject getSecondNearest(GameObject gb) {
        return GameObjects.getNearest(o -> o.getUID() != gb.getUID() && o.getName().equals(gb.getName()));
    }

    /**
     * A more refined intraction method. It will walk to a nearby tile if the given {GameObject} is null, or
     * walk to it, if it is not on the screen.
     *
     * @param o          The {GameObject} you wish to interact with
     * @param action     The right-click action "attack", "light", etc. you wish to do on {o}
     * @param nearbyTile A {Tile} close to {o}
     * @return The result of interacting with an object.
     */
    public static <T extends Interactable & Locatable> boolean interact(T o, final String action, final Tile nearbyTile) {
        // If object is null, create path to nearby tile, and return traverse
        if (o == null) {
            Path path = Walking.findPath(nearbyTile);
            return path != null && path.traverse();
        }

        if (!o.isOnScreen()) {
            Path path = Walking.findPath(o.getLocation().getRandomized(1));

            return path != null && path.traverse();
        }
        return o.interact(action);
    }

    public static void unpauseScript() {
        TBot.getBot().getScriptHandler().getScript().setPaused(false);
    }

    public static void pauseScript() {
        TBot.getBot().getScriptHandler().getScript().setPaused(true);
    }

    public static void sleepConditionWithExtraWait(Condition a, int min, int max) {
        Time.sleepUntil(a, Random.nextInt(min, max));
        Time.sleep(Random.nextInt(min, max));
    }

    public static int minToMilli(int min) {
        return secToMs(min * 60);
    }

    public static int secToMs(int sec) {
        return sec * 1000;
    }

    public static void showSimpleStats(Graphics graphics, SkillTracker tracker) {
        graphics.drawString("Time running: " + tracker.getFormattedTimeTracking(), 8, 15);
        graphics.drawString("XP/H: " + tracker.getExperiencePerHour(), 8, 30);
        graphics.drawString("Time to level: " + tracker.getTimeToLevel(), 8, 45);
        graphics.drawString("Level: " + tracker.getCurrentLevel(), 8, 60);
    }

    public static WidgetChild chatBoxWidget() {
        return Widgets.getWidget(162, 41);
    }

    /**
     * Returns the interact box that comes when you use items on each other
     *
     * @return an interactable widget
     */
    public static WidgetChild interactWidget() {
        return Widgets.getWidget(309, 6);
    }

    public static WidgetChild levelUpWidget() {
        return Widgets.getWidget(233, 1);
    }

    public static void BankAllAndWithdraw(int numToWithDraw, String... withdrawItems) {
        Bank.openNearestBank();

        BotscriptsUtil.sleepConditionWithExtraWait(Bank::isOpen, 0, 500);

        if (Bank.isOpen()) {
            Time.sleep(200, 500);

            if (!Inventory.isEmpty()) {
                Bank.depositAll();
            }

            if (!Bank.containsAll(withdrawItems)) {
                LogHandler.log("Bank does not contain all items required, pausing script");
                BotscriptsUtil.pauseScript();
            }

            for (String item : withdrawItems) {
                LogHandler.log("Withdrawing " + numToWithDraw + " " + item + "s");

                while (!Inventory.contains(item)) {
                    Bank.withdraw(item, numToWithDraw);
                    Time.sleepUntil(() -> Inventory.contains(item), 5000);
                    Time.sleep(2000, 3500);
                }
            }

            while (Bank.isOpen())
                Bank.close();

            Time.sleep(250, 2000);
        }
    }
}
