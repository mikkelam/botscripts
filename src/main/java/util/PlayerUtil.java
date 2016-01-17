package util;

import org.tbot.methods.Players;
import org.tbot.methods.Skills;

public final class PlayerUtil {
    public static boolean isIdle() {
        return Players.getLocal().getAnimation() == -1;
    }
    public static boolean isMoving() {return Players.getLocal().isMoving();}

    public static int currentHP(){return Players.getLocal().getCurrentHealth();}

    private PlayerUtil() { }

    public static boolean needToEat() {
        return (int) (((double) Skills.getCurrentLevel(Skills.Skill.HITPOINTS) / (double) Skills
                .getRealLevel(Skills.Skill.HITPOINTS)) * 100) < 60;
    }
}
