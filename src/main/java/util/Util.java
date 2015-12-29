package util;

import org.tbot.wrappers.GameObject;
import org.tbot.methods.GameObjects;
import org.tbot.methods.walking.Path;
import org.tbot.methods.walking.Walking;
import org.tbot.wrappers.Tile;

public final class Util {
    public static GameObject getSecondNearest(GameObject gb) {
        return GameObjects.getNearest(o -> o.getUID() != gb.getUID() && o.getName().equals(gb.getName()));
    }

    /**
     * A more refined intraction method. It will walk to a nearby tile if the given {GameObject} is null, or
     * walk to it, if it is not on the screen.
     * @param o The {GameObject} you wish to interact with
     * @param action The right-click action "attack", "light", etc. you wish to do on {o}
     * @param nearbyTile A {Tile} close to {o}
     * @return The result of interacting with an object.
     */
    public static boolean interact(org.tbot.wrappers.GameObject o, String action, Tile nearbyTile) {
        // If object is null, create path to nearby tile, and return traverse
        if(o == null) {
            Path path = Walking.findPath(nearbyTile);
            return path != null && path.traverse();
        }

        if(!o.isOnScreen()) {
            Path path = Walking.findPath(o.getLocation().getRandomized(1));

            return path != null && path.traverse();
        }
        return o.interact(action);
    }
}
