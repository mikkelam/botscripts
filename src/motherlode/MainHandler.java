package motherlode;

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
import org.tbot.methods.tabs.Equipment;
import org.tbot.methods.tabs.Inventory;
import org.tbot.methods.walking.Path;
import org.tbot.methods.walking.Walking;
import org.tbot.methods.walking.nodes.WebAction;
import org.tbot.methods.web.Web;
import org.tbot.methods.web.actions.ObjectAction;
import org.tbot.methods.web.areas.WebArea;
import org.tbot.methods.web.nodes.WebNode;
import org.tbot.methods.web.path.WebPath;
import org.tbot.wrappers.*;

@Manifest(
        name = "Motherlode miner",
        authors = {
                "Saph"
        },
        version = 1.1,
        description = "Mines the lower level motherlode",
        category = ScriptCategory.THIEVING
)
/**
 * This script is missing upper level motherlode mining
 */
public class MainHandler extends AbstractScript implements MessageListener, PaintListener {

    private static ObjectAction runecraftAction = new ObjectAction("Rockfall","Mine");



    public boolean onStart() {

        log("Miner started");
        return true;
    }

    public int loop() {
        Player player = Players.getLocal();
        if ( Equipment.getItemInSlot(Equipment.SLOTS_WEAPON).getName().contains("pickaxe")) {//must wear pickaxe
            if (Inventory.isFull()) {
                if (Inventory.contains("Pay-dirt"))
                    log("I should wash");
                else
                    log("I should bank");
            }
            else {
                log("I should mine");
                GameObject ore = GameObjects.getNearest("Ore vein");



//                if (player.getAnimation() == -1) { // standing still
//                    if (ore.interact("Mine")){
//                        Time.sleepUntil(()-> player.getAnimation() == -1, Random.nextInt(1400,2200));
//                }
            }
        }


        return 1000;
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