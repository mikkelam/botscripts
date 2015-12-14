package shopbuyer;

import java.awt.*;
import java.util.*;

import org.tbot.client.reflection.wrappers.WorldDataWrapper;
import org.tbot.graphics.MouseTrail;
import org.tbot.graphics.SkillPaint;
import org.tbot.internal.AbstractScript;
import org.tbot.internal.Manifest;
import org.tbot.internal.ScriptCategory;
import org.tbot.internal.event.events.MessageEvent;
import org.tbot.internal.event.listeners.MessageListener;
import org.tbot.internal.event.listeners.PaintListener;
import org.tbot.methods.*;
import org.tbot.methods.Random;
import org.tbot.methods.tabs.Inventory;
import org.tbot.wrappers.Item;
import org.tbot.wrappers.NPC;
import org.tbot.wrappers.WorldData;

import javax.swing.*;


@Manifest(
        name = "Shopbuyer ayylmfao",
        authors = {
                "Saph"
        },
        version = 0.1,
        description = "Shops Death rune",
        category = ScriptCategory.MONEY_MAKING
)
public class MainHandler extends AbstractScript implements MessageListener, PaintListener {
    public boolean onStart() {
        //TODO dont hardcode seller and GUI
        return true;
    }

    @Override
    public void onFinish() {
        super.onFinish();

        log("Finished for some reason");

    }

    public int loop() {
        Item coins = Inventory.getFirst("Coins");
        if (coins == null)
            return -1;
        if( !Shop.isOpen()){
            NPC aubury = Npcs.getNearest("Aubury");
            aubury.interact("Trade");
        }
        else{
            ShopItem item = Shop.getItem("Death rune");
            if (item.getStackSize()>0 && coins.getStackSize() > 1000) {
                int chance = Random.nextInt(1, 10);
                String interaction = "Buy 10";
                if (chance == 10)
                    interaction = "Examine";
                else if (chance == 9)
                    interaction = "Buy 9";
                item.interact(interaction);
            }
            else {
                Time.sleep(2);
                Game.instaHopNextP2P();
            }
        }


        return Random.nextInt(400,800);
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

















