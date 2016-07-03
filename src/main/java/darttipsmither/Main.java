package darttipsmither;

import org.tbot.graphics.MouseTrail;
import org.tbot.internal.AbstractScript;
import org.tbot.internal.Manifest;
import org.tbot.internal.ScriptCategory;
import org.tbot.internal.event.listeners.PaintListener;
import org.tbot.methods.*;
import org.tbot.methods.tabs.Inventory;
import org.tbot.methods.walking.Walking;
import org.tbot.wrappers.GameObject;
import org.tbot.wrappers.Tile;
import org.tbot.wrappers.WidgetChild;
import util.BotscriptsUtil;
import util.PlayerUtil;
import util.SkillTracker;

import java.awt.*;

@Manifest(
        name = "Martin's Dart Tip Smither",
        authors = {
                "Martin",
        },
        version = 0.1,
        description = "Smiths dart tips at Varrock West bank anvil",
        category = ScriptCategory.SMITHING
)
public class Main extends AbstractScript implements PaintListener {
    private final SkillTracker tracker = new SkillTracker(Skills.Skill.SMITHING);
    private final MouseTrail mt = new MouseTrail();
    private String dartsToSmith;
    private String barRequired;
    private WidgetChild smithWidget = Widgets.getWidget(312, 0);
    private WidgetChild dartWidget = Widgets.getWidget(312, 23);
    private WidgetChild makeAmount = Widgets.getWidget(162, 33);
    private WidgetChild levelUp = Widgets.getWidget(233, 1);
    private Tile anvilTile = new Tile(3188, 3427);

    @Override
    public boolean onStart() {
        log("Martin's Smither has started");
        dartsToSmith = "Mithril dart tip";
        barRequired = "Mithril bar";
        return super.onStart();
    }

    @Override
    public int loop() {
        if(Random.nextInt(1, 10) == 1)
            Camera.rotateAndTiltRandomly();

        if(PlayerUtil.isIdle()) {
            if(Inventory.contains(barRequired))
                smith();
            else {
                getBars();
                Time.sleep(200, 1000);
            }
        }

        return Random.nextInt(500, 1500);
    }

    private void getBars() {
        Camera.rotateRandomly();

        Bank.openNearestBank();

        BotscriptsUtil.sleepConditionWithExtraWait(Bank::isOpen, 0, 500);

        if(Bank.isOpen()) {
            Bank.depositAllExcept("Hammer", dartsToSmith);

            if(!Inventory.contains("Hammer")) {
                Bank.withdraw("Hammer", 1);
                Time.sleep(Random.nextInt(200, 500));
            }

            if(!Bank.contains(barRequired)) {
                log("Bank has run out of " + barRequired + "s. Stopping script.");
                BotscriptsUtil.pauseScript();
            }

            Bank.withdrawAll(barRequired);

            Time.sleep(Random.nextInt(200, 500));

            while(Bank.isOpen())
                Bank.close();
        }

        BotscriptsUtil.sleepConditionWithExtraWait(() -> !Bank.isOpen(), 0, 500);
    }

    private void smith() {
        if(Players.getLocal().getLocation() != anvilTile) {
            if(Random.nextInt(1, 3) == 1)
                Walking.walkTileMM(anvilTile, 3, 3);
            else
                Walking.walkTileOnScreen(anvilTile);
        }

        Time.sleep(Random.nextInt(500, 1000));
        GameObject anvil = GameObjects.getNearest(o -> o.getName().equals("Anvil"));

        anvil.interact("Smith");

        Time.sleepUntil(smithWidget::isVisible, 5000);

        if(smithWidget.isVisible()) {
            dartWidget.interact("Smith X sets");

            Time.sleepUntil(makeAmount::isVisible, 5000);

            Time.sleep(BotscriptsUtil.secToMs(2), BotscriptsUtil.secToMs(10));

            if(makeAmount.isVisible()) {
                Time.sleep(BotscriptsUtil.secToMs(2), BotscriptsUtil.secToMs(4));
                org.tbot.methods.input.keyboard.Keyboard.sendText("200", true);

                Time.sleepUntil(() -> Inventory.getCount(barRequired) == 0, BotscriptsUtil.secToMs(100));
            }
        }

        Time.sleep(100, 500);
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
