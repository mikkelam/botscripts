package highalcher;

import org.tbot.graphics.MouseTrail;
import org.tbot.internal.AbstractScript;
import org.tbot.internal.Manifest;
import org.tbot.internal.ScriptCategory;
import org.tbot.internal.event.listeners.PaintListener;
import org.tbot.methods.*;
import org.tbot.methods.combat.magic.Magic;
import org.tbot.methods.combat.magic.SpellBooks;
import org.tbot.methods.tabs.Inventory;
import org.tbot.wrappers.WidgetChild;
import util.BotscriptsUtil;
import util.PlayerUtil;
import util.SkillTracker;

import java.awt.*;

@Manifest(
        name = "Martin's High Alcher",
        authors = {
                "Martin",
        },
        version = 0.2,
        description = "High alchs items",
        category = ScriptCategory.MAGIC
)
public class Main extends AbstractScript implements PaintListener {
    private final SkillTracker tracker = new SkillTracker(Skills.Skill.MAGIC);
    private final MouseTrail mt = new MouseTrail();
    private boolean alchingHasBegun = false;
    private WidgetChild highAlchWidget = Widgets.getWidget(218, 35);
    private WidgetChild magicTabWidget = Widgets.getWidget(548, 58);

    @Override
    public boolean onStart() {
        magicTabWidget.click();

        return super.onStart();
    }

    @Override
    public int loop() {
        if(Magic.hasRunesOrStaff(SpellBooks.Modern.HIGH_LEVEL_ALCHEMY) && Inventory.contains("Yew Longbow")) {
            if(highAlchWidget.isVisible()) {
                highAlchWidget.click();

                Time.sleepUntil(Inventory::isOpen, 3000);
                Time.sleep(300, 800);

                if(Inventory.isOpen()) {
                    Inventory.getFirst("Yew Longbow").click();

                    Time.sleepUntil(highAlchWidget::isVisible, 5000);
                    Time.sleep(250, 500);
                }
            }
            else {
                log("High alch logo not visible, opening magic tab");
                magicTabWidget.click();
            }
        }
        else {
            log("Don't have staff and/or runes to high alch. Stopping script and put items in inventory.");
            BotscriptsUtil.pauseScript();
        }

        return Random.nextInt(250, 750);
    }

    @Override
    public void onRepaint(Graphics graphics) {
        mt.draw(graphics);
        mt.setColor(Color.ORANGE);
        BotscriptsUtil.showSimpleStats(graphics, tracker);
    }
}
