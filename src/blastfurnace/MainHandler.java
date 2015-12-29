package blastfurnace;

import java.awt.Graphics;

import org.tbot.client.*;
import org.tbot.client.Player;
import org.tbot.graphics.MouseTrail;
import org.tbot.graphics.SkillPaint;
import org.tbot.internal.AbstractScript;
import org.tbot.internal.Manifest;
import org.tbot.internal.ScriptCategory;
import org.tbot.internal.event.events.MessageEvent;
import org.tbot.internal.event.listeners.InventoryListener;
import org.tbot.internal.event.listeners.MessageListener;
import org.tbot.internal.event.listeners.PaintListener;
import org.tbot.methods.*;
import org.tbot.methods.tabs.Inventory;
import org.tbot.methods.walking.Walking;
import org.tbot.util.Filter;
import org.tbot.wrappers.*;
import org.tbot.wrappers.GameObject;

import javax.swing.*;

@Manifest(
        name = "Blast furnace",
        authors = {
                "Saph"
        },
        version = 1.1,
        description = "Plays blast furnace",
        category = ScriptCategory.THIEVING
)
/**
 * newly started, it sucks
 */
public class MainHandler extends AbstractScript implements MessageListener, PaintListener {
    String ore;
    int coaladded = 0;
    boolean smelting = false;
    boolean coalBagFilled = false;


    private void handleBanking(String withdraw) {
        if (Bank.isOpen()) {
            if (Inventory.isFull())
                Bank.depositAllExcept("Coal bag");

            Item ore = Bank.getItem(withdraw);
            Mouse.move(ore.getRandomPoint());
            Time.sleep(400, 700);
            ore.interact("Withdraw-All");
            Time.sleep(300, 500);

        }
        else{
            GameObject bank = GameObjects.getTopAt(new Tile(1948,4956,0));
            if (bank.isOnScreen()){
                bank.interact("Use");
            }
            else {
                Walking.walkTileMM(bank.getLocation(),Random.nextInt(2),Random.nextInt(2));
                Camera.rotateRandomly();
            }
        }
    }

    public boolean closeToBank(){
        GameObject bank = GameObjects.getTopAt(new Tile(1948,4956,0));

        return Players.getLocal().distance(bank.getLocation())  <= 3;
    }


    public boolean onStart() {
        JTextField jore = new JTextField("Iron ore");
        JTextField jcoal = new JTextField();



        Object[] content = {
                "Enter the type of ore to use for BF:", jore,
                "How many coal in furnace right now?", jcoal
        };
        int option = JOptionPane.showConfirmDialog(null,content, "Enter all values", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            coaladded=Integer.parseInt(jcoal.getText());
            ore=jore.getText();
        }

        return true;
    }

    public void addConveyor(String ore){
        if (Inventory.contains(ore)){
            if (ore.equals("Coal") && !coalBagFilled && closeToBank()){
                if (!Bank.isOpen()) {
                    Inventory.getFirst("Coal bag").interact("Fill");
                    coalBagFilled = true;
                }
                else
                    Bank.close();
                return;
            }

            GameObject conveyor = GameObjects.getTopAt(new Tile(1943,4967,0));
            if (conveyor.isOnScreen()){

                if (Widgets.getWidget(219,0).isVisible()){
                    Widgets.getWidget(219,0).getChild(1).click();
                    Time.sleep(1200,1700);
                    if (Inventory.getCount()==1) {
                        if (ore.equals("Coal"))
                            coaladded += 27;
                        else
                            smelting = true;
                    }
                }
                else
                    conveyor.interact("Put-ore-on");

            }
            else{
                Walking.walkTileMM(new Tile(1942,4967,0));
            }
        }
        else if(coalBagFilled && !closeToBank()) {
            Inventory.getFirst("Coal bag").interact("Empty");
            coalBagFilled = false;
            Time.sleep(600);
        }
        else{
            handleBanking(ore);
        }
    }

    public void takeBars(){
        if (Inventory.contains("Steel bar")){
            smelting = false;
            coaladded -= 27;
            return;
        }

        GameObject bardispenser = GameObjects.getTopAt(new Tile(1940,4963));
        if (bardispenser.isOnScreen()) {
            if (bardispenser.hasAction("Take")){
                bardispenser.interact("Take");
                if (Widgets.getWidget(28,110).isOnScreen()){
                    Widgets.getWidget(28,110).click();
                }
            }
        }
        else
            Walking.walkTileMM(bardispenser.getLocation(), Random.nextInt(2), Random.nextInt(2));
        if (Inventory.contains("Steel bar")){
            if (Inventory.contains(ore) || Inventory.contains("Coal"))
                smelting=false;
        }
    }


    public int loop() {
        if (Players.getLocal().isMoving()){
            Camera.rotateRandomly();
            log(coaladded);
            return Random.nextInt(1100,1500);
        }
        if (smelting)
            takeBars();
        else{
            if (coaladded >= 81 && !Inventory.contains("Coal")) {
                addConveyor("Iron ore");
            }
            else {
                addConveyor("Coal");
            }
        }
        if (Walking.getRunEnergy() > 70)
            Walking.setRun(true);

        return Random.nextInt(500,800);

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



}