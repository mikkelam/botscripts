package superheat;

import java.awt.*;
import java.util.*;
import javax.swing.*;

import javafx.scene.transform.NonInvertibleTransformException;
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
import org.tbot.methods.combat.magic.Magic;
import org.tbot.methods.combat.magic.SpellBooks;
import org.tbot.methods.tabs.Equipment;
import org.tbot.methods.tabs.Inventory;
import org.tbot.util.Filter;


@Manifest(
        name = "Superheat yo",
        authors = {
                "Saph"
        },
        version = 0.1,
        description = "Superheat anything",
        category = ScriptCategory.MAGIC
)
public class MainHandler extends AbstractScript implements MessageListener, PaintListener {
    String[] choices = {
            "bronze bar",
            "iron bar",
            "silver bar",
            "steel bar",
            "gold bar",
            "mithril bar",
            "adamant bar",
            "runite bar"
    };
    Map<String, ArrayList<Map.Entry<Integer,String>>> Withdrawals = new HashMap<String, ArrayList<Map.Entry<Integer,String>>>(){{
        put("bronze bar", new ArrayList<>(Arrays.asList(
                new AbstractMap.SimpleEntry<>(13, "Tin ore"),
                new AbstractMap.SimpleEntry<>(13, "Copper ore")
        )));
        put("iron bar", new ArrayList<>(Arrays.asList(
                new AbstractMap.SimpleEntry<>(27, "Iron ore")
        )));
        put("silver bar", new ArrayList<>(Arrays.asList(
                new AbstractMap.SimpleEntry<>(27, "Silver ore")
        )));
        put("steel bar", new ArrayList<>(Arrays.asList(
                new AbstractMap.SimpleEntry<>(9, "Iron ore"),
                new AbstractMap.SimpleEntry<>(18, "Coal")
        )));
        put("gold bar", new ArrayList<>(Arrays.asList(
                new AbstractMap.SimpleEntry<>(27, "Gold ore")
        )));
        put("mithril bar", new ArrayList<>(Arrays.asList(
                new AbstractMap.SimpleEntry<>(5, "Mithril ore"),
                new AbstractMap.SimpleEntry<>(20, "Coal")
        )));
        put("adamant bar", new ArrayList<>(Arrays.asList(
                new AbstractMap.SimpleEntry<>(3, "Adamantite ore"),
                new AbstractMap.SimpleEntry<>(18, "Coal")
        )));
        put("runite bar", new ArrayList<>(Arrays.asList(
                new AbstractMap.SimpleEntry<>(2, "Runite ore"),
                new AbstractMap.SimpleEntry<>(16, "Coal")
        )));
    }};
    String smelting;
    String ore1 = null;
    String ore2 = null;

    public boolean onStart() {
        JLabel info = new JLabel("START WITH STAFF OF FIRE ON AND NATURE RUNES IN INVENTORY");


        JComboBox<String> cb = new JComboBox<String>(choices);

        Object[] content = {
                "", info,
                "Enter the bar you want to smelt:", cb
        };
        int option = JOptionPane.showConfirmDialog(null,content, "Enter all your values", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            smelting = cb.getSelectedItem().toString();

            if (Withdrawals.get(smelting).size() > 2)
                ore2 = Withdrawals.get(smelting).get(1).getValue();
            else{
                ore1= Withdrawals.get(smelting).get(0).getValue();
                ore2=ore1;
            }
        }
        log(smelting);

        return true;
    }

    @Override
    public void onFinish() {
        super.onFinish();
        log("Finished for some reason");
    }

    public int loop() {
        if (Inventory.contains("Nature rune") &&
            Equipment.getItemInSlot(Equipment.SLOTS_WEAPON).getName().equals("Staff of fire")){
            if (Inventory.contains(ore1) &&  Inventory.contains(ore2)) {
                smelt(ore1);
            }
            else{
                boolean oresLeft = newOres();
                if (!oresLeft)
                    return -1;
            }
        }
        else{
            log("No nats or staff of fire not in weapon slot");
            return -1;
        }
        return Random.nextInt(1200, 1500);
    }
    public boolean smelt(String item){
        Magic.cast(SpellBooks.Modern.SUPERHEAT_ITEM);

        Time.sleepUntil(()->Inventory.isOpen(), Random.nextInt(700,900));
        if (Withdrawals.get(smelting).size()==2)
            Inventory.getLast(item).click();
        else
            Inventory.getItemClosestToMouse(item).click();

        return true;
    }

    public boolean newOres(){

        Bank.open();
        Time.sleepUntil(() -> {
            return Bank.isOpen();
        },Random.nextInt(1100,1500));
        if (Bank.isOpen()){
            Bank.depositAll(smelting);
            Time.sleepUntil(() -> {
                return !Inventory.contains(smelting);
            },Random.nextInt(1100,1500));

            ArrayList<Map.Entry<Integer,String>> ores = Withdrawals.get(smelting);
            for (Map.Entry<Integer,String> ore: ores) {
                int amount = ore.getKey();
                String name = ore.getValue();
                if (Bank.contains(name)) {
                    Bank.withdraw(name, amount);
                    Time.sleepUntil(() -> {
                        return Inventory.contains(name);
                    },Random.nextInt(1100,1500));

                }
                else{
                    log("wtf");
                    return false;
                }
            }
            Bank.close();
            Time.sleepUntil(() -> {
                return !Bank.isOpen();
            },Random.nextInt(1100,1500));
        }

        return true;
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

















