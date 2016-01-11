package sharkfisher;

import org.tbot.graphics.MouseTrail;
import org.tbot.internal.AbstractScript;
import org.tbot.internal.Manifest;
import org.tbot.internal.ScriptCategory;
import org.tbot.internal.event.listeners.PaintListener;
import org.tbot.methods.*;
import org.tbot.methods.tabs.Inventory;
import org.tbot.wrappers.GameObject;
import org.tbot.wrappers.GroundItem;
import org.tbot.wrappers.NPC;
import org.tbot.wrappers.Tile;
import util.Antiban;
import util.BotscriptsUtil;
import util.PlayerUtil;
import util.SkillTracker;

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
public class Main extends AbstractScript implements PaintListener {
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
            else
                fish();
        }

        return Random.nextInt(500, 2500);
    }

    public void fish() {
        NPC spot = Npcs.getNearest(o -> o.getName().equals("Fishing spot") && o.hasAction("Cage"));

        BotscriptsUtil.interact(spot, "Harpoon", new Tile(2598, 3421, 0));
    }

    public void bankGoods() {
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
}
