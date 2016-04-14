package inventorydropper;

import org.tbot.graphics.MouseTrail;
import org.tbot.internal.AbstractScript;
import org.tbot.internal.Manifest;
import org.tbot.internal.ScriptCategory;
import org.tbot.internal.event.listeners.PaintListener;
import org.tbot.methods.Random;
import org.tbot.methods.Skills;
import org.tbot.methods.Time;
import org.tbot.methods.tabs.Inventory;
import org.tbot.wrappers.Item;
import util.Antiban;
import util.BotscriptsUtil;
import util.SkillTracker;

import java.awt.*;

@Manifest(
        name = "Martin's Inv dropper",
        authors = {
                "Martin",
        },
        version = 0.1,
        description = "Drops every type of the item in the first inv slot",
        category = ScriptCategory.OTHER
)
public class Main extends AbstractScript implements PaintListener {
    private final MouseTrail mt = new MouseTrail();
    private final SkillTracker tracker = new SkillTracker(Skills.Skill.WOODCUTTING);


    @Override
    public boolean onStart() {
        return super.onStart();
    }


    @Override
    public int loop() {
        Item i = Inventory.getInSlot(1);

        if(i == null) {
            log("Could not find item in the first slot");
            return Random.nextInt(3000, 20000);
        }

        Inventory.fastDropAll(i.getID());
        Antiban.doSomethingRandom();

        return Random.nextInt(50, 766);
    }

    @Override
    public void onRepaint(Graphics graphics) {
        mt.draw(graphics);
        mt.setColor(Color.ORANGE);
        BotscriptsUtil.showSimpleStats(graphics, tracker);
    }
}
