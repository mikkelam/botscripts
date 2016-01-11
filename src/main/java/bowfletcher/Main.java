package bowfletcher;

import org.tbot.graphics.MouseTrail;
import org.tbot.internal.AbstractScript;
import org.tbot.internal.Manifest;
import org.tbot.internal.ScriptCategory;
import org.tbot.internal.event.listeners.PaintListener;
import org.tbot.methods.*;
import org.tbot.methods.input.keyboard.Keyboard;
import org.tbot.methods.tabs.Inventory;
import org.tbot.wrappers.Item;
import org.tbot.wrappers.WidgetChild;
import util.Antiban;
import util.BotscriptsUtil;
import util.PlayerUtil;
import util.SkillTracker;

import javax.swing.*;
import java.awt.*;

@Manifest(
        name = "Martin's Fletcher",
        authors = {
                "Martin",
        },
        version = 0.1,
        description = "Creates bows (u), because bow fletching is boring af",
        category = ScriptCategory.FLETCHING
)
public class Main extends AbstractScript implements PaintListener {
    private final SkillTracker tracker = new SkillTracker(Skills.Skill.FLETCHING);
    private boolean makeShortbow;
    private String selectedBowType;
    private static final String[] bowTypes = new String[] { "Shortbow", "Longbow", "Oak", "Willow", "Maple", "Yew", "Magic" };
    private static final String[] bowStyles = new String[] { "Shortbow", "Longbow" };
    private WidgetChild shortBow = Widgets.getWidget(304, 8);
    private WidgetChild longBow  = Widgets.getWidget(304, 12);
    private WidgetChild inputBox = Widgets.getWidget(162, 32);
    private WidgetChild chatBox = Widgets.getWidget(162, 41);
    private WidgetChild levelUp = Widgets.getWidget(233, 1);

    @Override
    public boolean onStart() {
        log("Martin's Fletcher has started");

        selectedBowType = (String)JOptionPane.showInputDialog(null, "Please choose a bow", "Test",
                JOptionPane.QUESTION_MESSAGE, null, bowTypes, bowTypes[2]);

        String bowStyleSelection = (String)JOptionPane.showInputDialog(null, "Please choose a bow type", "Test",
                JOptionPane.QUESTION_MESSAGE, null, bowStyles, bowStyles[0]);

        makeShortbow = bowStyleSelection.equals(bowStyles[0]);

        return super.onStart();
    }

    @Override
    public int loop() {
        if(Random.nextInt(1, 20) == 1)
            Antiban.doSomethingRandom();

        if(PlayerUtil.isIdle()) { // Wait until player is idle
            if(Inventory.contains("Knife") && Inventory.contains(logForBowType(selectedBowType))) {
                fletch();
            }
            else
                bankGoods();
        }

        return Random.nextInt(700, 1500);
    }

    private void fletch() {
        Item knife = Inventory.getFirst("Knife");
        Item logs = Inventory.getFirst(logForBowType(selectedBowType));

        if(chatBox.isVisible() || levelUp.isVisible())
            Inventory.useItemOn(knife, logs);

        if(shortBow.isVisible() || longBow.isVisible()) {
            if(makeShortbow)
                shortBow.interact("Make X");
            else
                longBow.interact("Make X");

            BotscriptsUtil.sleepConditionWithExtraWait(inputBox::isVisible, 0, 500);
        }

        if(inputBox.isVisible()) {
            Keyboard.sendText(String.valueOf(Random.nextInt(27, 999)), true);
        }
    }

    private void bankGoods() {
        Bank.openNearestBank();

        BotscriptsUtil.sleepConditionWithExtraWait(Bank::isOpen, 0, 500);

        if(Bank.isOpen()) {
            if(!Inventory.contains("Knife"))
                Bank.withdraw("Knife", 1);

            Bank.depositAllExcept("Knife");

            Time.sleep(Random.nextInt(200, 500));

            Bank.withdrawAll(logForBowType(selectedBowType));

            while(Bank.isOpen())
                Bank.close();
        }

        BotscriptsUtil.sleepConditionWithExtraWait(() -> !Bank.isOpen(), 0, 500);
    }

    private String logForBowType(String bowType) {
        if(bowType.equals("Shortbow") || bowType.equals("Longbow"))
            return "Logs";
        return bowType + " logs";
    }

    private final MouseTrail mt = new MouseTrail();

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
