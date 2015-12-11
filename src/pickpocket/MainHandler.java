package pickpocket;

import java.awt.Graphics;
import javax.swing.JOptionPane;

import org.tbot.graphics.MouseTrail;
import org.tbot.graphics.SkillPaint;
import org.tbot.internal.AbstractScript;
import org.tbot.internal.Manifest;
import org.tbot.internal.ScriptCategory;
import org.tbot.internal.event.events.MessageEvent;
import org.tbot.internal.event.listeners.MessageListener;
import org.tbot.internal.event.listeners.PaintListener;
import org.tbot.methods.*;
import org.tbot.methods.tabs.Inventory;
import org.tbot.methods.walking.Path;
import org.tbot.methods.walking.Walking;
import org.tbot.wrappers.Item;
import org.tbot.wrappers.NPC;
import org.tbot.wrappers.Player;

@Manifest(
        name = "AIO Pickpocket & eat",
        authors = {
                "Kocke",
                "Saph"
        },
        version = 1.1,
        description = "Based on Kockes thiever",
        category = ScriptCategory.THIEVING
)
/**
 * This script is missing a banking mechanism
 */
public class MainHandler extends AbstractScript implements MessageListener, PaintListener {

    String pick_name;
    String food;

    public boolean onStart() {
        this.pick_name = JOptionPane.showInputDialog("Enter exact name of the NPC you want to pickpocket!",
                null);
        this.food = JOptionPane.showInputDialog("Enter exact food you want to eat",
                null);
        Mouse.setSpeed(Random.nextInt(45,
                55));
        return true;
    }

    public int loop() {
        NPC pick = Npcs.getNearest(this.pick_name);
        int playerState = Players.getLocal().getAnimation();
        if ((pick != null) && (pick.isOnScreen()) && (playerState == -1)) {
            pick.interact("Pickpocket");

            Time.sleep(300,
                    500);
        } else if (playerState != -1) {
            if (needToEat()) {
                handleFood();
            }
            Time.sleep(50,
                    100);
        } else {
            Path wp = Walking.findPath(pick.getLocation());
            if (wp != null) {
                wp.traverse();
            }
        }
        return Random.nextInt(500,
                850);
    }

    private SkillPaint sp = new SkillPaint();
    private MouseTrail mt = new MouseTrail();

    public void antiban() {
    }

    public void messageReceived(MessageEvent messageEvent) {
    }

    public void onRepaint(Graphics g) {
        this.sp.draw(g);
        this.mt.draw(g);
    }

    public boolean needToEat() {
        return (int) (((double) Skills.getCurrentLevel(Skills.Skill.HITPOINTS) / (double) Skills
                .getRealLevel(Skills.Skill.HITPOINTS)) * 100) < 60;
    }

    private void handleFood() {
        Item food = Inventory.getItemClosestToMouse(this.food);
        if (Inventory.isOpen()) {
            if (food != null) {
                int currentHP = Skills.getCurrentLevel(Skills.Skill.HITPOINTS);
                if (food.interact("Eat"))
                    Time.sleepUntil(() -> (Skills.getCurrentLevel(Skills.Skill.HITPOINTS) > currentHP), Random.nextInt(1200, 1650));
            }
        } else Inventory.openTab();

    }

}