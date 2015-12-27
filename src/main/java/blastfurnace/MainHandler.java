package blastfurnace;

import java.awt.Graphics;

import org.tbot.client.*;
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
import org.tbot.methods.walking.Walking;
import org.tbot.wrappers.*;
import org.tbot.wrappers.GameObject;

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



    private void handleBanking(String withdraw) {
        if (Bank.isOpen()){
            if (Inventory.isFull())
                Bank.depositAll();

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

                Camera.turnTo(bank);
                Walking.walkTileMM(bank.getLocation(),Random.nextInt(2),Random.nextInt(2));
            }
        }
    }


    public boolean onStart() {
        ore = "Iron ore";
        return true;
    }

    public void addConveyor(String ore){
        if (Inventory.contains(ore)){
            GameObject conveyor = GameObjects.getTopAt(new Tile(1943,4967,0));
            if (conveyor.isOnScreen()){
                if (Widgets.getWidgetByText("Yes") != null){
                    Widgets.getWidgetByText("Yes").click();
                    Time.sleep(1200,1700);
                    if (Inventory.isEmpty()) {
                        if (ore.equals("Coal"))
                            coaladded += 28;
                        else
                            smelting = true;
                    }


                }
                else
                    conveyor.interact("Put-ore-on");

            }
            else{
                Walking.walkTileMM(new Tile(1942,4967,0));
                Camera.turnTo(conveyor);
            }
        }
        else{
            handleBanking(ore);
        }
    }

    public void takeBars(){
        if (Inventory.contains("Steel bar")){
            smelting = false;
            coaladded -=24;
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
        if (smelting)
            takeBars();
        else{
            if (coaladded >= 84) {
                addConveyor("Iron ore");
            }
            else {
                addConveyor("Coal");
            }
        }
        if (Walking.getRunEnergy() > 70)
            Walking.setRun(true);

        return Random.nextInt(1000,3000);

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