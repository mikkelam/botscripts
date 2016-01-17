package splash;

import org.tbot.methods.Skills;
import org.tbot.graphics.MouseTrail;
import org.tbot.internal.*;
import org.tbot.internal.event.listeners.PaintListener;
import org.tbot.methods.*;
import org.tbot.methods.tabs.Inventory;
import org.tbot.util.Filter;
import org.tbot.wrappers.NPC;
import org.tbot.wrappers.Timer;
import util.SkillTracker;
import java.awt.*;



@Manifest(
        name = "Splash",
        authors = {
                "Saph"
        },
        version = 0.1,
        description = "Splashes shit",
        category = ScriptCategory.THIEVING
)
public class Main extends AbstractScript implements PaintListener {
    final SkillTracker tracker = new SkillTracker(Skills.Skill.MAGIC);
    MouseTrail mouseTrail = new MouseTrail();
    Timer attackTimer;

    public static final Filter<NPC> targetFilter = new Filter<NPC>() {
        @Override
        public boolean accept(NPC w) {
            if (!w.getName().equals("Rat"))
                return false;

            return w.getInteractingEntity() == null;
        }
    };

    public static final Filter<NPC> attackingFilter = new Filter<NPC>() {
        @Override
        public boolean accept(NPC w) {
            if (!w.getName().equals("Rat"))
                return false;

            return w.isInteractingWithLocalPlayer();
        }
    };


    public boolean onStart() {
        return true;
    }



    public int loop() {

        if (Inventory.getFirst("Mind rune").getStackSize() < 10 || Inventory.getFirst("Air rune").getStackSize()< 10){
            Bank.openNearestBank();
            Time.sleep(5000);
            throw new NullPointerException();
        }
        if (!Players.getLocal().isHealthBarVisible()) {
            NPC rat = Npcs.getNearest(targetFilter);
            rat.interact("Attack");
            attackTimer = new Timer(600000,1200000);
        }
        else{
            if (attackTimer.isFinished()){
                Npcs.getNearest(attackingFilter).interact("Attack");
                attackTimer = new Timer(600000,1200000);
            }
        }
        return Random.nextInt(5000,10000);
    }


    public void onRepaint(Graphics g) {
        this.mouseTrail.draw(g);
        g.drawString("Time running: " + tracker.getFormattedTimeTracking(), 8, 15);
        g.drawString("XP/H: " + tracker.getExperiencePerHour(), 8, 30);
        g.drawString("Time to level: " + tracker.getTimeToLevel(), 8, 45);
        g.drawString("Level: " + tracker.getCurrentLevel(), 8, 60);
    }



}




















