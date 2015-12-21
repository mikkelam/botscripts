package blastfurnace;

import java.awt.Graphics;
import java.lang.reflect.Array;
import javax.swing.JOptionPane;

import com.sun.org.apache.xpath.internal.axes.WalkingIterator;
import org.tbot.graphics.MouseTrail;
import org.tbot.graphics.SkillPaint;
import org.tbot.internal.AbstractScript;
import org.tbot.internal.Manifest;
import org.tbot.internal.ScriptCategory;
import org.tbot.internal.event.events.MessageEvent;
import org.tbot.internal.event.listeners.MessageListener;
import org.tbot.internal.event.listeners.PaintListener;
import org.tbot.methods.*;
import org.tbot.methods.tabs.Equipment;
import org.tbot.methods.tabs.Inventory;
import org.tbot.methods.walking.Path;
import org.tbot.methods.walking.Walking;
import org.tbot.methods.walking.nodes.WebAction;
import org.tbot.methods.web.Web;
import org.tbot.methods.web.actions.ObjectAction;
import org.tbot.methods.web.areas.WebArea;
import org.tbot.methods.web.banks.WebBank;
import org.tbot.methods.web.nodes.WebNode;
import org.tbot.methods.web.path.WebPath;
import org.tbot.wrappers.*;

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
 * This script is missing upper level motherlode mining
 */
public class MainHandler extends AbstractScript implements MessageListener, PaintListener {
    String ore1;
    String ore2;
    String[] ores;
    int added = 0;



    private void handleBanking() {
        if (Bank.isOpen()){
            if (Inventory.isFull()){
                Bank.depositAll();
                if (added ==2)
                    added=0;
            }
            int oreindex = Random.randomIntFromArray(1,1,1,1,1,1,0,1,1,1,1,0,1,1,0,0,1,1);
            Item first = Bank.getItem(ores[oreindex]);
            Mouse.move(first.getRandomPoint());
            Time.sleep(100,300);
            first.interact("Withdraw-14");
            Time.sleep(300,500);
            Item second = Bank.getItem(ores[(oreindex+1)%2]);
            Mouse.move(second.getRandomPoint());
            Time.sleep(100,300);
            second.interact("Withdraw-All");
            Time.sleep(300,500);

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
        ore1 = "Iron ore";
        ore2 = "Coal";
        ores = new String[]{ore1, ore2};
        return true;
    }

    public int loop() {
        log(added);
        if (added < 2){
            if (Inventory.contains(ore1) && Inventory.contains(ore2)){
                GameObject conveyor = GameObjects.getTopAt(new Tile(1943,4967,0));
                if (conveyor.isOnScreen()){
                    if (Widgets.getWidgetByText("Yes") != null){
                        Widgets.getWidgetByText("Yes").click();
                        Time.sleep(1000,1500);
                        if (Widgets.getWidgetByText("Yes") == null)
                            added+=1;

                    }
                    conveyor.interact("Put-ore-on");
                }
                else{
                    Walking.walkTileMM(new Tile(1942,4967,0));
                    Camera.turnTo(conveyor);
                }


            }
            else{
                handleBanking();
            }
        }
        else{
            GameObject bardispenser = GameObjects.getTopAt(new Tile(1940,4963));
            Walking.walkTileMM(bardispenser.getLocation(),Random.nextInt(2),Random.nextInt(2));
            if (bardispenser.hasAction("Take"))
                bardispenser.interact("Take");
            if (Inventory.contains("Steel bar"));
                handleBanking();

        }


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