package burthorpecooker;

import org.tbot.graphics.MouseTrail;
import org.tbot.internal.AbstractScript;
import org.tbot.internal.Manifest;
import org.tbot.internal.ScriptCategory;
import org.tbot.internal.event.listeners.PaintListener;
import org.tbot.methods.*;
import org.tbot.methods.tabs.Inventory;
import org.tbot.wrappers.GameObject;
import org.tbot.wrappers.Item;
import org.tbot.wrappers.NPC;
import org.tbot.wrappers.WidgetChild;
import util.Antiban;
import util.BotscriptsUtil;
import util.SkillTracker;

import java.awt.*;


@Manifest(
        name = "Martin's Cooker",
        authors = {
                "Martin",
        },
        version = 0.1,
        description = "Cooks at Burhtrope, the only real place to cook",
        category = ScriptCategory.COOKING
)
public class Main extends AbstractScript implements PaintListener {
    private final SkillTracker tracker = new SkillTracker(Skills.Skill.COOKING);
    private final MouseTrail mt = new MouseTrail();
    private WidgetChild foodMakeWidget = Widgets.getWidget(307, 6);
    private WidgetChild chatBoxWidget = Widgets.getWidget(162, 41);
    private WidgetChild levelUp = Widgets.getWidget(233, 1);
    private String itemToCook;


    @Override
    public boolean onStart() {
        itemToCook = "swordfish";
        return super.onStart();
    }

    @Override
    public int loop() {
        GameObject fire = GameObjects.getNearest("Fire");
        NPC benedict = Npcs.getNearest("Emerald Benedict");

        if(Random.nextInt(1, 20) == 1)
            Antiban.doSomethingRandom();

        if(Inventory.isEmpty() || !Inventory.contains("Raw " + itemToCook)) {
            if(Inventory.hasItemSelected())
                Inventory.deselectItem();

            log("Banking");
            benedict.interact("Bank");

            Time.sleepUntil(Bank::isOpen, 5000);
            Time.sleep(350, 1500);

            if(Bank.isOpen()) {
                if(!Inventory.isEmpty()) {
                    Bank.depositAll();
                    Time.sleep(200, 500);
                }

                if(Bank.contains("Raw " + itemToCook)) {
                    Bank.withdrawAll("Raw " + itemToCook);
                    Time.sleep(500, 1500);
                }
                else {
                    log("Ran out of chosen raw items to cook. Pausing script");
                    BotscriptsUtil.pauseScript();
                }

                while(Bank.isOpen())
                    Bank.close();
            }
        }
        else {
            if(foodMakeWidget.isVisible()) {
                log("Cooking all");
                Time.sleep(500, 3000);



                foodMakeWidget.interact("Cook all");
                Time.sleepUntil(() -> !foodMakeWidget.isVisible(), 10000);
                Time.sleep(200, 300);

                Time.sleepUntil(() -> !Inventory.contains("Raw " + itemToCook) || levelUp.isVisible(), 100000);

                log("All done cooking. Time to bank!");
            }
            else {
                log("Food make widget not visible");
                Inventory.useItemOn("Raw " + itemToCook, fire);
                Time.sleepUntil(foodMakeWidget::isVisible, 3000);
                Time.sleep(500, 3000);
            }

        }

        return Random.nextInt(1000, 2400);
    }

    @Override
    public void onRepaint(Graphics graphics) {
        mt.draw(graphics);
        mt.setColor(Color.ORANGE);
        BotscriptsUtil.showSimpleStats(graphics, tracker);
    }
}
