package humidifier;

import org.tbot.graphics.MouseTrail;
import org.tbot.internal.AbstractScript;
import org.tbot.internal.Manifest;
import org.tbot.internal.ScriptCategory;
import org.tbot.internal.event.listeners.PaintListener;
import org.tbot.methods.Random;
import org.tbot.methods.Skills;
import org.tbot.methods.Time;
import org.tbot.methods.Widgets;
import org.tbot.methods.combat.magic.Magic;
import org.tbot.methods.combat.magic.SpellBooks;
import org.tbot.methods.tabs.Inventory;
import org.tbot.wrappers.WidgetChild;
import util.*;

import java.awt.*;

@Manifest(
        name = "Martin's Clay Humidifier",
        authors = {
                "Martin",
        },
        version = 0.1,
        description = "Humidifies clay using the lunar spellbook for profits",
        category = ScriptCategory.MONEY_MAKING
)
public class Main extends AbstractScript implements PaintListener {
    private final SkillTracker tracker = new SkillTracker(Skills.Skill.MAGIC);
    private final WidgetChild magicTabWidget = Widgets.getWidget(548, 58);
    private final WidgetChild humidifySpell = WidgetsUtil.humidifiyWidget();
    private final MouseTrail mt = new MouseTrail();

    @Override
    public boolean onStart() {
        log("Make sure to have astral runes in inventory.");
        return super.onStart();
    }

    @Override
    public int loop() {
        if(Random.nextInt(1, 20) == 1)
            Antiban.doSomethingRandom();

        if(PlayerUtil.isIdle()) {
            if(Inventory.contains("Astral rune") && Inventory.contains("Clay")) {
                log("Humidifying");
                humidify();
            }
            else {
                log("Getting items from bank");
                InventoryUtil.BankAllAndWithdraw((i) -> i.getName().equals("Soft clay"), 99, "Clay");
            }
        }

        return Random.nextInt(1000, 2400);
    }

    private void humidify() {
        if(!Magic.hasRunesOrStaff(SpellBooks.Lunar.HUMIDIFY)) {
            log("Do not have runes or staff for humidify. Make sure they are in inventory");
            BotscriptsUtil.pauseScript();
        }

        if(humidifySpell.isVisible()) {
            humidifySpell.click();

            Time.sleep(1000, 2000);
        } else {
            magicTabWidget.click();
        }
    }

    @Override
    public void onRepaint(Graphics graphics) {
        mt.draw(graphics);
        mt.setColor(Color.ORANGE);
        BotscriptsUtil.showSimpleStats(graphics, tracker);
    }
}
