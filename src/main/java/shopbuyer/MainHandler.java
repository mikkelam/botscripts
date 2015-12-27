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

//TODO: Stops at 10000 coins, fix this somehow
public class MainHandler extends AbstractScript implements MessageListener, PaintListener {
    String shopkeeper;
    String itemtext="";
    int stopstack;
    boolean f2p;

    public boolean onStart() {
        JLabel info = new JLabel("start near shopkeeper with coins in inventory");


        JTextField shopkeepertf = new JTextField();
        JTextField itemtf = new JTextField();
        JTextField stopstacktf = new JTextField();
        JCheckBox f2ptf = new JCheckBox();



        Object[] content = {
                "", info,
                "Enter exact name of shopkeeper", shopkeepertf,
                "Enter exact name of item you want to purchase", itemtf,

                "Enter stack size to stop purchasing at", stopstacktf,
                "Use free to play worlds?", f2ptf
        };
        int option = JOptionPane.showConfirmDialog(null,content, "Enter all values", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION){
            shopkeeper = shopkeepertf.getText();
            itemtext = itemtf.getText();
            stopstack = Integer.parseInt(stopstacktf.getText());
            f2p = f2ptf.isSelected();

        }

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
            Npcs.getNearest(shopkeeper).interact("Trade");
        }
        else{
            ShopItem item = Shop.getItem(itemtext);
            if (item.getStackSize()>stopstack && coins.getStackSize() > 10000) {

                int chance = Random.nextInt(1, 11);
                String interaction = "Buy 10";
                if (chance == 8)
                    interaction = "Examine";
                else if (chance == 9)
                    interaction = "Buy 5";
                item.interact(interaction);
                Time.sleep(100,150);
                Mouse.move(item.getRandomPoint());
            }
            else {
                Time.sleep(2000,7000);
                if (f2p)
                    Game.instaHopNextF2P();
                else
                    Game.instaHopNextP2P();
                Time.sleep(1000,3000);
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

















