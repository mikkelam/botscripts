package thieeeever;


import org.tbot.graphics.MouseTrail;
import org.tbot.internal.event.listeners.PaintListener;
import org.tbot.methods.Time;
import org.tbot.methods.tabs.Inventory;
import org.tbot.wrappers.GameObject;
import org.tbot.internal.AbstractScript;
import org.tbot.internal.Manifest;
import org.tbot.internal.ScriptCategory;
import org.tbot.methods.GameObjects;
import org.tbot.methods.Random;
import org.tbot.wrappers.Tile;
import org.tbot.wrappers.Timer;
import util.Antiban;
import util.AntibanChoice;
import util.PlayerUtil;
import util.BotscriptsUtil;

import java.awt.*;

@Manifest(
        name = "Martin's Thiever",
        authors = {
                "Martin",
        },
        version = 0.1,
        description = "Thieves from a nearby stall. So far works on Tea stall in Varrock to 20",
        category = ScriptCategory.THIEVING
)
public class Main extends AbstractScript implements PaintListener {
    private final MouseTrail mt = new MouseTrail();
    private final Timer t = new Timer();

    @Override
    public boolean onStart() {
        log("Martin's Thiever has started");
        return super.onStart();
    }

    @Override
    public int loop() {
        int recommendedWait = stealFromStall("Tea stall", "Cup of tea", new Tile(3268, 3410, 0));

        AntibanChoice randomChoice = Antiban.doSomethingRandom();
        log("Took the '" + randomChoice.toString() + "' choice");

        return recommendedWait;
    }

    private int stealFromStall(String stallName, String stealItem, Tile nearbyTile) {
        GameObject stall = GameObjects.getNearest(stallName);

        if(PlayerUtil.isIdle()) {
            boolean playerIsBusy = BotscriptsUtil.interact(stall, "Steal-from", nearbyTile);

            if(playerIsBusy)
                Time.sleepUntil(PlayerUtil::isIdle, Random.nextInt(200, 300));

            while(Inventory.contains(stealItem)) {
                Inventory.drop(Inventory.getFirst(stealItem).getID());

                Time.sleep(200, 500);
            }
        }

        // Wait until the stall respawns
        Time.sleepUntil(() -> GameObjects.getNearest(stallName) == null, Random.nextInt(2000, 3000));

        return Random.nextInt(0, 250);
    }

    @Override
    public void onRepaint(Graphics graphics) {
        mt.draw(graphics);
        mt.setColor(Color.red);

        graphics.setColor(Color.white);
        //graphics.drawString("Martin's Firemaker", 100, 100);
        graphics.drawString("Time elapsed: " + t.getTimeRunningString(), 50, 100);
    }
}
