package util;

import org.tbot.methods.Players;

public final class PlayerUtil {
    public static boolean isIdle() {
        return Players.getLocal().getAnimation() == -1;
    }

    private PlayerUtil() { }
}
