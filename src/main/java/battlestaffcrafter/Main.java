package battlestaffcrafter;

import org.tbot.graphics.MouseTrail;
import org.tbot.internal.AbstractScript;
import org.tbot.internal.Manifest;
import org.tbot.internal.ScriptCategory;
import org.tbot.internal.event.listeners.PaintListener;
import org.tbot.methods.*;
import org.tbot.methods.tabs.Inventory;
import org.tbot.wrappers.Item;
import org.tbot.wrappers.WidgetChild;
import util.*;

import java.awt.*;

@Manifest(
        name = "Martin's Battlestaff Crafter",
        authors = {
                "Martin",
        },
        version = 0.1,
        description = "Makes water battlestaves (lvls 54-63)",
        category = ScriptCategory.CRAFTING
)
public class Main extends AbstractScript implements PaintListener {
    private final SkillTracker tracker = new SkillTracker(Skills.Skill.CRAFTING);
    private final MouseTrail mt = new MouseTrail();
    private final WidgetChild craftBox = WidgetsUtil.interactWidget();
    private final WidgetChild chatBox = WidgetsUtil.chatBoxWidget();
    private final WidgetChild levelUp = WidgetsUtil.levelUpWidget();

    @Override
    public int loop() {
        if(Random.nextInt(1, 20) == 1)
            Antiban.doSomethingRandom();

        if(PlayerUtil.isIdle()) {
            if(Inventory.contains("Water orb") && Inventory.contains("Battlestaff")) {
                log("Crafting");
                craft();
            }
            else {
                log("Getting items from inventory");
                InventoryUtil.BankAllAndWithdraw(14, "Water orb", "Battlestaff");
            }
        }

        return Random.nextInt(1000, 2400);
    }

    private void craft() {
        Item orb = Inventory.getLast("Water orb");
        Item battlestaff = Inventory.getFirst("Battlestaff");

        if(chatBox.isVisible() || levelUp.isVisible())
            Inventory.useItemOn(orb, battlestaff);

        Time.sleepUntil(craftBox::isVisible, 5000);

        if(craftBox.isVisible()) {
            craftBox.interact("Make All");
            Time.sleep(BotscriptsUtil.secToMs(15), BotscriptsUtil.secToMs(20));
        }
    }

    @Override
    public void onRepaint(Graphics graphics) {
        mt.draw(graphics);
        mt.setColor(Color.ORANGE);
        BotscriptsUtil.showSimpleStats(graphics, tracker);
    }
}
