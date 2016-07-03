package bowstringer;

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

import javax.swing.*;
import java.awt.*;

@Manifest(
        name = "Martin's Bow Stringer",
        authors = {
                "Martin",
        },
        version = 0.1,
        description = "Strings bows, because  fletching is boring af",
        category = ScriptCategory.FLETCHING
)
public class Main extends AbstractScript implements PaintListener {
    private final SkillTracker tracker = new SkillTracker(Skills.Skill.FLETCHING);
    private String selectedBowType, selectedBowStyle;
    private static final String[] bowTypes = new String[] { "Shortbow", "Longbow", "Oak", "Willow", "Maple", "Yew", "Magic" };
    private static final String[] bowStyles = new String[] { "Shortbow", "Longbow" };
    private WidgetChild bowBox = WidgetsUtil.interactWidget();
    private WidgetChild chatBox = WidgetsUtil.chatBoxWidget();
    private WidgetChild levelUp = WidgetsUtil.levelUpWidget();
    private final MouseTrail mt = new MouseTrail();

    @Override
    public boolean onStart() {
        log("Martin's Bow Stringer has started");

        selectedBowType = (String)JOptionPane.showInputDialog(null, "Please choose a bow", "Test",
                JOptionPane.QUESTION_MESSAGE, null, bowTypes, bowTypes[4]);

        selectedBowStyle = (String)JOptionPane.showInputDialog(null, "Please choose a bow type", "Test",
                JOptionPane.QUESTION_MESSAGE, null, bowStyles, bowStyles[1]);

        return super.onStart();
    }


    @Override
    public int loop() {
        if(Random.nextInt(1, 20) == 1)
            Antiban.doSomethingRandom();

        if(PlayerUtil.isIdle()) { // Wait until player is idle
            if(Inventory.contains(getSelectedUnsrungBow()) && Inventory.contains("Bow string"))
                string();
            else
                InventoryUtil.BankAllAndWithdraw(14, getSelectedUnsrungBow(), "Bow String");
        }

        return Random.nextInt(700, 1500);
    }

    private void string() {
        Item bows = Inventory.getLast(getSelectedUnsrungBow());
        Item bowStrings = Inventory.getFirst("Bow string");


        if(chatBox.isVisible() || levelUp.isVisible()) {
            Inventory.useItemOn(bowStrings, bows);
        }

        Time.sleepUntil(bowBox::isVisible, 5000);

        if(bowBox.isVisible()) {
            bowBox.interact("Make All");
            Time.sleep(BotscriptsUtil.secToMs(15), BotscriptsUtil.secToMs(20));
        }
    }

    public String getSelectedUnsrungBow() {
        if(selectedBowType.equals("Shortbow") || selectedBowType.equals("Longbow"))
            return selectedBowType;
        else
            return selectedBowType + " " + selectedBowStyle.toLowerCase() + " (u)";
    }

    @Override
    public void onRepaint(Graphics graphics) {
        mt.draw(graphics);
        mt.setColor(Color.ORANGE);
        BotscriptsUtil.showSimpleStats(graphics, tracker);
    }
}
