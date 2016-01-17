package pickpocket;

import org.tbot.concurrency.*;
import org.tbot.graphics.MouseTrail;
import org.tbot.graphics.SkillPaint;
import org.tbot.internal.*;
import org.tbot.internal.event.events.MessageEvent;
import org.tbot.internal.event.listeners.InventoryListener;
import org.tbot.internal.event.listeners.MessageListener;
import org.tbot.internal.event.listeners.PaintListener;
import org.tbot.methods.*;
import org.tbot.methods.Menu;
import org.tbot.methods.Random;
import org.tbot.methods.combat.magic.Magic;
import org.tbot.methods.combat.magic.SpellBooks;
import org.tbot.methods.tabs.Equipment;
import org.tbot.methods.tabs.Inventory;
import org.tbot.methods.walking.Walking;
import org.tbot.wrappers.NPC;
import org.tbot.wrappers.Player;
import util.PlayerUtil;
import util.SkillTracker;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;


@Manifest(
        name = "Pickpocket <:(",
        authors = {
                "Saph"
        },
        version = 0.1,
        description = "Pickpockets the master farmer in Draynor",
        category = ScriptCategory.THIEVING
)
public class Main extends AbstractScript implements PaintListener {
    String food = "";
    final SkillTracker tracker = new SkillTracker(Skills.Skill.THIEVING);
    MouseTrail mouseTrail = new MouseTrail();
    int currentHP;


    public boolean onStart() {
        JLabel info = new JLabel("You should be in draynor, with food in bank");


        JTextField jtfood = new JTextField("Jug of wine");

        Object[] content = {
                "", info,
                "Enter exact name of food", jtfood
        };
        int option = JOptionPane.showConfirmDialog(null,content, "Enter all values", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            food = jtfood.getText();
        }
        log(food);
        currentHP = PlayerUtil.currentHP();
        return true;
    }



    public int loop() {



        if (!PlayerUtil.isMoving()) {
            if (Inventory.contains(food) && Inventory.getEmptySlots() > 0) {
                if (currentHP != PlayerUtil.currentHP()) {
                    steal();
                    currentHP = PlayerUtil.currentHP();

                    if (PlayerUtil.needToEat()) {
                        Inventory.getFirst(food).click();
                    }

                    return Random.nextInt(3000, 3500);
                }

                steal();

            } else {
                banking();
                return Random.nextInt(500, 1500);
            }
        }
        return Random.nextInt(100, 300);
    }
    public void steal(){
        NPC npc = Npcs.getNearest("Master Farmer");
        if (npc.isOnScreen()){
            Mouse.move(npc.toScreen().x + Random.nextInt(1,3),npc.toScreen().y - Random.nextInt(1,3));
            if(Menu.getUpText().contains("Pickpocket" + " " + npc.getName())) {
                Mouse.click(true);
            }
        }
        else{
            Walking.walkTileMM(npc.getLocation());
        }

    }

    public void banking(){
        if (Bank.isOpen()){

            Bank.depositAll();
            Time.sleep(200,400);

            if (Bank.contains(food))
                Bank.withdraw(food,10);
            else
                System.exit(0);

        }
        else{
            Bank.openNearestBank();

        }
    }

    public void onRepaint(Graphics g) {
        this.mouseTrail.draw(g);
        g.drawString("Time running: " + tracker.getFormattedTimeTracking(), 8, 15);
        g.drawString("XP/H: " + tracker.getExperiencePerHour(), 8, 30);
        g.drawString("Time to level: " + tracker.getTimeToLevel(), 8, 45);
        g.drawString("Level: " + tracker.getCurrentLevel(), 8, 60);
    }



}




















