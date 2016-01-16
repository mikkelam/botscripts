package herbcleaner;

import org.tbot.concurrency.*;
import org.tbot.graphics.MouseTrail;
import org.tbot.graphics.SkillPaint;
import org.tbot.internal.*;
import org.tbot.internal.event.events.MessageEvent;
import org.tbot.internal.event.listeners.InventoryListener;
import org.tbot.internal.event.listeners.MessageListener;
import org.tbot.internal.event.listeners.PaintListener;
import org.tbot.methods.*;
import org.tbot.methods.Random;
import org.tbot.methods.combat.magic.Magic;
import org.tbot.methods.combat.magic.SpellBooks;
import org.tbot.methods.tabs.Equipment;
import org.tbot.methods.tabs.Inventory;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;


@Manifest(
        name = "Herbcleaner =)",
        authors = {
                "Saph"
        },
        version = 0.1,
        description = "Cleans herbs for profit and exp",
        category = ScriptCategory.MAGIC
)
public class Main extends AbstractScript implements MessageListener, PaintListener {
    String herb = "";
    int toClickIdx =0;
    static final List<Integer> toClick = Arrays.asList(0,4,8,12,16,20,24,25,21,17,13,9,5,1,2,6,10,14,18,22,26,27,23,19,15,11,7,3);



    public boolean onStart() {
        JLabel info = new JLabel("You should have the grimy herbs already");


        JTextField jtherb = new JTextField("Grimy ranarr weed");

        Object[] content = {
                "", info,
                "Enter the herb you want to clean:", jtherb
        };
        int option = JOptionPane.showConfirmDialog(null,content, "Enter all values", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            herb = jtherb.getText();
        }
        log(herb);

        return true;
    }



    public int loop() {
        if (Inventory.contains(herb)){
            if (Bank.isOpen())
                {Bank.close();return Random.nextInt(500,1500);}
            clean();
        }
        else{
            banking();
            return Random.nextInt(500,1500);
        }

        return Random.nextInt(123, 171);
    }
    public void clean(){
        if (toClickIdx>= 28)
            Inventory.getFirst(herb).click();
        else {
            try {
                Inventory.getInSlot(toClick.get(toClickIdx)).click();
            }
            catch (NullPointerException np){}
        }
            toClickIdx++;

    }

    public void banking(){
        if (Bank.isOpen()){
            Bank.depositAll();
            if (Bank.contains(herb))
               Bank.withdrawAll(herb);
            else
                System.exit(0);
            toClickIdx=0;
        }
        else{
            Bank.open();

        }
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




















