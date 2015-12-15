package agility;

import org.tbot.methods.GameObjects;
import org.tbot.wrappers.GameObject;
import org.tbot.wrappers.Tile;

/**
 * Created by Jonross on 6/4/2015.
 */
public class GObject {

    private final String object;
    private final Tile[] tile;

    public GObject(final String object, final Tile ... tile){
        this.object = object;
        this.tile = tile;
    }

    public GameObject getObject() {
        if (tile.length > 0) {
            return GameObjects.getNearest(object1 -> object1.getName().equals(object) && object1.getLocation().equals(tile[0]));
        } else {
            return GameObjects.getNearest(object1 -> object1.getName().equals(object));
        }
    }
}
