package sharkfisher;

import org.tbot.client.*;
import org.tbot.graphics.MouseTrail;
import org.tbot.internal.AbstractScript;
import org.tbot.internal.Manifest;
import org.tbot.internal.ScriptCategory;
import org.tbot.internal.event.events.InventoryEvent;
import org.tbot.internal.event.listeners.InventoryListener;
import org.tbot.internal.event.listeners.PaintListener;
import org.tbot.methods.*;
import org.tbot.methods.tabs.Inventory;
import org.tbot.methods.web.banks.WebBank;
import org.tbot.wrappers.*;
import org.tbot.wrappers.Character;
import org.tbot.wrappers.NPC;
import org.tbot.wrappers.Player;
import util.*;

import java.awt.*;

@Manifest(
        name = "Martin's Fisher",
        authors = {
                "Martin",
        },
        version = 0.1,
        description = "Fishes at fishing guild. So far, only harpoons tuna and swordfish",
        category = ScriptCategory.FISHING
)
public class Main extends AbstractScript implements PaintListener, InventoryListener {
    private final SkillTracker tracker = new SkillTracker(Skills.Skill.FISHING);
    private final MouseTrail mt = new MouseTrail();

    @Override
    public boolean onStart() {
        log("Martin's Fisher has started");
        return super.onStart();
    }

    @Override
    public int loop() {
        if(Random.nextInt(1, 20) == 1)
            Antiban.doSomethingRandom();

        if(PlayerUtil.isIdle()) {
            if(Inventory.isFull())
                bankGoods();
            else {
                Time.sleep(200, 1000);
                fish();
            }

        }

        return Random.nextInt(500, 1500);
    }

    public void fish() {
        NPC spot = Npcs.getNearest(o -> o.getName().equals("Fishing spot") && o.hasAction("Net"));


        BotscriptsUtil.interact(spot, "Harpoon", new Tile(2598, 3421, 0));
        Time.sleep(1000, 2500);

        try {
            BotscriptsUtil.sleepConditionWithExtraWait(() -> spot.distance() < 2, 200, 2500);
        }
        catch (NullPointerException e) {
            log("Null pointer in sleep fishing method");
        }
    }

    public void bankGoods() {
        Camera.rotateRandomly();

        Bank.openNearestBank();

        BotscriptsUtil.sleepConditionWithExtraWait(Bank::isOpen, 0, 500);

        if(Bank.isOpen()) {
            Bank.depositAll();

            Time.sleep(Random.nextInt(200, 500));

            while(Bank.isOpen())
                Bank.close();
        }

        BotscriptsUtil.sleepConditionWithExtraWait(() -> !Bank.isOpen(), 0, 500);
    }

    @Override
    public void onRepaint(Graphics graphics) {
        mt.draw(graphics);
        mt.setColor(Color.ORANGE);
        graphics.drawString("Time running: " + tracker.getFormattedTimeTracking(), 8, 15);
        graphics.drawString("XP/H: " + tracker.getExperiencePerHour(), 8, 30);
        graphics.drawString("Time to level: " + tracker.getTimeToLevel(), 8, 45);
        graphics.drawString("Level: " + tracker.getCurrentLevel(), 8, 60);
    }

    @Override
    public void itemsAdded(InventoryEvent inventoryEvent) {
        // 70% chance to open inventory
        if(Random.nextInt(1, 10) > 3 ) {
            if(!Inventory.isOpen())
                Widgets.openTab(Widgets.TAB_INVENTORY);
        }
    }

    @Override
    public void itemsRemoved(InventoryEvent inventoryEvent) {

    }
}
