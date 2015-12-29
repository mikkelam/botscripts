package smelter;

import java.awt.*;

import agility.GameUtil;
import org.tbot.client.reflection.wrappers.WidgetWrapper;
import org.tbot.graphics.MouseTrail;
import org.tbot.graphics.SkillPaint;
import org.tbot.internal.AbstractScript;
import org.tbot.internal.Manifest;
import org.tbot.internal.ScriptCategory;
import org.tbot.internal.event.events.InventoryEvent;
import org.tbot.internal.event.events.MessageEvent;
import org.tbot.internal.event.listeners.InventoryListener;
import org.tbot.internal.event.listeners.MessageListener;
import org.tbot.internal.event.listeners.PaintListener;
import org.tbot.internal.handlers.LogHandler;
import org.tbot.methods.*;
import org.tbot.methods.Menu;
import org.tbot.methods.Random;
import org.tbot.methods.input.keyboard.*;
import org.tbot.methods.input.keyboard.Keyboard;
import org.tbot.methods.tabs.Inventory;
import org.tbot.methods.walking.Walking;
import org.tbot.methods.web.banks.WebBanks;
import org.tbot.wrappers.*;
import org.tbot.wrappers.Timer;

import javax.swing.*;


@Manifest(
        name = "smelter bro",
        authors = {
                "Saph"
        },
        version = 0.1,
        description = "smelts GOLD ore in edgeville",
        category = ScriptCategory.MONEY_MAKING
)
//Todo generalize this to more ores
public class MainHandler extends AbstractScript implements PaintListener, InventoryListener {

    private final GameUtil util = new GameUtil();
    private final Tile furnaceTile = new Tile(3109, 3499);
    private Timer smeltTimer;
    private SkillPaint sp = new SkillPaint();
    private MouseTrail mt = new MouseTrail();


    private WidgetChild goldWidget() {
        return Widgets.getWidget(311, 33);
    }

    private WidgetChild enterAmt() {
        return Widgets.getWidget(162, 32);
    }


    public boolean onStart() {
        return true;
    }


    private boolean needToSmelt() {
        return !goldWidget().isVisible() && !enterAmt().isVisible();
    }


    private boolean enterAmount() {
        if (enterAmt().isVisible()) {
            String amt = String.valueOf(Random.randomIntFromArray(121, 221, 112, 2121, 1212, 3213, 12312, 4123, 123, 123,211, 312, 123, 111, 312, 31,231,221, 3123));
            Keyboard.sendText(amt, true);
            smeltTimer = new Timer(4000);
            Time.sleepUntil(() -> enterAmt().isVisible(), 1200);
        }
        return true;
    }

    private boolean handleBanking() {
        if (Inventory.contains("Gold bar") ? Bank.depositAll("Gold bar") : Bank.withdrawAll("Gold ore")) {
            Time.sleepUntil(() -> Inventory.contains("Gold ore"), 1200);
        }
        return true;
    }

    private boolean interactWidget() {
        if (goldWidget().interact("Smelt X")) {
            Time.sleepUntil(() -> enterAmt().isVisible(), 1200);
        }
        return true;
    }


    @Override
    public int loop() {
        Player player = Players.getLocal();
        if (!player.isMoving() && (smeltTimer == null || !smeltTimer.isRunning())) {
            if (Inventory.contains("Gold ore")) {
                if (player.getLocation().equals(furnaceTile.getLocation()) ||  GameObjects.getNearest("Furnace").isOnScreen()) {
                    if (needToSmelt()) {
                        util.interact(GameObjects.getNearest("Furnace"), "Smelt");

                    } else {
                        interactWidget();
                        enterAmount();
                    }
                } else {
                    Walking.findPath(furnaceTile).traverse();
                }
            } else if (Bank.isOpen()) {
                handleBanking();
            } else {
                util.interact(GameObjects.getNearest("Bank booth"), "Bank");
            }
        }
        return Random.nextInt(198, 378);
    }


    @Override
    public void itemsAdded(InventoryEvent inventoryEvent) {
        if (inventoryEvent.getItem().getName().equals("Gold bar")) {
            smeltTimer.reset();
        }
    }

    @Override
    public void itemsRemoved(InventoryEvent inventoryEvent) {

    }

    @Override
    public void onRepaint(Graphics g) {
        this.sp.draw(g);
        this.mt.draw(g);
    }
}
















